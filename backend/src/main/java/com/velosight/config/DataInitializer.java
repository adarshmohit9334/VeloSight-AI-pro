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
        User admin = User.builder()
                .name("System Administrator")
                .email("admin@velosight.ai")
                .password(passwordEncoder.encode("Admin@123"))
                .role("ADMIN")
                .createdAt(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .build();

        User analyst = User.builder()
                .name("Traffic Analyst")
                .email("analyst@velosight.ai")
                .password(passwordEncoder.encode("Analyst@123"))
                .role("ANALYST")
                .createdAt(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .build();

        User operator = User.builder()
                .name("Control Room Operator")
                .email("operator@velosight.ai")
                .password(passwordEncoder.encode("Operator@123"))
                .role("OPERATOR")
                .createdAt(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .build();

        userRepository.save(admin);
        userRepository.save(analyst);
        userRepository.save(operator);
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
        Intersection i1 = intersectionRepository.findAll().get(0);
        Camera c1 = cameraRepository.findAll().get(0);

        AnalysisSession session1 = AnalysisSession.builder()
                .analysisIdStr("VS-DEMO-001")
                .videoFilename("traffic_peak_hour.mp4")
                .originalFilePath("uploads/traffic_peak_hour.mp4")
                .processedFilePath("processed/VS-DEMO-001_processed.mp4")
                .status("COMPLETED")
                .totalVehicles(156)
                .carCount(92)
                .motorcycleCount(41)
                .busCount(8)
                .truckCount(15)
                .bicycleCount(0)
                .densityLevel("HIGH")
                .densityScore(78.5)
                .congestionLevel("HIGH")
                .congestionScore(74.0)
                .averageSpeedKmh(24.5)
                .peakVehicleCount(42)
                .processingDurationSec(6.8)
                .intersection(i1)
                .camera(c1)
                .createdAt(LocalDateTime.now().minusHours(2))
                .build();

        AnalysisSession session2 = AnalysisSession.builder()
                .analysisIdStr("VS-DEMO-002")
                .videoFilename("highway_flow_morning.mp4")
                .originalFilePath("uploads/highway_flow_morning.mp4")
                .processedFilePath("processed/VS-DEMO-002_processed.mp4")
                .status("COMPLETED")
                .totalVehicles(88)
                .carCount(60)
                .motorcycleCount(20)
                .busCount(3)
                .truckCount(5)
                .bicycleCount(0)
                .densityLevel("MODERATE")
                .densityScore(45.0)
                .congestionLevel("LOW")
                .congestionScore(28.0)
                .averageSpeedKmh(48.2)
                .peakVehicleCount(22)
                .processingDurationSec(4.1)
                .intersection(i1)
                .camera(c1)
                .createdAt(LocalDateTime.now().minusHours(5))
                .build();

        analysisSessionRepository.save(session1);
        analysisSessionRepository.save(session2);

        // Seed time series metrics
        for (int h = 12; h >= 0; h--) {
            TrafficMetric m = TrafficMetric.builder()
                    .analysisSession(session1)
                    .intersection(i1)
                    .vehicleCount(80 + (h * 7) + (int)(Math.random() * 20))
                    .densityScore(35.0 + (h * 3.5))
                    .congestionScore(25.0 + (h * 4.0))
                    .averageSpeedKmh(52.0 - (h * 2.1))
                    .timestamp(LocalDateTime.now().minusHours(h))
                    .build();
            trafficMetricRepository.save(m);
        }
    }

    private void seedAlerts() {
        Intersection i1 = intersectionRepository.findAll().get(0);
        Camera c1 = cameraRepository.findAll().get(0);

        Alert a1 = Alert.builder()
                .type("HIGH_CONGESTION")
                .severity("CRITICAL")
                .message("Severe traffic queue buildup detected at Central Avenue North Approach (Score: 84%).")
                .intersection(i1)
                .camera(c1)
                .status("ACTIVE")
                .timestamp(LocalDateTime.now().minusMinutes(25))
                .build();

        Alert a2 = Alert.builder()
                .type("POTENTIAL_INCIDENT")
                .severity("HIGH")
                .message("Unusual stationary vehicle cluster detected near IT Corridor Phase 1 lane 2.")
                .intersection(i1)
                .camera(c1)
                .status("ACTIVE")
                .timestamp(LocalDateTime.now().minusMinutes(45))
                .build();

        Alert a3 = Alert.builder()
                .type("CAMERA_OFFLINE")
                .severity("MEDIUM")
                .message("Camera CAM-WEST-01 lost RTSP signal ping.")
                .intersection(i1)
                .camera(c1)
                .status("ACKNOWLEDGED")
                .timestamp(LocalDateTime.now().minusHours(3))
                .build();

        alertRepository.save(a1);
        alertRepository.save(a2);
        alertRepository.save(a3);
    }

    private void seedRecommendations() {
        Intersection i1 = intersectionRepository.findAll().get(0);

        SignalRecommendation r1 = SignalRecommendation.builder()
                .intersection(i1)
                .nsTrafficScore(82.5)
                .ewTrafficScore(34.0)
                .nsGreenDurationSec(55)
                .ewGreenDurationSec(25)
                .reason("North-South traffic corridor has significantly higher vehicle volume (82.5%) compared to East-West (34.0%). Extending N-S green phase duration.")
                .confidence(0.94)
                .createdAt(LocalDateTime.now().minusMinutes(10))
                .build();

        signalRecommendationRepository.save(r1);
    }
}
