import cv2
import sys
from app.anpr import ANPREngine

engine = ANPREngine(enabled=True)

cap = cv2.VideoCapture("../backend/uploads/VS-E625FF3D.mp4")
frame_count = 0
found_plates = []
while cap.isOpened():
    ret, frame = cap.read()
    if not ret:
        break
    
    # Try full frame plate extraction as a baseline
    h, w = frame.shape[:2]
    # simulate vehicle box as full frame
    plate_crop, det_conf = engine.extract_plate(frame, [0, 0, w, h])
    if plate_crop is not None:
        text, ocr_conf = engine.recognize_plate(plate_crop)
        if text:
            found_plates.append((frame_count, text, ocr_conf, det_conf))
            
    frame_count += 1

print(f"Total frames: {frame_count}")
print("Found plates:")
for p in found_plates:
    print(p)

cap.release()
