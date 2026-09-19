package com.velosight.dto;

import java.time.LocalDateTime;

public class SignalRecommendationDto {
    private Long id;
    private String intersectionName;
    private Double nsScore;
    private Double ewScore;
    private Integer nsGreenDuration;
    private Integer ewGreenDuration;
    private String reason;
    private Double confidence;
    private LocalDateTime createdAt;

    public SignalRecommendationDto() {}

    public SignalRecommendationDto(Long id, String intersectionName, Double nsScore, Double ewScore, Integer nsGreenDuration, Integer ewGreenDuration, String reason, Double confidence, LocalDateTime createdAt) {
        this.id = id;
        this.intersectionName = intersectionName;
        this.nsScore = nsScore;
        this.ewScore = ewScore;
        this.nsGreenDuration = nsGreenDuration;
        this.ewGreenDuration = ewGreenDuration;
        this.reason = reason;
        this.confidence = confidence;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getIntersectionName() { return intersectionName; }
    public void setIntersectionName(String intersectionName) { this.intersectionName = intersectionName; }

    public Double getNsScore() { return nsScore; }
    public void setNsScore(Double nsScore) { this.nsScore = nsScore; }

    public Double getEwScore() { return ewScore; }
    public void setEwScore(Double ewScore) { this.ewScore = ewScore; }

    public Integer getNsGreenDuration() { return nsGreenDuration; }
    public void setNsGreenDuration(Integer nsGreenDuration) { this.nsGreenDuration = nsGreenDuration; }

    public Integer getEwGreenDuration() { return ewGreenDuration; }
    public void setEwGreenDuration(Integer ewGreenDuration) { this.ewGreenDuration = ewGreenDuration; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public Double getConfidence() { return confidence; }
    public void setConfidence(Double confidence) { this.confidence = confidence; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static SignalRecommendationDtoBuilder builder() {
        return new SignalRecommendationDtoBuilder();
    }

    public static class SignalRecommendationDtoBuilder {
        private Long id;
        private String intersectionName;
        private Double nsScore;
        private Double ewScore;
        private Integer nsGreenDuration;
        private Integer ewGreenDuration;
        private String reason;
        private Double confidence;
        private LocalDateTime createdAt;

        public SignalRecommendationDtoBuilder id(Long id) { this.id = id; return this; }
        public SignalRecommendationDtoBuilder intersectionName(String intersectionName) { this.intersectionName = intersectionName; return this; }
        public SignalRecommendationDtoBuilder nsScore(Double nsScore) { this.nsScore = nsScore; return this; }
        public SignalRecommendationDtoBuilder ewScore(Double ewScore) { this.ewScore = ewScore; return this; }
        public SignalRecommendationDtoBuilder nsGreenDuration(Integer nsGreenDuration) { this.nsGreenDuration = nsGreenDuration; return this; }
        public SignalRecommendationDtoBuilder ewGreenDuration(Integer ewGreenDuration) { this.ewGreenDuration = ewGreenDuration; return this; }
        public SignalRecommendationDtoBuilder reason(String reason) { this.reason = reason; return this; }
        public SignalRecommendationDtoBuilder confidence(Double confidence) { this.confidence = confidence; return this; }
        public SignalRecommendationDtoBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public SignalRecommendationDto build() {
            return new SignalRecommendationDto(id, intersectionName, nsScore, ewScore, nsGreenDuration, ewGreenDuration, reason, confidence, createdAt);
        }
    }
}
