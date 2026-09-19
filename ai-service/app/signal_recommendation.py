from typing import Dict, Any

class SignalRecommendationEngine:
    def recommend(self, ns_traffic_score: float, ew_traffic_score: float, base_cycle_time_sec: int = 90) -> Dict[str, Any]:
        """
        Calculates recommended green duration for North-South vs East-West corridors.
        This is for decision support only.
        """
        total_score = max(ns_traffic_score + ew_traffic_score, 1.0)
        
        ns_ratio = ns_traffic_score / total_score
        ew_ratio = ew_traffic_score / total_score

        # Minimum green phase is 15 seconds
        available_green_time = max(base_cycle_time_sec - 10, 30) # 10s for yellow/all-red transitions
        
        ns_green = max(15, min(int(round(available_green_time * ns_ratio)), available_green_time - 15))
        ew_green = max(15, available_green_time - ns_green)

        confidence = round(min(0.70 + (abs(ns_traffic_score - ew_traffic_score) / 200.0), 0.98), 2)

        if ns_traffic_score > ew_traffic_score + 15:
            reason = f"North-South traffic corridor has significantly higher volume ({round(ns_traffic_score)}%) compared to East-West ({round(ew_traffic_score)}%). Extending N-S green phase."
        elif ew_traffic_score > ns_traffic_score + 15:
            reason = f"East-West traffic corridor exhibits higher density ({round(ew_traffic_score)}%) than North-South ({round(ns_traffic_score)}%). Extending E-W green phase."
        else:
            reason = f"Traffic distribution between North-South ({round(ns_traffic_score)}%) and East-West ({round(ew_traffic_score)}%) is balanced. Equal phase distribution recommended."

        return {
            "nsScore": round(ns_traffic_score, 1),
            "ewScore": round(ew_traffic_score, 1),
            "nsGreenDuration": ns_green,
            "ewGreenDuration": ew_green,
            "reason": reason,
            "confidence": confidence,
            "notice": "AI Recommendation — Decision Support Only"
        }
