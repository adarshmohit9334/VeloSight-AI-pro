import os
import cv2
import json
import numpy as np

# A script to quickly test ANPR functionality standalone
from app.anpr import ANPREngine

engine = ANPREngine()
# Create a dummy image mimicking a plate for a quick sanity check of the OCR
img = np.zeros((100, 300, 3), dtype=np.uint8)
img.fill(255)
cv2.putText(img, "MH16C00555", (20, 60), cv2.FONT_HERSHEY_SIMPLEX, 1.5, (0, 0, 0), 4)

text, conf = engine.recognize_plate(img)
print(f"Recognized: {text}, Conf: {conf}")
