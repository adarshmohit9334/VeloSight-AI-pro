package com.velosight.dto;

import java.time.LocalDateTime;

public class AlertDto {
    private Long id;
    private String type;
    private String severity;
    private String message;
    private LocalDateTime timestamp;
    private String intersectionName;
    private String cameraName;
    private String status;

    public AlertDto() {}

    public AlertDto(Long id, String type, String severity, String message, LocalDateTime timestamp, String intersectionName, String cameraName, String status) {
        this.id = id;
        this.type = type;
        this.severity = severity;
        this.message = message;
        this.timestamp = timestamp;
        this.intersectionName = intersectionName;
        this.cameraName = cameraName;
        this.status = status;
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

    public String getIntersectionName() { return intersectionName; }
    public void setIntersectionName(String intersectionName) { this.intersectionName = intersectionName; }

    public String getCameraName() { return cameraName; }
    public void setCameraName(String cameraName) { this.cameraName = cameraName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public static AlertDtoBuilder builder() {
        return new AlertDtoBuilder();
    }

    public static class AlertDtoBuilder {
        private Long id;
        private String type;
        private String severity;
        private String message;
        private LocalDateTime timestamp;
        private String intersectionName;
        private String cameraName;
        private String status;

        public AlertDtoBuilder id(Long id) { this.id = id; return this; }
        public AlertDtoBuilder type(String type) { this.type = type; return this; }
        public AlertDtoBuilder severity(String severity) { this.severity = severity; return this; }
        public AlertDtoBuilder message(String message) { this.message = message; return this; }
        public AlertDtoBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }
        public AlertDtoBuilder intersectionName(String intersectionName) { this.intersectionName = intersectionName; return this; }
        public AlertDtoBuilder cameraName(String cameraName) { this.cameraName = cameraName; return this; }
        public AlertDtoBuilder status(String status) { this.status = status; return this; }

        public AlertDto build() {
            return new AlertDto(id, type, severity, message, timestamp, intersectionName, cameraName, status);
        }
    }
}
