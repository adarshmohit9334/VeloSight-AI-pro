import os
import re
import cv2
import numpy as np
import logging
from typing import Optional, Tuple, Dict

logger = logging.getLogger("velosight.anpr")

class ANPREngine:
    def __init__(self, model_path: str = None, enabled: bool = True):
        self.enabled = enabled
        self.model_path = model_path
        self.reader = None
        self.use_yolo_plate = False
        self.yolo_model = None

        if not self.enabled:
            logger.info("ANPR is disabled via configuration.")
            return

        try:
            import easyocr
            # Initialize EasyOCR (English + fallback characters, running on CPU if CUDA is not available)
            logger.info("Initializing EasyOCR for ANPR...")
            self.reader = easyocr.Reader(['en'], gpu=False, verbose=False)
            logger.info("EasyOCR initialized successfully.")
        except Exception as e:
            logger.error(f"Failed to initialize EasyOCR: {e}")
            self.enabled = False
            return

        if self.model_path and os.path.exists(self.model_path):
            try:
                from ultralytics import YOLO
                logger.info(f"Loading custom ANPR YOLO model from {self.model_path}")
                self.yolo_model = YOLO(self.model_path)
                self.use_yolo_plate = True
            except Exception as e:
                logger.warning(f"Failed to load custom ANPR YOLO model from {self.model_path}: {e}")
                self.use_yolo_plate = False
        else:
            logger.info("No custom ANPR YOLO model provided. Using heuristic crop and OpenCV Haar Cascade fallback.")
            cascade_path = cv2.data.haarcascades + 'haarcascade_russian_plate_number.xml'
            if os.path.exists(cascade_path):
                self.plate_cascade = cv2.CascadeClassifier(cascade_path)
            else:
                self.plate_cascade = None

    def _normalize_indian_plate(self, text: str) -> str:
        """
        Normalizes OCR text into standard Indian license plate format (e.g., MH12AB1234)
        """
        # Convert to upper case and remove non-alphanumeric characters
        text = text.upper()
        text = re.sub(r'[^A-Z0-9]', '', text)

        # Standard Indian plate regex check (StateCode 2 chars, District 2 digits, Series 1-3 chars, Number 4 digits)
        # e.g., MH12AB1234
        # We will do a loose validation to allow slight OCR errors, but enforce basic length
        if len(text) >= 6 and len(text) <= 12:
            return text
        return ""

    def validate_indian_plate(self, text: str) -> bool:
        """
        Checks if the text looks strongly like an Indian license plate
        """
        pattern = r'^[A-Z]{2}[0-9]{1,2}[A-Z]{0,3}[0-9]{1,4}$'
        return bool(re.match(pattern, text))

    def extract_plate(self, frame: np.ndarray, vehicle_box: list) -> Tuple[Optional[np.ndarray], float]:
        """
        Attempts to crop the license plate from a vehicle bounding box.
        Returns (cropped_img, detection_confidence)
        """
        if not self.enabled:
            return None, 0.0

        padding = float(os.environ.get("ANPR_VEHICLE_CROP_PADDING", "0.05"))
        x1, y1, x2, y2 = [int(v) for v in vehicle_box]
        
        # Apply padding
        w_box, h_box = x2 - x1, y2 - y1
        x1 = max(0, int(x1 - (w_box * padding)))
        y1 = max(0, int(y1 - (h_box * padding)))
        x2 = min(frame.shape[1], int(x2 + (w_box * padding)))
        y2 = min(frame.shape[0], int(y2 + (h_box * padding)))
        
        vehicle_roi = frame[y1:y2, x1:x2]
        if vehicle_roi.size == 0:
            return None, 0.0

        plate_crop = None
        det_conf = 0.0

        if self.use_yolo_plate and self.yolo_model is not None:
            results = self.yolo_model(vehicle_roi, verbose=False)[0]
            if len(results.boxes) > 0:
                best_box = max(results.boxes, key=lambda b: b.conf[0].item())
                px1, py1, px2, py2 = best_box.xyxy[0].cpu().numpy().astype(int)
                det_conf = float(best_box.conf[0].item())
                min_det_conf = float(os.environ.get("ANPR_MIN_DETECTION_CONFIDENCE", "0.25"))
                if det_conf >= min_det_conf:
                    plate_crop = vehicle_roi[max(0, py1):min(vehicle_roi.shape[0], py2), max(0, px1):min(vehicle_roi.shape[1], px2)]
                    return plate_crop, det_conf

        if self.plate_cascade is not None:
            gray_roi = cv2.cvtColor(vehicle_roi, cv2.COLOR_BGR2GRAY)
            plates = self.plate_cascade.detectMultiScale(gray_roi, scaleFactor=1.1, minNeighbors=4, minSize=(30, 10))
            if len(plates) > 0:
                px, py, pw, ph = plates[0]
                plate_crop = vehicle_roi[py:py+ph, px:px+pw]
                det_conf = 0.65
                return plate_crop, det_conf

        roi_h, roi_w = vehicle_roi.shape[:2]
        bottom_y = int(roi_h * 0.60) # use 40% instead of 35%
        plate_crop = vehicle_roi[bottom_y:roi_h, 0:roi_w]
        det_conf = 0.30
        
        return plate_crop, det_conf

    def _run_ocr_on_variant(self, img: np.ndarray) -> Tuple[str, float]:
        try:
            result = self.reader.readtext(img)
            if result:
                full_text = "".join([res[1] for res in result])
                conf = max([res[2] for res in result])
                norm = self._normalize_indian_plate(full_text)
                if norm:
                    return norm, float(conf)
        except Exception:
            pass
        return "", 0.0

    def recognize_plate(self, plate_crop: np.ndarray) -> Tuple[str, float]:
        """
        Runs OCR on the cropped plate image using multiple preprocessing pipelines.
        Returns (normalized_plate_text, ocr_confidence)
        """
        if not self.enabled or self.reader is None or plate_crop is None or plate_crop.size == 0:
            return "", 0.0

        gray = cv2.cvtColor(plate_crop, cv2.COLOR_BGR2GRAY)
        gray = cv2.resize(gray, None, fx=2.5, fy=2.5, interpolation=cv2.INTER_CUBIC)
        
        # Pipeline 1: Bilateral Filter (reduces noise, keeps edges)
        blur = cv2.bilateralFilter(gray, 11, 17, 17)
        
        # Pipeline 2: CLAHE (Contrast Limited Adaptive Histogram Equalization)
        clahe = cv2.createCLAHE(clipLimit=2.0, tileGridSize=(8,8))
        cl_img = clahe.apply(gray)
        
        # Pipeline 3: Inverted
        inverted = cv2.bitwise_not(blur)
        
        # Pipeline 4: Adaptive Threshold
        thresh = cv2.adaptiveThreshold(blur, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C, cv2.THRESH_BINARY, 11, 2)
        
        variants = [blur, cl_img, inverted, thresh]
        best_text = ""
        best_conf = 0.0
        
        for variant in variants:
            text, conf = self._run_ocr_on_variant(variant)
            if text:
                is_valid = self.validate_indian_plate(text)
                # Boost confidence if it perfectly matches Indian plate format
                effective_conf = conf + 0.2 if is_valid else conf
                
                if effective_conf > best_conf:
                    best_conf = conf # store actual conf
                    best_text = text
                    
        return best_text, best_conf

    def process_vehicle(self, frame: np.ndarray, vehicle_box: list) -> Dict:
        """
        Full pipeline: extract plate -> recognize
        """
        try:
            plate_crop, det_conf = self.extract_plate(frame, vehicle_box)
            if plate_crop is not None:
                text, ocr_conf = self.recognize_plate(plate_crop)
                status = "READ" if text else "NOT_READABLE"
                min_ocr = float(os.environ.get("ANPR_MIN_OCR_CONFIDENCE", "0.50"))
                if text and ocr_conf < min_ocr:
                    status = "LOW_CONFIDENCE"
                    
                return {
                    "plate_number": text,
                    "plate_status": status,
                    "detection_confidence": round(det_conf, 3),
                    "ocr_confidence": round(ocr_conf, 3)
                }
        except Exception as e:
            logger.error(f"ANPR error processing vehicle: {e}")
            
        return {
            "plate_number": "",
            "plate_status": "UNKNOWN",
            "detection_confidence": 0.0,
            "ocr_confidence": 0.0
        }
