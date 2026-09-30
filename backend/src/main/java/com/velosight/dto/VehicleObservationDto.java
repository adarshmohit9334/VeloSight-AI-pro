package com.velosight.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
public class VehicleObservationDto {
    private Long id;
    private Integer trackId;
    private String vehicleType;
    private String plateNumber;
    private String plateStatus;
    private Double detectionConfidence;
    private Double ocrConfidence;
    private Double firstSeen;
    private Double lastSeen;

    public VehicleObservationDto() {}

    public VehicleObservationDto(Long id, Integer trackId, String vehicleType, String plateNumber, String plateStatus, Double detectionConfidence, Double ocrConfidence, Double firstSeen, Double lastSeen) {
        this.id = id;
        this.trackId = trackId;
        this.vehicleType = vehicleType;
        this.plateNumber = plateNumber;
        this.plateStatus = plateStatus;
        this.detectionConfidence = detectionConfidence;
        this.ocrConfidence = ocrConfidence;
        this.firstSeen = firstSeen;
        this.lastSeen = lastSeen;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getTrackId() { return trackId; }
    public void setTrackId(Integer trackId) { this.trackId = trackId; }
    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }
    public String getPlateNumber() { return plateNumber; }
    public void setPlateNumber(String plateNumber) { this.plateNumber = plateNumber; }
    public String getPlateStatus() { return plateStatus; }
    public void setPlateStatus(String plateStatus) { this.plateStatus = plateStatus; }
    public Double getDetectionConfidence() { return detectionConfidence; }
    public void setDetectionConfidence(Double detectionConfidence) { this.detectionConfidence = detectionConfidence; }
    public Double getOcrConfidence() { return ocrConfidence; }
    public void setOcrConfidence(Double ocrConfidence) { this.ocrConfidence = ocrConfidence; }
    public Double getFirstSeen() { return firstSeen; }
    public void setFirstSeen(Double firstSeen) { this.firstSeen = firstSeen; }
    public Double getLastSeen() { return lastSeen; }
    public void setLastSeen(Double lastSeen) { this.lastSeen = lastSeen; }

    public static VehicleObservationDtoBuilder builder() {
        return new VehicleObservationDtoBuilder();
    }

    public static class VehicleObservationDtoBuilder {
        private Long id;
        private Integer trackId;
        private String vehicleType;
        private String plateNumber;
        private String plateStatus;
        private Double detectionConfidence;
        private Double ocrConfidence;
        private Double firstSeen;
        private Double lastSeen;

        public VehicleObservationDtoBuilder id(Long id) { this.id = id; return this; }
        public VehicleObservationDtoBuilder trackId(Integer trackId) { this.trackId = trackId; return this; }
        public VehicleObservationDtoBuilder vehicleType(String vehicleType) { this.vehicleType = vehicleType; return this; }
        public VehicleObservationDtoBuilder plateNumber(String plateNumber) { this.plateNumber = plateNumber; return this; }
        public VehicleObservationDtoBuilder plateStatus(String plateStatus) { this.plateStatus = plateStatus; return this; }
        public VehicleObservationDtoBuilder detectionConfidence(Double detectionConfidence) { this.detectionConfidence = detectionConfidence; return this; }
        public VehicleObservationDtoBuilder ocrConfidence(Double ocrConfidence) { this.ocrConfidence = ocrConfidence; return this; }
        public VehicleObservationDtoBuilder firstSeen(Double firstSeen) { this.firstSeen = firstSeen; return this; }
        public VehicleObservationDtoBuilder lastSeen(Double lastSeen) { this.lastSeen = lastSeen; return this; }

        public VehicleObservationDto build() {
            return new VehicleObservationDto(id, trackId, vehicleType, plateNumber, plateStatus, detectionConfidence, ocrConfidence, firstSeen, lastSeen);
        }
    }
}
