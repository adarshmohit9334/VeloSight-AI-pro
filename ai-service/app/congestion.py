from typing import Dict, Any

class CongestionEngine:
    def calculate(self, density_score: float, avg_speed_kmh: float = 35.0, queue_length: int = 0) -> Dict[str, Any]:
        """
        Calculates congestion level, score, and metric-derived explanation.
        """
        # Speed penalty: expected free flow speed is ~50 km/h
        speed_factor = max(0.0, (50.0 - min(avg_speed_kmh, 50.0)) / 50.0) * 40.0
        
        # Density factor contribution: 50%
        density_factor = (density_score / 100.0) * 50.0
        
        # Queue length factor: 10%
        queue_factor = min(queue_length / 15.0, 1.0) * 10.0

        raw_score = density_factor + speed_factor + queue_factor
        score = round(min(max(raw_score, 0.0), 100.0), 1)

        if score < 30.0:
            level = "LOW"
            explanation = "Traffic flow is free-flowing with optimal vehicle speeds and minimal queue accumulation."
        elif score < 60.0:
            level = "MODERATE"
            explanation = "Moderate vehicle density detected. Flow speeds are slightly reduced but moving steadily."
        elif score < 82.0:
            level = "HIGH"
            explanation = "High vehicle concentration and reduced average movement speed indicate heavy traffic build-up."
        else:
            level = "SEVERE"
            explanation = "Critical vehicle concentration combined with near-stationary queue movement indicates severe congestion."

        return {
            "level": level,
            "score": score,
            "explanation": explanation,
            "avg_speed_kmh": round(avg_speed_kmh, 1)
        }
