package com.velosight.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "analysis_sessions")
public class AnalysisSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String analysisIdStr;

    private String videoFilename;
    private String originalFilePath;
    private String processedFilePath;

    @Column(nullable = false)
    private String status;

    private Integer totalVehicles;
    private Integer carCount;
    private Integer motorcycleCount;
    private Integer busCount;
    private Integer truckCount;
    private Integer bicycleCount;

    private String densityLevel;
    private Double densityScore;

    private String congestionLevel;
    private Double congestionScore;

    private Double averageSpeedKmh;
    private Integer peakVehicleCount;
    private Double processingDurationSec;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "camera_id")
    private Camera camera;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "intersection_id")
    private Intersection intersection;

    private LocalDateTime createdAt;

    public AnalysisSession() {}

    public AnalysisSession(Long id, String analysisIdStr, String videoFilename, String originalFilePath, String processedFilePath, String status, Integer totalVehicles, Integer carCount, Integer motorcycleCount, Integer busCount, Integer truckCount, Integer bicycleCount, String densityLevel, Double densityScore, String congestionLevel, Double congestionScore, Double averageSpeedKmh, Integer peakVehicleCount, Double processingDurationSec, Camera camera, Intersection intersection, LocalDateTime createdAt) {
        this.id = id;
        this.analysisIdStr = analysisIdStr;
        this.videoFilename = videoFilename;
        this.originalFilePath = originalFilePath;
        this.processedFilePath = processedFilePath;
        this.status = status;
        this.totalVehicles = totalVehicles;
        this.carCount = carCount;
        this.motorcycleCount = motorcycleCount;
        this.busCount = busCount;
        this.truckCount = truckCount;
        this.bicycleCount = bicycleCount;
        this.densityLevel = densityLevel;
        this.densityScore = densityScore;
        this.congestionLevel = congestionLevel;
        this.congestionScore = congestionScore;
        this.averageSpeedKmh = averageSpeedKmh;
        this.peakVehicleCount = peakVehicleCount;
        this.processingDurationSec = processingDurationSec;
        this.camera = camera;
        this.intersection = intersection;
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAnalysisIdStr() { return analysisIdStr; }
    public void setAnalysisIdStr(String analysisIdStr) { this.analysisIdStr = analysisIdStr; }

    public String getVideoFilename() { return videoFilename; }
    public void setVideoFilename(String videoFilename) { this.videoFilename = videoFilename; }

    public String getOriginalFilePath() { return originalFilePath; }
    public void setOriginalFilePath(String originalFilePath) { this.originalFilePath = originalFilePath; }

    public String getProcessedFilePath() { return processedFilePath; }
    public void setProcessedFilePath(String processedFilePath) { this.processedFilePath = processedFilePath; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getTotalVehicles() { return totalVehicles; }
    public void setTotalVehicles(Integer totalVehicles) { this.totalVehicles = totalVehicles; }

    public Integer getCarCount() { return carCount; }
    public void setCarCount(Integer carCount) { this.carCount = carCount; }

    public Integer getMotorcycleCount() { return motorcycleCount; }
    public void setMotorcycleCount(Integer motorcycleCount) { this.motorcycleCount = motorcycleCount; }

    public Integer getBusCount() { return busCount; }
    public void setBusCount(Integer busCount) { this.busCount = busCount; }

    public Integer getTruckCount() { return truckCount; }
    public void setTruckCount(Integer truckCount) { this.truckCount = truckCount; }

    public Integer getBicycleCount() { return bicycleCount; }
    public void setBicycleCount(Integer bicycleCount) { this.bicycleCount = bicycleCount; }

    public String getDensityLevel() { return densityLevel; }
    public void setDensityLevel(String densityLevel) { this.densityLevel = densityLevel; }

    public Double getDensityScore() { return densityScore; }
    public void setDensityScore(Double densityScore) { this.densityScore = densityScore; }

    public String getCongestionLevel() { return congestionLevel; }
    public void setCongestionLevel(String congestionLevel) { this.congestionLevel = congestionLevel; }

    public Double getCongestionScore() { return congestionScore; }
    public void setCongestionScore(Double congestionScore) { this.congestionScore = congestionScore; }

    public Double getAverageSpeedKmh() { return averageSpeedKmh; }
    public void setAverageSpeedKmh(Double averageSpeedKmh) { this.averageSpeedKmh = averageSpeedKmh; }

    public Integer getPeakVehicleCount() { return peakVehicleCount; }
    public void setPeakVehicleCount(Integer peakVehicleCount) { this.peakVehicleCount = peakVehicleCount; }

    public Double getProcessingDurationSec() { return processingDurationSec; }
    public void setProcessingDurationSec(Double processingDurationSec) { this.processingDurationSec = processingDurationSec; }

    public Camera getCamera() { return camera; }
    public void setCamera(Camera camera) { this.camera = camera; }

    public Intersection getIntersection() { return intersection; }
    public void setIntersection(Intersection intersection) { this.intersection = intersection; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static AnalysisSessionBuilder builder() {
        return new AnalysisSessionBuilder();
    }

    public static class AnalysisSessionBuilder {
        private Long id;
        private String analysisIdStr;
        private String videoFilename;
        private String originalFilePath;
        private String processedFilePath;
        private String status;
        private Integer totalVehicles;
        private Integer carCount;
        private Integer motorcycleCount;
        private Integer busCount;
        private Integer truckCount;
        private Integer bicycleCount;
        private String densityLevel;
        private Double densityScore;
        private String congestionLevel;
        private Double congestionScore;
        private Double averageSpeedKmh;
        private Integer peakVehicleCount;
        private Double processingDurationSec;
        private Camera camera;
        private Intersection intersection;
        private LocalDateTime createdAt;

        public AnalysisSessionBuilder id(Long id) { this.id = id; return this; }
        public AnalysisSessionBuilder analysisIdStr(String analysisIdStr) { this.analysisIdStr = analysisIdStr; return this; }
        public AnalysisSessionBuilder videoFilename(String videoFilename) { this.videoFilename = videoFilename; return this; }
        public AnalysisSessionBuilder originalFilePath(String originalFilePath) { this.originalFilePath = originalFilePath; return this; }
        public AnalysisSessionBuilder processedFilePath(String processedFilePath) { this.processedFilePath = processedFilePath; return this; }
        public AnalysisSessionBuilder status(String status) { this.status = status; return this; }
        public AnalysisSessionBuilder totalVehicles(Integer totalVehicles) { this.totalVehicles = totalVehicles; return this; }
        public AnalysisSessionBuilder carCount(Integer carCount) { this.carCount = carCount; return this; }
        public AnalysisSessionBuilder motorcycleCount(Integer motorcycleCount) { this.motorcycleCount = motorcycleCount; return this; }
        public AnalysisSessionBuilder busCount(Integer busCount) { this.busCount = busCount; return this; }
        public AnalysisSessionBuilder truckCount(Integer truckCount) { this.truckCount = truckCount; return this; }
        public AnalysisSessionBuilder bicycleCount(Integer bicycleCount) { this.bicycleCount = bicycleCount; return this; }
        public AnalysisSessionBuilder densityLevel(String densityLevel) { this.densityLevel = densityLevel; return this; }
        public AnalysisSessionBuilder densityScore(Double densityScore) { this.densityScore = densityScore; return this; }
        public AnalysisSessionBuilder congestionLevel(String congestionLevel) { this.congestionLevel = congestionLevel; return this; }
        public AnalysisSessionBuilder congestionScore(Double congestionScore) { this.congestionScore = congestionScore; return this; }
        public AnalysisSessionBuilder averageSpeedKmh(Double averageSpeedKmh) { this.averageSpeedKmh = averageSpeedKmh; return this; }
        public AnalysisSessionBuilder peakVehicleCount(Integer peakVehicleCount) { this.peakVehicleCount = peakVehicleCount; return this; }
        public AnalysisSessionBuilder processingDurationSec(Double processingDurationSec) { this.processingDurationSec = processingDurationSec; return this; }
        public AnalysisSessionBuilder camera(Camera camera) { this.camera = camera; return this; }
        public AnalysisSessionBuilder intersection(Intersection intersection) { this.intersection = intersection; return this; }
        public AnalysisSessionBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public AnalysisSession build() {
            return new AnalysisSession(id, analysisIdStr, videoFilename, originalFilePath, processedFilePath, status, totalVehicles, carCount, motorcycleCount, busCount, truckCount, bicycleCount, densityLevel, densityScore, congestionLevel, congestionScore, averageSpeedKmh, peakVehicleCount, processingDurationSec, camera, intersection, createdAt);
        }
    }
}
