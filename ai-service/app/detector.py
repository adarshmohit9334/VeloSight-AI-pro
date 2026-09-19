import os
import cv2
import numpy as np
import logging

logger = logging.getLogger("velosight.detector")

# COCO Vehicle Class IDs
COCO_VEHICLES = {
    2: 'car',
    3: 'motorcycle',
    5: 'bus',
    7: 'truck',
    1: 'bicycle'
}

class VehicleDetector:
    def __init__(self, model_path: str = "models/yolov8n.pt", confidence_threshold: float = 0.40):
        self.model_path = model_path
        self.confidence_threshold = confidence_threshold
        self.yolo_model = None
        self.use_yolo = False
        
        # Try loading YOLO
        try:
            from ultralytics import YOLO
            # If specified file exists or auto-download works
            logger.info(f"Initializing YOLO detector with threshold={confidence_threshold}")
            self.yolo_model = YOLO(model_path if os.path.exists(model_path) else "yolov8n.pt")
            self.use_yolo = True
            logger.info("YOLO model loaded successfully.")
        except Exception as e:
            logger.warning(f"YOLO model initialization failed ({str(e)}). Falling back to OpenCV MOG2 detector.")
            self.use_yolo = False
            self.bg_subtractor = cv2.createBackgroundSubtractorMOG2(history=500, varThreshold=50, detectShadows=True)

    def detect(self, frame: np.ndarray) -> list:
        """
        Detects vehicles in frame.
        Returns list of dicts: [{'box': [x1, y1, x2, y2], 'class': str, 'confidence': float}]
        """
        detections = []
        if self.use_yolo and self.yolo_model is not None:
            try:
                results = self.yolo_model(frame, conf=self.confidence_threshold, verbose=False)[0]
                for box in results.boxes:
                    cls_id = int(box.cls[0].item())
                    conf = float(box.conf[0].item())
                    if cls_id in COCO_VEHICLES and conf >= self.confidence_threshold:
                        xyxy = box.xyxy[0].cpu().numpy().astype(int).tolist()
                        detections.append({
                            'box': xyxy,
                            'class': COCO_VEHICLES[cls_id],
                            'confidence': round(conf, 3)
                        })
                return detections
            except Exception as e:
                logger.error(f"YOLO inference error: {e}. Switching to OpenCV fallback.")
                self.use_yolo = False

        # OpenCV Fallback Detection
        gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
        fg_mask = self.bg_subtractor.apply(gray)
        _, thresh = cv2.threshold(fg_mask, 200, 255, cv2.THRESH_BINARY)
        kernel = cv2.getStructuringElement(cv2.MORPH_RECT, (5, 5))
        dilated = cv2.dilate(thresh, kernel, iterations=2)
        contours, _ = cv2.findContours(dilated, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

        frame_h, frame_w = frame.shape[:2]
        min_area = int(frame_w * frame_h * 0.003)

        for contour in contours:
            area = cv2.contourArea(contour)
            if area > min_area:
                x, y, w, h = cv2.boundingRect(contour)
                # Aspect ratio classification approximation
                aspect_ratio = w / float(h)
                if area > min_area * 5:
                    v_class = 'bus' if aspect_ratio > 1.2 else 'truck'
                elif area > min_area * 2:
                    v_class = 'car'
                else:
                    v_class = 'motorcycle' if aspect_ratio < 0.8 else 'car'

                detections.append({
                    'box': [x, y, x + w, y + h],
                    'class': v_class,
                    'confidence': 0.85
                })

        return detections
