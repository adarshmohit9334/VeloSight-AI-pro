import os
import time
import cv2
import numpy as np
import logging
from typing import Dict, Any, Callable

from app.detector import VehicleDetector
from app.tracker import CentroidTracker
from app.density import DensityEngine
from app.congestion import CongestionEngine
from app.signal_recommendation import SignalRecommendationEngine
from app.anpr import ANPREngine

logger = logging.getLogger("velosight.analyzer")

class VideoAnalyzer:
    def __init__(self, confidence_threshold: float = 0.40, frame_skip: int = 2):
        self.detector = VehicleDetector(confidence_threshold=confidence_threshold)
        self.tracker = CentroidTracker(max_disappeared=15)
        self.density_engine = DensityEngine()
        self.congestion_engine = CongestionEngine()
        self.signal_engine = SignalRecommendationEngine()
        self.anpr_engine = ANPREngine(
            model_path=os.environ.get("ANPR_MODEL_PATH", ""),
            enabled=os.environ.get("ANPR_ENABLED", "true").lower() == "true"
        )
        self.frame_skip = max(1, frame_skip)
        self.anpr_frame_interval = int(os.environ.get("ANPR_FRAME_INTERVAL", "5"))

    def process_video(
        self,
        analysis_id: str,
        input_video_path: str,
        output_video_dir: str,
        progress_callback: Callable[[float, str], None] = None
    ) -> Dict[str, Any]:
        """
        Processes a video file and generates full traffic metrics & annotated output video.
        """
        if not os.path.exists(input_video_path):
            raise FileNotFoundError(f"Input video not found: {input_video_path}")

        os.makedirs(output_video_dir, exist_ok=True)
        output_video_path = os.path.join(output_video_dir, f"{analysis_id}_processed.mp4")

        cap = cv2.VideoCapture(input_video_path)
        if not cap.isOpened():
            raise ValueError(f"Could not open video file: {input_video_path}")

        width = int(cap.get(cv2.CAP_PROP_FRAME_WIDTH))
        height = int(cap.get(cv2.CAP_PROP_FRAME_HEIGHT))
        fps = int(cap.get(cv2.CAP_PROP_FPS)) or 25
        total_frames = int(cap.get(cv2.CAP_PROP_FRAME_COUNT)) or 1
        duration_sec = round(total_frames / float(fps), 2)

        # Counting Line setup (Horizontal line at 60% of frame height)
        line_y = int(height * 0.60)

        # Setup VideoWriter
        fourcc = cv2.VideoWriter_fourcc(*'mp4v')
        out = cv2.VideoWriter(output_video_path, fourcc, fps, (width, height))

        vehicle_counts = {
            "car": 0,
            "motorcycle": 0,
            "bus": 0,
            "truck": 0,
            "bicycle": 0
        }

        # ANPR tracking: track_id -> { "v_class": str, "plate_number": str, "plate_status": str, "det_conf": float, "ocr_conf": float, "first_seen": float, "last_seen": float }
        plate_observations = {}

        crossed_ids = set()
        previous_positions: Dict[int, list] = {}
        speed_measurements = []
        max_vehicles_in_frame = 0
        total_roi_occupancy_sum = 0.0

        start_time = time.time()
        frame_idx = 0

        while cap.isOpened():
            ret, frame = cap.read()
            if not ret or frame is None:
                break

            frame_idx += 1

            # Update progress
            if progress_callback and frame_idx % 10 == 0:
                progress = min(round((frame_idx / float(total_frames)) * 100, 1), 99.0)
                progress_callback(progress, f"Processing frame {frame_idx}/{total_frames}")

            # Run detection & tracking (skip frames for speed if configured)
            if frame_idx % self.frame_skip == 0:
                raw_detections = self.detector.detect(frame)
                tracked_objects = self.tracker.update(raw_detections)
            else:
                tracked_objects = self.tracker.update([])

            vehicles_in_frame = len(tracked_objects)
            max_vehicles_in_frame = max(max_vehicles_in_frame, vehicles_in_frame)

            # Compute ROI occupancy
            frame_area = width * height
            occupied_area = sum([(det['box'][2] - det['box'][0]) * (det['box'][3] - det['box'][1]) for det in tracked_objects if 'box' in det])
            roi_occupancy = min(occupied_area / float(frame_area), 1.0)
            total_roi_occupancy_sum += roi_occupancy

            # Virtual line crossing & Speed estimation logic
            for obj in tracked_objects:
                track_id = obj['track_id']
                v_class = obj.get('class', 'car')
                box = obj.get('box', [0, 0, 0, 0])
                cx, cy = obj['centroid']

                # Initialize observation if needed
                current_time_sec = frame_idx / float(fps)
                if track_id not in plate_observations:
                    plate_observations[track_id] = {
                        "trackId": track_id,
                        "vehicleType": v_class,
                        "plateNumber": "",
                        "plateStatus": "UNKNOWN",
                        "detectionConfidence": 0.0,
                        "ocrConfidence": 0.0,
                        "firstSeen": current_time_sec,
                        "lastSeen": current_time_sec
                    }
                else:
                    plate_observations[track_id]["lastSeen"] = current_time_sec

                # Speed estimation based on displacement per frame
                if track_id in previous_positions:
                    prev_cx, prev_cy = previous_positions[track_id]
                    dist_px = np.hypot(cx - prev_cx, cy - prev_cy)
                    # Convert px displacement to estimated km/h (scaled)
                    speed_kmh = (dist_px / (width * 0.001)) * (fps / 25.0) * 0.8
                    if 5.0 <= speed_kmh <= 120.0:
                        speed_measurements.append(speed_kmh)

                previous_positions[track_id] = [cx, cy]

                # Line crossing check
                if track_id not in crossed_ids:
                    if abs(cy - line_y) < (height * 0.05):
                        crossed_ids.add(track_id)
                        vehicle_counts[v_class] = vehicle_counts.get(v_class, 0) + 1

                # ANPR processing
                obs = plate_observations[track_id]
                anpr_debug = os.environ.get("ANPR_DEBUG", "false").lower() == "true"
                
                # We will process OCR multiple times and store all reads in a temporary list for consensus
                if "reads" not in obs:
                    obs["reads"] = []
                    
                if self.anpr_engine.enabled and frame_idx % self.anpr_frame_interval == 0:
                    # Stop processing if we already have a very high confidence read (e.g. > 90%)
                    best_conf_so_far = max([r["conf"] for r in obs["reads"]]) if obs["reads"] else 0.0
                    if best_conf_so_far < 0.90:
                        anpr_res = self.anpr_engine.process_vehicle(frame, box)
                        
                        if anpr_debug and (anpr_res["plate_status"] != "UNKNOWN"):
                            logger.info(f"DEBUG ANPR Track {track_id}: Plate: '{anpr_res['plate_number']}' Status: {anpr_res['plate_status']} (Det: {anpr_res['detection_confidence']}, OCR: {anpr_res['ocr_confidence']})")
                            
                        if anpr_res["plate_number"]:
                            obs["reads"].append({
                                "text": anpr_res["plate_number"],
                                "conf": anpr_res["ocr_confidence"],
                                "det_conf": anpr_res["detection_confidence"]
                            })
                            # Keep the highest confidence one tentatively active for HUD overlay
                            if anpr_res["ocr_confidence"] > obs.get("ocrConfidence", 0):
                                obs["plateNumber"] = anpr_res["plate_number"]
                                obs["plateStatus"] = anpr_res["plate_status"]
                                obs["detectionConfidence"] = anpr_res["detection_confidence"]
                                obs["ocrConfidence"] = anpr_res["ocr_confidence"]

                # Draw bounding box & track ID overlay on output frame
                x1, y1, x2, y2 = box
                color = (0, 255, 120) if v_class == 'car' else (255, 165, 0) if v_class in ['bus', 'truck'] else (255, 200, 0)
                cv2.rectangle(frame, (x1, y1), (x2, y2), color, 2)
                
                label = f"ID:{track_id} {v_class}"
                if obs["plateNumber"] and (os.environ.get("ANPR_OVERLAY_ENABLED", "true").lower() == "true" or anpr_debug):
                    label += f" [{obs['plateNumber']} {int(obs['ocrConfidence']*100)}%]"
                
                cv2.putText(frame, label, (x1, max(y1 - 6, 15)), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 255, 255), 2)

            # Draw virtual counting line
            cv2.line(frame, (0, line_y), (width, line_y), (0, 0, 255), 2)
            cv2.putText(frame, "COUNTING LINE", (15, line_y - 8), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 0, 255), 2)

            # Overlay HUD stats header
            total_counted = len(crossed_ids)
            hud_text = f"VELOSIGHT AI | Vehicles: {total_counted} | Cars: {vehicle_counts['car']} | Bikes: {vehicle_counts['motorcycle']} | Heavy: {vehicle_counts['bus'] + vehicle_counts['truck']}"
            cv2.rectangle(frame, (0, 0), (width, 40), (15, 23, 42), -1)
            cv2.putText(frame, hud_text, (15, 25), cv2.FONT_HERSHEY_SIMPLEX, 0.55, (0, 230, 255), 2)

            out.write(frame)

        cap.release()
        out.release()

        processing_duration = round(time.time() - start_time, 2)
        total_vehicles = len(crossed_ids) if len(crossed_ids) > 0 else max_vehicles_in_frame

        # Compute summary metrics
        avg_occupancy = total_roi_occupancy_sum / float(max(frame_idx, 1))
        density_res = self.density_engine.calculate(total_vehicles, roi_occupancy_ratio=avg_occupancy)

        avg_speed = round(float(np.mean(speed_measurements)), 1) if speed_measurements else 36.5
        congestion_res = self.congestion_engine.calculate(density_res['score'], avg_speed_kmh=avg_speed)

        # Directional Split heuristic for Signal Recommendation (60% NS vs 40% EW approximation or count based)
        ns_score = min(density_res['score'] * 1.1, 100.0)
        ew_score = max(density_res['score'] * 0.7, 15.0)
        signal_rec = self.signal_engine.recommend(ns_score, ew_score)

        # Incident / anomaly heuristic
        potential_incident = False
        incident_description = None
        if avg_speed < 12.0 and density_res['score'] > 65.0:
            potential_incident = True
            incident_description = "Unusual traffic slowdown detected. High vehicle clustering with low movement."

        # Compute consensus for ANPR observations
        final_observations = []
        for track_id, obs in plate_observations.items():
            reads = obs.get("reads", [])
            # Only include if we actually saw the vehicle enough or got reads
            if len(reads) > 0:
                # frequency map
                freq = {}
                for r in reads:
                    t = r["text"]
                    if t not in freq:
                        freq[t] = {"count": 0, "max_conf": 0.0, "max_det": 0.0}
                    freq[t]["count"] += 1
                    freq[t]["max_conf"] = max(freq[t]["max_conf"], r["conf"])
                    freq[t]["max_det"] = max(freq[t]["max_det"], r["det_conf"])
                
                # Pick the most frequent text, break ties with highest max_conf
                best_text = max(freq.keys(), key=lambda k: (freq[k]["count"], freq[k]["max_conf"]))
                obs["plateNumber"] = best_text
                obs["ocrConfidence"] = freq[best_text]["max_conf"]
                obs["detectionConfidence"] = freq[best_text]["max_det"]
                obs["plateStatus"] = "READ" if obs["ocrConfidence"] >= 0.50 else "LOW_CONFIDENCE"
            else:
                if obs["plateStatus"] != "UNKNOWN" and obs["plateStatus"] != "NOT_READABLE":
                    obs["plateStatus"] = "NOT_READABLE"
            
            # Clean up temporary field
            if "reads" in obs:
                del obs["reads"]
            
            # Filter out vehicles where we never even tried to read a plate, or if the user wants them all, we can include them
            # For now, include all tracked vehicles, but mark those with no plates clearly.
            if obs["plateStatus"] == "UNKNOWN":
                obs["plateStatus"] = "NO_PLATE_DETECTED"

            final_observations.append(obs)

        return {
            "analysisId": analysis_id,
            "status": "COMPLETED",
            "videoMetadata": {
                "filename": os.path.basename(input_video_path),
                "resolution": f"{width}x{height}",
                "fps": fps,
                "totalFrames": total_frames,
                "durationSec": duration_sec,
                "processedFilePath": output_video_path
            },
            "totalVehicles": total_vehicles,
            "vehicleCounts": vehicle_counts,
            "peakVehicleCount": max_vehicles_in_frame,
            "avgVehiclesPerMinute": round((total_vehicles / max(duration_sec, 1.0)) * 60.0, 1),
            "averageEstimatedSpeedKmh": avg_speed,
            "density": density_res,
            "congestion": congestion_res,
            "signalRecommendation": signal_rec,
            "potentialIncident": potential_incident,
            "incidentDescription": incident_description,
            "processingDurationSec": processing_duration,
            "vehicleObservations": final_observations
        }
