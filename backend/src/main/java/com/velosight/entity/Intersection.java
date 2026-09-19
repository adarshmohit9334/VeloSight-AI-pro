package com.velosight.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "intersections")
public class Intersection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    private Double latitude;
    private Double longitude;

    @Column(nullable = false)
    private String status; // ACTIVE, INACTIVE, MAINTENANCE

    private Integer cameraCount;
    private LocalDateTime createdAt;

    public Intersection() {}

    public Intersection(Long id, String name, String location, Double latitude, Double longitude, String status, Integer cameraCount, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.cameraCount = cameraCount != null ? cameraCount : 0;
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.cameraCount == null) {
            this.cameraCount = 0;
        }
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

    public static IntersectionBuilder builder() {
        return new IntersectionBuilder();
    }

    public static class IntersectionBuilder {
        private Long id;
        private String name;
        private String location;
        private Double latitude;
        private Double longitude;
        private String status;
        private Integer cameraCount;
        private LocalDateTime createdAt;

        public IntersectionBuilder id(Long id) { this.id = id; return this; }
        public IntersectionBuilder name(String name) { this.name = name; return this; }
        public IntersectionBuilder location(String location) { this.location = location; return this; }
        public IntersectionBuilder latitude(Double latitude) { this.latitude = latitude; return this; }
        public IntersectionBuilder longitude(Double longitude) { this.longitude = longitude; return this; }
        public IntersectionBuilder status(String status) { this.status = status; return this; }
        public IntersectionBuilder cameraCount(Integer cameraCount) { this.cameraCount = cameraCount; return this; }
        public IntersectionBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public Intersection build() {
            return new Intersection(id, name, location, latitude, longitude, status, cameraCount, createdAt);
        }
    }
}
