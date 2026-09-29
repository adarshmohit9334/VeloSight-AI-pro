package com.velosight.config;

import com.velosight.entity.*;
import com.velosight.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IntersectionRepository intersectionRepository;

    @Autowired
    private CameraRepository cameraRepository;

    @Autowired
    private AnalysisSessionRepository analysisSessionRepository;

    @Autowired
    private TrafficMetricRepository trafficMetricRepository;

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private SignalRecommendationRepository signalRecommendationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            seedUsers();
        }
        if (intersectionRepository.count() == 0) {
            seedIntersectionsAndCameras();
        }
        if (analysisSessionRepository.count() == 0) {
            seedAnalysisSessions();
        }
        if (alertRepository.count() == 0) {
            seedAlerts();
        }
        if (signalRecommendationRepository.count() == 0) {
            seedRecommendations();
        }
    }

    private void seedUsers() {
        // Dummy users auto-seeding disabled as requested by user.
    }

    private void seedIntersectionsAndCameras() {
        Intersection i1 = Intersection.builder()
                .name("Central Avenue Crossing")
                .location("Downtown Sector 4")
                .latitude(18.5204)
                .longitude(73.8567)
                .status("ACTIVE")
                .cameraCount(2)
                .build();

        Intersection i2 = Intersection.builder()
                .name("North Plaza Junction")
                .location("IT Corridor Phase 1")
                .latitude(18.5314)
                .longitude(73.8446)
                .status("ACTIVE")
                .cameraCount(2)
                .build();

        Intersection i3 = Intersection.builder()
                .name("West Highway Intersect")
                .location("Expressway Gateway")
                .latitude(18.5089)
                .longitude(73.8012)
                .status("ACTIVE")
                .cameraCount(1)
                .build();

        intersectionRepository.save(i1);
        intersectionRepository.save(i2);
        intersectionRepository.save(i3);

        Camera c1 = Camera.builder()
                .cameraName("Central North Cam-01")
                .cameraIdStr("CAM-CENTRAL-01")
                .direction("North")
                .streamUrl("http://localhost:8080/sample_stream_1.mp4")
                .status("ONLINE")
                .intersection(i1)
                .build();

        Camera c2 = Camera.builder()
                .cameraName("Central East Cam-02")
                .cameraIdStr("CAM-CENTRAL-02")
                .direction("East")
                .streamUrl("http://localhost:8080/sample_stream_2.mp4")
                .status("ONLINE")
                .intersection(i1)
                .build();

        Camera c3 = Camera.builder()
                .cameraName("North Plaza Cam-01")
                .cameraIdStr("CAM-NORTH-01")
                .direction("South")
                .streamUrl("http://localhost:8080/sample_stream_3.mp4")
                .status("ONLINE")
                .intersection(i2)
                .build();

        cameraRepository.save(c1);
        cameraRepository.save(c2);
        cameraRepository.save(c3);
    }

    private void seedAnalysisSessions() {
        // Dummy data seeding disabled
    }

    private void seedAlerts() {
        // Dummy alerts disabled
    }

    private void seedRecommendations() {
        // Dummy recommendations disabled
    }
}
