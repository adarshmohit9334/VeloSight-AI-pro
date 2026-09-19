package com.velosight.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "signal_recommendations")
public class SignalRecommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "intersection_id")
    private Intersection intersection;

    private Double nsTrafficScore;
    private Double ewTrafficScore;
    private Integer nsGreenDurationSec;
    private Integer ewGreenDurationSec;

    @Column(length = 1000)
    private String reason;

    private Double confidence;
    private LocalDateTime createdAt;

    public SignalRecommendation() {}

    public SignalRecommendation(Long id, Intersection intersection, Double nsTrafficScore, Double ewTrafficScore, Integer nsGreenDurationSec, Integer ewGreenDurationSec, String reason, Double confidence, LocalDateTime createdAt) {
        this.id = id;
        this.intersection = intersection;
        this.nsTrafficScore = nsTrafficScore;
        this.ewTrafficScore = ewTrafficScore;
        this.nsGreenDurationSec = nsGreenDurationSec;
        this.ewGreenDurationSec = ewGreenDurationSec;
        this.reason = reason;
        this.confidence = confidence;
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Intersection getIntersection() { return intersection; }
    public void setIntersection(Intersection intersection) { this.intersection = intersection; }

    public Double getNsTrafficScore() { return nsTrafficScore; }
    public void setNsTrafficScore(Double nsTrafficScore) { this.nsTrafficScore = nsTrafficScore; }

    public Double getEwTrafficScore() { return ewTrafficScore; }
    public void setEwTrafficScore(Double ewTrafficScore) { this.ewTrafficScore = ewTrafficScore; }

    public Integer getNsGreenDurationSec() { return nsGreenDurationSec; }
    public void setNsGreenDurationSec(Integer nsGreenDurationSec) { this.nsGreenDurationSec = nsGreenDurationSec; }

    public Integer getEwGreenDurationSec() { return ewGreenDurationSec; }
    public void setEwGreenDurationSec(Integer ewGreenDurationSec) { this.ewGreenDurationSec = ewGreenDurationSec; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public Double getConfidence() { return confidence; }
    public void setConfidence(Double confidence) { this.confidence = confidence; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static SignalRecommendationBuilder builder() {
        return new SignalRecommendationBuilder();
    }

    public static class SignalRecommendationBuilder {
        private Long id;
        private Intersection intersection;
        private Double nsTrafficScore;
        private Double ewTrafficScore;
        private Integer nsGreenDurationSec;
        private Integer ewGreenDurationSec;
        private String reason;
        private Double confidence;
        private LocalDateTime createdAt;

        public SignalRecommendationBuilder id(Long id) { this.id = id; return this; }
        public SignalRecommendationBuilder intersection(Intersection intersection) { this.intersection = intersection; return this; }
        public SignalRecommendationBuilder nsTrafficScore(Double nsTrafficScore) { this.nsTrafficScore = nsTrafficScore; return this; }
        public SignalRecommendationBuilder ewTrafficScore(Double ewTrafficScore) { this.ewTrafficScore = ewTrafficScore; return this; }
        public SignalRecommendationBuilder nsGreenDurationSec(Integer nsGreenDurationSec) { this.nsGreenDurationSec = nsGreenDurationSec; return this; }
        public SignalRecommendationBuilder ewGreenDurationSec(Integer ewGreenDurationSec) { this.ewGreenDurationSec = ewGreenDurationSec; return this; }
        public SignalRecommendationBuilder reason(String reason) { this.reason = reason; return this; }
        public SignalRecommendationBuilder confidence(Double confidence) { this.confidence = confidence; return this; }
        public SignalRecommendationBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public SignalRecommendation build() {
            return new SignalRecommendation(id, intersection, nsTrafficScore, ewTrafficScore, nsGreenDurationSec, ewGreenDurationSec, reason, confidence, createdAt);
        }
    }
}
