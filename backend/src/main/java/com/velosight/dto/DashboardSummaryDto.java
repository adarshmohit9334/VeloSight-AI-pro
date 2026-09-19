package com.velosight.dto;

import java.util.List;
import java.util.Map;

public class DashboardSummaryDto {
    private Long totalVehicles;
    private String currentTrafficDensity;
    private String congestionLevel;
    private Long activeAlertsCount;

    private List<Map<String, Object>> volumeTrends;
    private Map<String, Integer> vehicleDistribution;
    private List<AlertDto> recentAlerts;
    private List<SignalRecommendationDto> recentRecommendations;
    private List<IntersectionDto> activeIntersections;

    public DashboardSummaryDto() {}

    public DashboardSummaryDto(Long totalVehicles, String currentTrafficDensity, String congestionLevel, Long activeAlertsCount, List<Map<String, Object>> volumeTrends, Map<String, Integer> vehicleDistribution, List<AlertDto> recentAlerts, List<SignalRecommendationDto> recentRecommendations, List<IntersectionDto> activeIntersections) {
        this.totalVehicles = totalVehicles;
        this.currentTrafficDensity = currentTrafficDensity;
        this.congestionLevel = congestionLevel;
        this.activeAlertsCount = activeAlertsCount;
        this.volumeTrends = volumeTrends;
        this.vehicleDistribution = vehicleDistribution;
        this.recentAlerts = recentAlerts;
        this.recentRecommendations = recentRecommendations;
        this.activeIntersections = activeIntersections;
    }

    public Long getTotalVehicles() { return totalVehicles; }
    public void setTotalVehicles(Long totalVehicles) { this.totalVehicles = totalVehicles; }

    public String getCurrentTrafficDensity() { return currentTrafficDensity; }
    public void setCurrentTrafficDensity(String currentTrafficDensity) { this.currentTrafficDensity = currentTrafficDensity; }

    public String getCongestionLevel() { return congestionLevel; }
    public void setCongestionLevel(String congestionLevel) { this.congestionLevel = congestionLevel; }

    public Long getActiveAlertsCount() { return activeAlertsCount; }
    public void setActiveAlertsCount(Long activeAlertsCount) { this.activeAlertsCount = activeAlertsCount; }

    public List<Map<String, Object>> getVolumeTrends() { return volumeTrends; }
    public void setVolumeTrends(List<Map<String, Object>> volumeTrends) { this.volumeTrends = volumeTrends; }

    public Map<String, Integer> getVehicleDistribution() { return vehicleDistribution; }
    public void setVehicleDistribution(Map<String, Integer> vehicleDistribution) { this.vehicleDistribution = vehicleDistribution; }

    public List<AlertDto> getRecentAlerts() { return recentAlerts; }
    public void setRecentAlerts(List<AlertDto> recentAlerts) { this.recentAlerts = recentAlerts; }

    public List<SignalRecommendationDto> getRecentRecommendations() { return recentRecommendations; }
    public void setRecentRecommendations(List<SignalRecommendationDto> recentRecommendations) { this.recentRecommendations = recentRecommendations; }

    public List<IntersectionDto> getActiveIntersections() { return activeIntersections; }
    public void setActiveIntersections(List<IntersectionDto> activeIntersections) { this.activeIntersections = activeIntersections; }

    public static DashboardSummaryDtoBuilder builder() {
        return new DashboardSummaryDtoBuilder();
    }

    public static class DashboardSummaryDtoBuilder {
        private Long totalVehicles;
        private String currentTrafficDensity;
        private String congestionLevel;
        private Long activeAlertsCount;
        private List<Map<String, Object>> volumeTrends;
        private Map<String, Integer> vehicleDistribution;
        private List<AlertDto> recentAlerts;
        private List<SignalRecommendationDto> recentRecommendations;
        private List<IntersectionDto> activeIntersections;

        public DashboardSummaryDtoBuilder totalVehicles(Long totalVehicles) { this.totalVehicles = totalVehicles; return this; }
        public DashboardSummaryDtoBuilder currentTrafficDensity(String currentTrafficDensity) { this.currentTrafficDensity = currentTrafficDensity; return this; }
        public DashboardSummaryDtoBuilder congestionLevel(String congestionLevel) { this.congestionLevel = congestionLevel; return this; }
        public DashboardSummaryDtoBuilder activeAlertsCount(Long activeAlertsCount) { this.activeAlertsCount = activeAlertsCount; return this; }
        public DashboardSummaryDtoBuilder volumeTrends(List<Map<String, Object>> volumeTrends) { this.volumeTrends = volumeTrends; return this; }
        public DashboardSummaryDtoBuilder vehicleDistribution(Map<String, Integer> vehicleDistribution) { this.vehicleDistribution = vehicleDistribution; return this; }
        public DashboardSummaryDtoBuilder recentAlerts(List<AlertDto> recentAlerts) { this.recentAlerts = recentAlerts; return this; }
        public DashboardSummaryDtoBuilder recentRecommendations(List<SignalRecommendationDto> recentRecommendations) { this.recentRecommendations = recentRecommendations; return this; }
        public DashboardSummaryDtoBuilder activeIntersections(List<IntersectionDto> activeIntersections) { this.activeIntersections = activeIntersections; return this; }

        public DashboardSummaryDto build() {
            return new DashboardSummaryDto(totalVehicles, currentTrafficDensity, congestionLevel, activeAlertsCount, volumeTrends, vehicleDistribution, recentAlerts, recentRecommendations, activeIntersections);
        }
    }
}
