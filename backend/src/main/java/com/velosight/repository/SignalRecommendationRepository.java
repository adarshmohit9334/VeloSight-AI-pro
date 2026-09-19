package com.velosight.repository;

import com.velosight.entity.SignalRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SignalRecommendationRepository extends JpaRepository<SignalRecommendation, Long> {
    List<SignalRecommendation> findTop5ByOrderByCreatedAtDesc();
    Optional<SignalRecommendation> findTopByIntersectionIdOrderByCreatedAtDesc(Long intersectionId);
}
