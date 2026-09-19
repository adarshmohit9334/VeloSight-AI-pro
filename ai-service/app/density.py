from typing import Dict, Any

class DensityEngine:
    def __init__(self, low_threshold: float = 25.0, moderate_threshold: float = 50.0, high_threshold: float = 75.0):
        self.low_threshold = low_threshold
        self.moderate_threshold = moderate_threshold
        self.high_threshold = high_threshold

    def calculate(self, vehicle_count: int, roi_occupancy_ratio: float = 0.0, max_capacity: int = 40) -> Dict[str, Any]:
        """
        Calculate traffic density score (0 - 100) and level based on count and occupancy.
        """
        # Capacity ratio (0 - 1)
        count_ratio = min(vehicle_count / max(max_capacity, 1), 1.0)
        
        # Weighted combination: 60% vehicle count ratio, 40% ROI occupancy ratio
        combined_score = (count_ratio * 60.0) + (min(roi_occupancy_ratio, 1.0) * 40.0)
        density_score = round(min(max(combined_score, 0.0), 100.0), 1)

        if density_score <= self.low_threshold:
            level = "LOW"
        elif density_score <= self.moderate_threshold:
            level = "MODERATE"
        elif density_score <= self.high_threshold:
            level = "HIGH"
        else:
            level = "CRITICAL"

        return {
            "level": level,
            "score": density_score,
            "vehicle_count": vehicle_count,
            "occupancy_ratio": round(roi_occupancy_ratio, 3)
        }
