package com.velosight.dto;

import java.time.LocalDateTime;
import java.util.Map;

public class AnalysisResultDto {
    private Long id;
    private String analysisId;
    private String videoFilename;
    private String status;
    private Integer totalVehicles;

    private Map<String, Integer> vehicleCounts;
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

    private String cameraName;
    private String intersectionName;
    private String processedVideoUrl;

    private SignalRecommendationDto signalRecommendation;
    private LocalDateTime createdAt;

    public AnalysisResultDto() {}

    public AnalysisResultDto(Long id, String analysisId, String videoFilename, String status, Integer totalVehicles, Map<String, Integer> vehicleCounts, Integer carCount, Integer motorcycleCount, Integer busCount, Integer truckCount, Integer bicycleCount, String densityLevel, Double densityScore, String congestionLevel, Double congestionScore, Double averageSpeedKmh, Integer peakVehicleCount, Double processingDurationSec, String cameraName, String intersectionName, String processedVideoUrl, SignalRecommendationDto signalRecommendation, LocalDateTime createdAt) {
        this.id = id;
        this.analysisId = analysisId;
        this.videoFilename = videoFilename;
        this.status = status;
        this.totalVehicles = totalVehicles;
        this.vehicleCounts = vehicleCounts;
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
        this.cameraName = cameraName;
        this.intersectionName = intersectionName;
        this.processedVideoUrl = processedVideoUrl;
        this.signalRecommendation = signalRecommendation;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAnalysisId() { return analysisId; }
    public void setAnalysisId(String analysisId) { this.analysisId = analysisId; }

    public String getVideoFilename() { return videoFilename; }
    public void setVideoFilename(String videoFilename) { this.videoFilename = videoFilename; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getTotalVehicles() { return totalVehicles; }
    public void setTotalVehicles(Integer totalVehicles) { this.totalVehicles = totalVehicles; }

    public Map<String, Integer> getVehicleCounts() { return vehicleCounts; }
    public void setVehicleCounts(Map<String, Integer> vehicleCounts) { this.vehicleCounts = vehicleCounts; }

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

    public String getCameraName() { return cameraName; }
    public void setCameraName(String cameraName) { this.cameraName = cameraName; }

    public String getIntersectionName() { return intersectionName; }
    public void setIntersectionName(String intersectionName) { this.intersectionName = intersectionName; }

    public String getProcessedVideoUrl() { return processedVideoUrl; }
    public void setProcessedVideoUrl(String processedVideoUrl) { this.processedVideoUrl = processedVideoUrl; }

    public SignalRecommendationDto getSignalRecommendation() { return signalRecommendation; }
    public void setSignalRecommendation(SignalRecommendationDto signalRecommendation) { this.signalRecommendation = signalRecommendation; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static AnalysisResultDtoBuilder builder() {
        return new AnalysisResultDtoBuilder();
    }

    public static class AnalysisResultDtoBuilder {
        private Long id;
        private String analysisId;
        private String videoFilename;
        private String status;
        private Integer totalVehicles;
        private Map<String, Integer> vehicleCounts;
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
        private String cameraName;
        private String intersectionName;
        private String processedVideoUrl;
        private SignalRecommendationDto signalRecommendation;
        private LocalDateTime createdAt;

        public AnalysisResultDtoBuilder id(Long id) { this.id = id; return this; }
        public AnalysisResultDtoBuilder analysisId(String analysisId) { this.analysisId = analysisId; return this; }
        public AnalysisResultDtoBuilder videoFilename(String videoFilename) { this.videoFilename = videoFilename; return this; }
        public AnalysisResultDtoBuilder status(String status) { this.status = status; return this; }
        public AnalysisResultDtoBuilder totalVehicles(Integer totalVehicles) { this.totalVehicles = totalVehicles; return this; }
        public AnalysisResultDtoBuilder vehicleCounts(Map<String, Integer> vehicleCounts) { this.vehicleCounts = vehicleCounts; return this; }
        public AnalysisResultDtoBuilder carCount(Integer carCount) { this.carCount = carCount; return this; }
        public AnalysisResultDtoBuilder motorcycleCount(Integer motorcycleCount) { this.motorcycleCount = motorcycleCount; return this; }
        public AnalysisResultDtoBuilder busCount(Integer busCount) { this.busCount = busCount; return this; }
        public AnalysisResultDtoBuilder truckCount(Integer truckCount) { this.truckCount = truckCount; return this; }
        public AnalysisResultDtoBuilder bicycleCount(Integer bicycleCount) { this.bicycleCount = bicycleCount; return this; }
        public AnalysisResultDtoBuilder densityLevel(String densityLevel) { this.densityLevel = densityLevel; return this; }
        public AnalysisResultDtoBuilder densityScore(Double densityScore) { this.densityScore = densityScore; return this; }
        public AnalysisResultDtoBuilder congestionLevel(String congestionLevel) { this.congestionLevel = congestionLevel; return this; }
        public AnalysisResultDtoBuilder congestionScore(Double congestionScore) { this.congestionScore = congestionScore; return this; }
        public AnalysisResultDtoBuilder averageSpeedKmh(Double averageSpeedKmh) { this.averageSpeedKmh = averageSpeedKmh; return this; }
        public AnalysisResultDtoBuilder peakVehicleCount(Integer peakVehicleCount) { this.peakVehicleCount = peakVehicleCount; return this; }
        public AnalysisResultDtoBuilder processingDurationSec(Double processingDurationSec) { this.processingDurationSec = processingDurationSec; return this; }
        public AnalysisResultDtoBuilder cameraName(String cameraName) { this.cameraName = cameraName; return this; }
        public AnalysisResultDtoBuilder intersectionName(String intersectionName) { this.intersectionName = intersectionName; return this; }
        public AnalysisResultDtoBuilder processedVideoUrl(String processedVideoUrl) { this.processedVideoUrl = processedVideoUrl; return this; }
        public AnalysisResultDtoBuilder signalRecommendation(SignalRecommendationDto signalRecommendation) { this.signalRecommendation = signalRecommendation; return this; }
        public AnalysisResultDtoBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public AnalysisResultDto build() {
            return new AnalysisResultDto(id, analysisId, videoFilename, status, totalVehicles, vehicleCounts, carCount, motorcycleCount, busCount, truckCount, bicycleCount, densityLevel, densityScore, congestionLevel, congestionScore, averageSpeedKmh, peakVehicleCount, processingDurationSec, cameraName, intersectionName, processedVideoUrl, signalRecommendation, createdAt);
        }
    }
}
