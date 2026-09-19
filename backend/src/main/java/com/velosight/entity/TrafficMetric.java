package com.velosight.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "traffic_metrics")
public class TrafficMetric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "analysis_session_id")
    private AnalysisSession analysisSession;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "intersection_id")
    private Intersection intersection;

    private LocalDateTime timestamp;
    private Integer vehicleCount;
    private Double densityScore;
    private Double congestionScore;
    private Double averageSpeedKmh;

    public TrafficMetric() {}

    public TrafficMetric(Long id, AnalysisSession analysisSession, Intersection intersection, LocalDateTime timestamp, Integer vehicleCount, Double densityScore, Double congestionScore, Double averageSpeedKmh) {
        this.id = id;
        this.analysisSession = analysisSession;
        this.intersection = intersection;
        this.timestamp = timestamp;
        this.vehicleCount = vehicleCount;
        this.densityScore = densityScore;
        this.congestionScore = congestionScore;
        this.averageSpeedKmh = averageSpeedKmh;
    }

    @PrePersist
    protected void onCreate() {
        if (this.timestamp == null) {
            this.timestamp = LocalDateTime.now();
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public AnalysisSession getAnalysisSession() { return analysisSession; }
    public void setAnalysisSession(AnalysisSession analysisSession) { this.analysisSession = analysisSession; }

    public Intersection getIntersection() { return intersection; }
    public void setIntersection(Intersection intersection) { this.intersection = intersection; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public Integer getVehicleCount() { return vehicleCount; }
    public void setVehicleCount(Integer vehicleCount) { this.vehicleCount = vehicleCount; }

    public Double getDensityScore() { return densityScore; }
    public void setDensityScore(Double densityScore) { this.densityScore = densityScore; }

    public Double getCongestionScore() { return congestionScore; }
    public void setCongestionScore(Double congestionScore) { this.congestionScore = congestionScore; }

    public Double getAverageSpeedKmh() { return averageSpeedKmh; }
    public void setAverageSpeedKmh(Double averageSpeedKmh) { this.averageSpeedKmh = averageSpeedKmh; }

    public static TrafficMetricBuilder builder() {
        return new TrafficMetricBuilder();
    }

    public static class TrafficMetricBuilder {
        private Long id;
        private AnalysisSession analysisSession;
        private Intersection intersection;
        private LocalDateTime timestamp;
        private Integer vehicleCount;
        private Double densityScore;
        private Double congestionScore;
        private Double averageSpeedKmh;

        public TrafficMetricBuilder id(Long id) { this.id = id; return this; }
        public TrafficMetricBuilder analysisSession(AnalysisSession analysisSession) { this.analysisSession = analysisSession; return this; }
        public TrafficMetricBuilder intersection(Intersection intersection) { this.intersection = intersection; return this; }
        public TrafficMetricBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }
        public TrafficMetricBuilder vehicleCount(Integer vehicleCount) { this.vehicleCount = vehicleCount; return this; }
        public TrafficMetricBuilder densityScore(Double densityScore) { this.densityScore = densityScore; return this; }
        public TrafficMetricBuilder congestionScore(Double congestionScore) { this.congestionScore = congestionScore; return this; }
        public TrafficMetricBuilder averageSpeedKmh(Double averageSpeedKmh) { this.averageSpeedKmh = averageSpeedKmh; return this; }

        public TrafficMetric build() {
            return new TrafficMetric(id, analysisSession, intersection, timestamp, vehicleCount, densityScore, congestionScore, averageSpeedKmh);
        }
    }
}
