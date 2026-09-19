import os
import cv2
import numpy as np

def generate_sample_traffic_video(output_path: str = "uploads/sample_traffic.mp4", duration_sec: int = 10, fps: int = 25):
    """
    Generates a realistic synthetic traffic video with moving cars, buses, and motorcycles crossing a virtual line.
    """
    os.makedirs(os.path.dirname(output_path), exist_ok=True)
    width, height = 640, 480
    total_frames = duration_sec * fps

    fourcc = cv2.VideoWriter_fourcc(*'mp4v')
    out = cv2.VideoWriter(output_path, fourcc, fps, (width, height))

    # Vehicles configuration: [x, y, speed_y, width, height, color, class_name]
    vehicles = [
        [150, 50, 4, 60, 100, (220, 150, 50), "car"],
        [240, -100, 6, 40, 70, (50, 200, 100), "car"],
        [360, -250, 3, 90, 160, (50, 100, 220), "bus"],
        [460, 20, 7, 30, 50, (200, 50, 200), "motorcycle"],
        [200, -400, 5, 70, 120, (100, 220, 220), "truck"],
        [300, -550, 6, 50, 90, (180, 180, 50), "car"]
    ]

    for frame_idx in range(total_frames):
        # Create dark road background
        frame = np.full((height, width, 3), (40, 44, 52), dtype=np.uint8)

        # Draw road lanes
        cv2.rectangle(frame, (100, 0), (540, height), (60, 64, 72), -1)
        # Dash lane dividers
        for y in range(0, height, 40):
            cv2.line(frame, (240, y), (240, y + 20), (255, 255, 255), 2)
            cv2.line(frame, (380, y), (380, y + 20), (255, 255, 255), 2)

        # Draw virtual counting line at y=300
        line_y = 300
        cv2.line(frame, (100, line_y), (540, line_y), (0, 0, 255), 2)

        # Render moving vehicles
        for v in vehicles:
            v[1] += v[2] # Move along y-axis
            if v[1] > height + 100:
                v[1] = -120 # Reset loop

            vx, vy, _, vw, vh, color, label = v
            cv2.rectangle(frame, (int(vx), int(vy)), (int(vx + vw), int(vy + vh)), color, -1)
            cv2.rectangle(frame, (int(vx), int(vy)), (int(vx + vw), int(vy + vh)), (255, 255, 255), 2)
            cv2.putText(frame, label, (int(vx + 5), int(vy + 20)), cv2.FONT_HERSHEY_SIMPLEX, 0.45, (255, 255, 255), 1)

        out.write(frame)

    out.release()
    print(f"Sample traffic video generated successfully at: {output_path}")

if __name__ == "__main__":
    generate_sample_traffic_video()
