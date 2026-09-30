package com.velosight.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "vehicle_observations")
public class VehicleObservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "analysis_session_id", nullable = false)
    private AnalysisSession analysisSession;

    @Column(name = "track_id", nullable = false)
    private Integer trackId;

    @Column(name = "vehicle_type")
    private String vehicleType;

    @Column(name = "plate_number")
    private String plateNumber;

    @Column(name = "plate_status")
    private String plateStatus;

    @Column(name = "detection_confidence")
    private Double detectionConfidence;

    @Column(name = "ocr_confidence")
    private Double ocrConfidence;

    @Column(name = "first_seen")
    private Double firstSeen;

    @Column(name = "last_seen")
    private Double lastSeen;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public VehicleObservation() {}

    public VehicleObservation(Long id, AnalysisSession analysisSession, Integer trackId, String vehicleType, String plateNumber, String plateStatus, Double detectionConfidence, Double ocrConfidence, Double firstSeen, Double lastSeen, LocalDateTime createdAt) {
        this.id = id;
        this.analysisSession = analysisSession;
        this.trackId = trackId;
        this.vehicleType = vehicleType;
        this.plateNumber = plateNumber;
        this.plateStatus = plateStatus;
        this.detectionConfidence = detectionConfidence;
        this.ocrConfidence = ocrConfidence;
        this.firstSeen = firstSeen;
        this.lastSeen = lastSeen;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public AnalysisSession getAnalysisSession() { return analysisSession; }
    public void setAnalysisSession(AnalysisSession analysisSession) { this.analysisSession = analysisSession; }
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
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static VehicleObservationBuilder builder() {
        return new VehicleObservationBuilder();
    }

    public static class VehicleObservationBuilder {
        private Long id;
        private AnalysisSession analysisSession;
        private Integer trackId;
        private String vehicleType;
        private String plateNumber;
        private String plateStatus;
        private Double detectionConfidence;
        private Double ocrConfidence;
        private Double firstSeen;
        private Double lastSeen;
        private LocalDateTime createdAt;

        public VehicleObservationBuilder id(Long id) { this.id = id; return this; }
        public VehicleObservationBuilder analysisSession(AnalysisSession analysisSession) { this.analysisSession = analysisSession; return this; }
        public VehicleObservationBuilder trackId(Integer trackId) { this.trackId = trackId; return this; }
        public VehicleObservationBuilder vehicleType(String vehicleType) { this.vehicleType = vehicleType; return this; }
        public VehicleObservationBuilder plateNumber(String plateNumber) { this.plateNumber = plateNumber; return this; }
        public VehicleObservationBuilder plateStatus(String plateStatus) { this.plateStatus = plateStatus; return this; }
        public VehicleObservationBuilder detectionConfidence(Double detectionConfidence) { this.detectionConfidence = detectionConfidence; return this; }
        public VehicleObservationBuilder ocrConfidence(Double ocrConfidence) { this.ocrConfidence = ocrConfidence; return this; }
        public VehicleObservationBuilder firstSeen(Double firstSeen) { this.firstSeen = firstSeen; return this; }
        public VehicleObservationBuilder lastSeen(Double lastSeen) { this.lastSeen = lastSeen; return this; }
        public VehicleObservationBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public VehicleObservation build() {
            return new VehicleObservation(id, analysisSession, trackId, vehicleType, plateNumber, plateStatus, detectionConfidence, ocrConfidence, firstSeen, lastSeen, createdAt);
        }
    }
}
