package com.velosight.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "alerts")
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String severity;

    @Column(nullable = false, length = 1000)
    private String message;

    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "intersection_id")
    private Intersection intersection;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "camera_id")
    private Camera camera;

    @Column(nullable = false)
    private String status;

    public Alert() {}

    public Alert(Long id, String type, String severity, String message, LocalDateTime timestamp, Intersection intersection, Camera camera, String status) {
        this.id = id;
        this.type = type;
        this.severity = severity;
        this.message = message;
        this.timestamp = timestamp;
        this.intersection = intersection;
        this.camera = camera;
        this.status = status != null ? status : "ACTIVE";
    }

    @PrePersist
    protected void onCreate() {
        if (this.timestamp == null) {
            this.timestamp = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = "ACTIVE";
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public Intersection getIntersection() { return intersection; }
    public void setIntersection(Intersection intersection) { this.intersection = intersection; }

    public Camera getCamera() { return camera; }
    public void setCamera(Camera camera) { this.camera = camera; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public static AlertBuilder builder() {
        return new AlertBuilder();
    }

    public static class AlertBuilder {
        private Long id;
        private String type;
        private String severity;
        private String message;
        private LocalDateTime timestamp;
        private Intersection intersection;
        private Camera camera;
        private String status;

        public AlertBuilder id(Long id) { this.id = id; return this; }
        public AlertBuilder type(String type) { this.type = type; return this; }
        public AlertBuilder severity(String severity) { this.severity = severity; return this; }
        public AlertBuilder message(String message) { this.message = message; return this; }
        public AlertBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }
        public AlertBuilder intersection(Intersection intersection) { this.intersection = intersection; return this; }
        public AlertBuilder camera(Camera camera) { this.camera = camera; return this; }
        public AlertBuilder status(String status) { this.status = status; return this; }

        public Alert build() {
            return new Alert(id, type, severity, message, timestamp, intersection, camera, status);
        }
    }
}
