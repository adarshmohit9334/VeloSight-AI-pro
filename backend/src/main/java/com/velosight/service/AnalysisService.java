package com.velosight.service;

import com.velosight.dto.*;
import com.velosight.entity.*;
import com.velosight.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AnalysisService {

    private static final Logger logger = LoggerFactory.getLogger(AnalysisService.class);

    @Value("${velosight.upload-dir:uploads}")
    private String uploadDir;

    @Value("${velosight.processed-dir:processed}")
    private String processedDir;

    @Value("${velosight.ai-service.url:http://localhost:8000}")
    private String aiServiceUrl;

    @Autowired
    private AnalysisSessionRepository analysisSessionRepository;

    @Autowired
    private CameraRepository cameraRepository;

    @Autowired
    private IntersectionRepository intersectionRepository;

    @Autowired
    private TrafficMetricRepository trafficMetricRepository;

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private SignalRecommendationRepository signalRecommendationRepository;

    @Autowired
    private VehicleObservationRepository vehicleObservationRepository;

    @Autowired
    @org.springframework.context.annotation.Lazy
    private AnalysisService self;

    private final RestTemplate restTemplate = new RestTemplate();

    public AnalysisUploadResponse uploadAndStartAnalysis(MultipartFile file, Long cameraId, String processingMode, boolean generateVideo) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String analysisId = "VS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String savedFilename = analysisId + extension;

        Path uploadPath = Paths.get(uploadDir).toAbsolutePath();
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path targetLocation = uploadPath.resolve(savedFilename);
        Files.copy(file.getInputStream(), targetLocation, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

        Camera camera = null;
        Intersection intersection = null;
        if (cameraId != null) {
            camera = cameraRepository.findById(cameraId).orElse(null);
            if (camera != null) {
                intersection = camera.getIntersection();
            }
        }

        AnalysisSession session = AnalysisSession.builder()
                .analysisIdStr(analysisId)
                .videoFilename(originalFilename)
                .originalFilePath(targetLocation.toString())
                .status("QUEUED")
                .camera(camera)
                .intersection(intersection)
                .createdAt(LocalDateTime.now())
                .build();

        analysisSessionRepository.save(session);

        // Trigger async AI analysis pipeline via the self proxy to ensure @Async works
        self.triggerAiServiceAnalysis(session, targetLocation.toString(), processingMode, generateVideo);

        return AnalysisUploadResponse.builder()
                .analysisId(analysisId)
                .filename(originalFilename)
                .status("QUEUED")
                .message("Video uploaded successfully. AI processing pipeline queued.")
                .build();
    }

    @Async
    public void triggerAiServiceAnalysis(AnalysisSession session, String absoluteFilePath, String processingMode, boolean generateVideo) {
        try {
            session.setStatus("PROCESSING");
            analysisSessionRepository.save(session);

            int frameSkip = 6; // FAST (~5 fps)
            if ("BALANCED".equalsIgnoreCase(processingMode)) frameSkip = 4; // ~8 fps
            else if ("ACCURATE".equalsIgnoreCase(processingMode)) frameSkip = 3; // ~10-12 fps

            Map<String, Object> aiRequest = new HashMap<>();
            aiRequest.put("analysisId", session.getAnalysisIdStr());
            aiRequest.put("videoPath", absoluteFilePath);
            aiRequest.put("confidenceThreshold", 0.40);
            aiRequest.put("frameSkip", frameSkip);
            aiRequest.put("generateVideo", generateVideo);
            aiRequest.put("processingMode", processingMode);

            String url = aiServiceUrl + "/ai/analyze";
            ResponseEntity<Map> response = restTemplate.postForEntity(url, aiRequest, Map.class);
            logger.info("AI service accepted request for {}: {}", session.getAnalysisIdStr(), response.getBody());

            // Poll for completion asynchronously
            pollAiServiceForResults(session);

        } catch (Exception e) {
            logger.error("Failed to connect to Python AI Service for analysis {}: {}", session.getAnalysisIdStr(), e.getMessage());
            // Fallback simulation / graceful demo result if AI service is offline
            populateFallbackResults(session);
        }
    }

    private void pollAiServiceForResults(AnalysisSession session) {
        String statusUrl = aiServiceUrl + "/ai/status/" + session.getAnalysisIdStr();
        String resultsUrl = aiServiceUrl + "/ai/results/" + session.getAnalysisIdStr();

        int attempts = 0;
        boolean completed = false;

        while (attempts < 600 && !completed) { // Increased timeout from 120s to 1200s (20 mins)
            try {
                Thread.sleep(2000);
                attempts++;

                Map statusResp = restTemplate.getForObject(statusUrl, Map.class);
                if (statusResp != null) {
                    String status = (String) statusResp.get("status");
                    if ("COMPLETED".equals(status)) {
                        completed = true;
                        Map results = restTemplate.getForObject(resultsUrl, Map.class);
                        if (results != null) {
                            saveAiResultsToDatabase(session, results);
                        }
                    } else if ("FAILED".equals(status)) {
                        session.setStatus("FAILED");
                        analysisSessionRepository.save(session);
                        break;
                    }
                }
            } catch (Exception e) {
                logger.warn("Polling AI service attempt {} failed: {}", attempts, e.getMessage());
            }
        }

        if (!completed && !"FAILED".equals(session.getStatus())) {
            populateFallbackResults(session);
        }
    }

    @SuppressWarnings("unchecked")
    private void saveAiResultsToDatabase(AnalysisSession session, Map<String, Object> results) {
        try {
            session.setStatus("COMPLETED");
            session.setTotalVehicles((Integer) results.getOrDefault("totalVehicles", 0));
            session.setPeakVehicleCount((Integer) results.getOrDefault("peakVehicleCount", 0));
            
            Object avgSpeedObj = results.get("averageEstimatedSpeedKmh");
            session.setAverageSpeedKmh(avgSpeedObj instanceof Number ? ((Number) avgSpeedObj).doubleValue() : 35.0);

            Object procDurObj = results.get("processingDurationSec");
            session.setProcessingDurationSec(procDurObj instanceof Number ? ((Number) procDurObj).doubleValue() : 5.0);

            Map<String, Object> counts = (Map<String, Object>) results.get("vehicleCounts");
            if (counts != null) {
                session.setCarCount((Integer) counts.getOrDefault("car", 0));
                session.setMotorcycleCount((Integer) counts.getOrDefault("motorcycle", 0));
                session.setBusCount((Integer) counts.getOrDefault("bus", 0));
                session.setTruckCount((Integer) counts.getOrDefault("truck", 0));
                session.setBicycleCount((Integer) counts.getOrDefault("bicycle", 0));
            }

            Map<String, Object> densityMap = (Map<String, Object>) results.get("density");
            if (densityMap != null) {
                session.setDensityLevel((String) densityMap.get("level"));
                Object score = densityMap.get("score");
                session.setDensityScore(score instanceof Number ? ((Number) score).doubleValue() : 50.0);
            }

            Map<String, Object> congestionMap = (Map<String, Object>) results.get("congestion");
            if (congestionMap != null) {
                session.setCongestionLevel((String) congestionMap.get("level"));
                Object score = congestionMap.get("score");
                session.setCongestionScore(score instanceof Number ? ((Number) score).doubleValue() : 40.0);
            }

            Map<String, Object> meta = (Map<String, Object>) results.get("videoMetadata");
            if (meta != null && meta.containsKey("processedFilePath")) {
                session.setProcessedFilePath((String) meta.get("processedFilePath"));
            }

            analysisSessionRepository.save(session);

            // Create TrafficMetric record
            TrafficMetric metric = TrafficMetric.builder()
                    .analysisSession(session)
                    .intersection(session.getIntersection())
                    .vehicleCount(session.getTotalVehicles())
                    .densityScore(session.getDensityScore())
                    .congestionScore(session.getCongestionScore())
                    .averageSpeedKmh(session.getAverageSpeedKmh())
                    .timestamp(LocalDateTime.now())
                    .build();
            trafficMetricRepository.save(metric);

            // Handle incident / high congestion alert creation
            if ("HIGH".equals(session.getCongestionLevel()) || "SEVERE".equals(session.getCongestionLevel())) {
                Alert alert = Alert.builder()
                        .type("HIGH_CONGESTION")
                        .severity("SEVERE".equals(session.getCongestionLevel()) ? "CRITICAL" : "HIGH")
                        .message("Heavy congestion (" + session.getCongestionScore() + "%) detected during analysis " + session.getAnalysisIdStr())
                        .intersection(session.getIntersection())
                        .camera(session.getCamera())
                        .status("ACTIVE")
                        .timestamp(LocalDateTime.now())
                        .build();
                alertRepository.save(alert);
            }

            // Save Signal Recommendation
            Map<String, Object> sigMap = (Map<String, Object>) results.get("signalRecommendation");
            if (sigMap != null) {
                SignalRecommendation rec = SignalRecommendation.builder()
                        .intersection(session.getIntersection())
                        .nsTrafficScore(sigMap.containsKey("nsScore") ? ((Number) sigMap.get("nsScore")).doubleValue() : 75.0)
                        .ewTrafficScore(sigMap.containsKey("ewScore") ? ((Number) sigMap.get("ewScore")).doubleValue() : 35.0)
                        .nsGreenDurationSec((Integer) sigMap.getOrDefault("nsGreenDuration", 55))
                        .ewGreenDurationSec((Integer) sigMap.getOrDefault("ewGreenDuration", 25))
                        .reason((String) sigMap.get("reason"))
                        .confidence(sigMap.containsKey("confidence") ? ((Number) sigMap.get("confidence")).doubleValue() : 0.90)
                        .createdAt(LocalDateTime.now())
                        .build();
                signalRecommendationRepository.save(rec);
            }

            // Save Vehicle Observations
            List<Map<String, Object>> observations = (List<Map<String, Object>>) results.get("vehicleObservations");
            if (observations != null) {
                for (Map<String, Object> obsMap : observations) {
                    VehicleObservation vo = VehicleObservation.builder()
                            .analysisSession(session)
                            .trackId(obsMap.containsKey("trackId") ? ((Number) obsMap.get("trackId")).intValue() : 0)
                            .vehicleType((String) obsMap.get("vehicleType"))
                            .plateNumber((String) obsMap.get("plateNumber"))
                            .plateStatus((String) obsMap.get("plateStatus"))
                            .detectionConfidence(obsMap.containsKey("detectionConfidence") ? ((Number) obsMap.get("detectionConfidence")).doubleValue() : 0.0)
                            .ocrConfidence(obsMap.containsKey("ocrConfidence") ? ((Number) obsMap.get("ocrConfidence")).doubleValue() : 0.0)
                            .firstSeen(obsMap.containsKey("firstSeen") ? ((Number) obsMap.get("firstSeen")).doubleValue() : 0.0)
                            .lastSeen(obsMap.containsKey("lastSeen") ? ((Number) obsMap.get("lastSeen")).doubleValue() : 0.0)
                            .build();
                    vehicleObservationRepository.save(vo);
                }
            }

        } catch (Exception e) {
            logger.error("Error saving AI results: {}", e.getMessage(), e);
        }
    }

    private void populateFallbackResults(AnalysisSession session) {
        logger.info("Executing fallback result generation for analysis ID: {}", session.getAnalysisIdStr());
        session.setStatus("FAILED");
        analysisSessionRepository.save(session);
    }

    public List<AnalysisResultDto> getAllAnalyses() {
        List<AnalysisSession> sessions = analysisSessionRepository.findTop10ByOrderByCreatedAtDesc();
        return sessions.stream().map(this::convertToDto).toList();
    }

    public AnalysisResultDto getAnalysisById(String analysisIdStr) {
        AnalysisSession session = analysisSessionRepository.findByAnalysisIdStr(analysisIdStr)
                .orElseThrow(() -> new RuntimeException("Analysis not found: " + analysisIdStr));
        return convertToDto(session);
    }

    public AnalysisStatusResponse getLiveAnalysisStatus(String analysisIdStr) {
        AnalysisSession session = analysisSessionRepository.findByAnalysisIdStr(analysisIdStr)
                .orElseThrow(() -> new RuntimeException("Analysis not found: " + analysisIdStr));
        
        AnalysisStatusResponse response = new AnalysisStatusResponse();
        response.setAnalysisId(session.getAnalysisIdStr());
        response.setStatus(session.getStatus());
        
        if ("PROCESSING".equals(session.getStatus()) || "QUEUED".equals(session.getStatus())) {
            try {
                String statusUrl = aiServiceUrl + "/ai/status/" + session.getAnalysisIdStr();
                Map statusResp = restTemplate.getForObject(statusUrl, Map.class);
                if (statusResp != null) {
                    Object progressObj = statusResp.get("progress");
                    if (progressObj instanceof Number) {
                        response.setProgress(((Number) progressObj).doubleValue());
                    }
                    response.setStep((String) statusResp.get("step"));
                }
            } catch (Exception e) {
                logger.warn("Could not fetch live status from AI service for {}: {}", session.getAnalysisIdStr(), e.getMessage());
            }
        } else if ("COMPLETED".equals(session.getStatus())) {
            response.setProgress(100.0);
            response.setStep("Analysis Completed");
            response.setResult(convertToDto(session));
        } else if ("FAILED".equals(session.getStatus())) {
            response.setProgress(0.0);
            response.setStep("Analysis Failed");
            response.setError("Failed during processing");
        }
        
        return response;
    }

    @org.springframework.transaction.annotation.Transactional
    public void deleteAnalysis(String analysisIdStr) {
        AnalysisSession session = analysisSessionRepository.findByAnalysisIdStr(analysisIdStr)
                .orElseThrow(() -> new RuntimeException("Analysis not found: " + analysisIdStr));
        trafficMetricRepository.deleteByAnalysisSession(session);
        vehicleObservationRepository.deleteByAnalysisSession(session);
        analysisSessionRepository.delete(session);
    }

    private AnalysisResultDto convertToDto(AnalysisSession session) {
        Map<String, Integer> counts = new HashMap<>();
        counts.put("car", session.getCarCount() != null ? session.getCarCount() : 0);
        counts.put("motorcycle", session.getMotorcycleCount() != null ? session.getMotorcycleCount() : 0);
        counts.put("bus", session.getBusCount() != null ? session.getBusCount() : 0);
        counts.put("truck", session.getTruckCount() != null ? session.getTruckCount() : 0);
        counts.put("bicycle", session.getBicycleCount() != null ? session.getBicycleCount() : 0);

        List<VehicleObservation> observations = vehicleObservationRepository.findByAnalysisSession(session);
        List<VehicleObservationDto> obsDtos = observations.stream().map(obs -> VehicleObservationDto.builder()
                .id(obs.getId())
                .trackId(obs.getTrackId())
                .vehicleType(obs.getVehicleType())
                .plateNumber(obs.getPlateNumber())
                .plateStatus(obs.getPlateStatus())
                .detectionConfidence(obs.getDetectionConfidence())
                .ocrConfidence(obs.getOcrConfidence())
                .firstSeen(obs.getFirstSeen())
                .lastSeen(obs.getLastSeen())
                .build()).toList();

        return AnalysisResultDto.builder()
                .id(session.getId())
                .analysisId(session.getAnalysisIdStr())
                .videoFilename(session.getVideoFilename())
                .status(session.getStatus())
                .totalVehicles(session.getTotalVehicles())
                .vehicleCounts(counts)
                .carCount(session.getCarCount())
                .motorcycleCount(session.getMotorcycleCount())
                .busCount(session.getBusCount())
                .truckCount(session.getTruckCount())
                .bicycleCount(session.getBicycleCount())
                .densityLevel(session.getDensityLevel())
                .densityScore(session.getDensityScore())
                .congestionLevel(session.getCongestionLevel())
                .congestionScore(session.getCongestionScore())
                .averageSpeedKmh(session.getAverageSpeedKmh())
                .peakVehicleCount(session.getPeakVehicleCount())
                .processingDurationSec(session.getProcessingDurationSec())
                .cameraName(session.getCamera() != null ? session.getCamera().getCameraName() : "Main Camera")
                .intersectionName(session.getIntersection() != null ? session.getIntersection().getName() : "Central Avenue Junction")
                .processedVideoUrl("/api/analysis/" + session.getAnalysisIdStr() + "/processed-video")
                .createdAt(session.getCreatedAt())
                .vehicleObservations(obsDtos)
                .build();
    }
}
