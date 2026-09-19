package com.velosight.dto;

import java.time.LocalDateTime;

public class IntersectionDto {
    private Long id;
    private String name;
    private String location;
    private Double latitude;
    private Double longitude;
    private String status;
    private Integer cameraCount;
    private LocalDateTime createdAt;

    public IntersectionDto() {}

    public IntersectionDto(Long id, String name, String location, Double latitude, Double longitude, String status, Integer cameraCount, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.cameraCount = cameraCount;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getCameraCount() { return cameraCount; }
    public void setCameraCount(Integer cameraCount) { this.cameraCount = cameraCount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static IntersectionDtoBuilder builder() {
        return new IntersectionDtoBuilder();
    }

    public static class IntersectionDtoBuilder {
        private Long id;
        private String name;
        private String location;
        private Double latitude;
        private Double longitude;
        private String status;
        private Integer cameraCount;
        private LocalDateTime createdAt;

        public IntersectionDtoBuilder id(Long id) { this.id = id; return this; }
        public IntersectionDtoBuilder name(String name) { this.name = name; return this; }
        public IntersectionDtoBuilder location(String location) { this.location = location; return this; }
        public IntersectionDtoBuilder latitude(Double latitude) { this.latitude = latitude; return this; }
        public IntersectionDtoBuilder longitude(Double longitude) { this.longitude = longitude; return this; }
        public IntersectionDtoBuilder status(String status) { this.status = status; return this; }
        public IntersectionDtoBuilder cameraCount(Integer cameraCount) { this.cameraCount = cameraCount; return this; }
        public IntersectionDtoBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public IntersectionDto build() {
            return new IntersectionDto(id, name, location, latitude, longitude, status, cameraCount, createdAt);
        }
    }
}
