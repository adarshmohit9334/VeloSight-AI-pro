import time
import cv2
from analyzer import VideoAnalyzer

analyzer = VideoAnalyzer(anpr_enabled=True)

start = time.time()
analyzer.process_video("../uploads/sample_traffic.mp4", "test_out.mp4")
end = time.time()
print(f"Total time: {end - start:.2f}s")
