package com.velosight.service;

import com.velosight.dto.SignalRecommendationDto;
import com.velosight.entity.SignalRecommendation;
import com.velosight.repository.SignalRecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendationService {

    @Autowired
    private SignalRecommendationRepository recommendationRepository;

    public List<SignalRecommendationDto> getAllRecommendations() {
        return recommendationRepository.findAll().stream().map(this::convertToDto).toList();
    }

    private SignalRecommendationDto convertToDto(SignalRecommendation r) {
        return SignalRecommendationDto.builder()
                .id(r.getId())
                .intersectionName(r.getIntersection() != null ? r.getIntersection().getName() : "Central Avenue Crossing")
                .nsScore(r.getNsTrafficScore())
                .ewScore(r.getEwTrafficScore())
                .nsGreenDuration(r.getNsGreenDurationSec())
                .ewGreenDuration(r.getEwGreenDurationSec())
                .reason(r.getReason())
                .confidence(r.getConfidence())
                .createdAt(r.getCreatedAt())
                .build();
    }
}
