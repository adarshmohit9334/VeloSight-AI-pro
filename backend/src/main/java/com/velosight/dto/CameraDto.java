package com.velosight.dto;

import java.time.LocalDateTime;

public class CameraDto {
    private Long id;
    private String cameraName;
    private String cameraIdStr;
    private String direction;
    private String streamUrl;
    private String status;
    private Long intersectionId;
    private String intersectionName;
    private LocalDateTime createdAt;

    public CameraDto() {}

    public CameraDto(Long id, String cameraName, String cameraIdStr, String direction, String streamUrl, String status, Long intersectionId, String intersectionName, LocalDateTime createdAt) {
        this.id = id;
        this.cameraName = cameraName;
        this.cameraIdStr = cameraIdStr;
        this.direction = direction;
        this.streamUrl = streamUrl;
        this.status = status;
        this.intersectionId = intersectionId;
        this.intersectionName = intersectionName;
        this.createdAt = createdAt;
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

    public Long getIntersectionId() { return intersectionId; }
    public void setIntersectionId(Long intersectionId) { this.intersectionId = intersectionId; }

    public String getIntersectionName() { return intersectionName; }
    public void setIntersectionName(String intersectionName) { this.intersectionName = intersectionName; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static CameraDtoBuilder builder() {
        return new CameraDtoBuilder();
    }

    public static class CameraDtoBuilder {
        private Long id;
        private String cameraName;
        private String cameraIdStr;
        private String direction;
        private String streamUrl;
        private String status;
        private Long intersectionId;
        private String intersectionName;
        private LocalDateTime createdAt;

        public CameraDtoBuilder id(Long id) { this.id = id; return this; }
        public CameraDtoBuilder cameraName(String cameraName) { this.cameraName = cameraName; return this; }
        public CameraDtoBuilder cameraIdStr(String cameraIdStr) { this.cameraIdStr = cameraIdStr; return this; }
        public CameraDtoBuilder direction(String direction) { this.direction = direction; return this; }
        public CameraDtoBuilder streamUrl(String streamUrl) { this.streamUrl = streamUrl; return this; }
        public CameraDtoBuilder status(String status) { this.status = status; return this; }
        public CameraDtoBuilder intersectionId(Long intersectionId) { this.intersectionId = intersectionId; return this; }
        public CameraDtoBuilder intersectionName(String intersectionName) { this.intersectionName = intersectionName; return this; }
        public CameraDtoBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public CameraDto build() {
            return new CameraDto(id, cameraName, cameraIdStr, direction, streamUrl, status, intersectionId, intersectionName, createdAt);
        }
    }
}
