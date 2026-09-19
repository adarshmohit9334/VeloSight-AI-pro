package com.velosight.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cameras")
public class Camera {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cameraName;

    @Column(nullable = false, unique = true)
    private String cameraIdStr;

    private String direction;
    private String streamUrl;

    @Column(nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "intersection_id")
    private Intersection intersection;

    private LocalDateTime createdAt;

    public Camera() {}

    public Camera(Long id, String cameraName, String cameraIdStr, String direction, String streamUrl, String status, Intersection intersection, LocalDateTime createdAt) {
        this.id = id;
        this.cameraName = cameraName;
        this.cameraIdStr = cameraIdStr;
        this.direction = direction;
        this.streamUrl = streamUrl;
        this.status = status;
        this.intersection = intersection;
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCameraName() { return cameraName; }
    public void setCameraName(String cameraName) { this.cameraName = cameraName; }

    public String getCameraIdStr() { return cameraIdStr; }
    public void setCameraIdStr(String cameraIdStr) { this.cameraIdStr = cameraIdStr; }

    public String getDirection() { return direction; }
    public void setDirection(String direction) { this.direction = direction; }

    public String getStreamUrl() { return streamUrl; }
    public void setStreamUrl(String streamUrl) { this.streamUrl = streamUrl; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Intersection getIntersection() { return intersection; }
    public void setIntersection(Intersection intersection) { this.intersection = intersection; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static CameraBuilder builder() {
        return new CameraBuilder();
    }

    public static class CameraBuilder {
        private Long id;
        private String cameraName;
        private String cameraIdStr;
        private String direction;
        private String streamUrl;
        private String status;
        private Intersection intersection;
        private LocalDateTime createdAt;

        public CameraBuilder id(Long id) { this.id = id; return this; }
        public CameraBuilder cameraName(String cameraName) { this.cameraName = cameraName; return this; }
        public CameraBuilder cameraIdStr(String cameraIdStr) { this.cameraIdStr = cameraIdStr; return this; }
        public CameraBuilder direction(String direction) { this.direction = direction; return this; }
        public CameraBuilder streamUrl(String streamUrl) { this.streamUrl = streamUrl; return this; }
        public CameraBuilder status(String status) { this.status = status; return this; }
        public CameraBuilder intersection(Intersection intersection) { this.intersection = intersection; return this; }
        public CameraBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public Camera build() {
            return new Camera(id, cameraName, cameraIdStr, direction, streamUrl, status, intersection, createdAt);
        }
    }
}
