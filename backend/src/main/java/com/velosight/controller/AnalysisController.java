package com.velosight.controller;

import com.velosight.dto.AnalysisResultDto;
import com.velosight.dto.AnalysisStatusResponse;
import com.velosight.dto.AnalysisUploadResponse;
import com.velosight.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {

    @Autowired
    private AnalysisService analysisService;

    @PostMapping("/upload")
    public ResponseEntity<AnalysisUploadResponse> uploadVideo(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "cameraId", required = false) Long cameraId,
            @RequestParam(value = "processingMode", defaultValue = "FAST") String processingMode,
            @RequestParam(value = "generateVideo", defaultValue = "false") boolean generateVideo) throws IOException {
        return ResponseEntity.ok(analysisService.uploadAndStartAnalysis(file, cameraId, processingMode, generateVideo));
    }

    @GetMapping
    public ResponseEntity<List<AnalysisResultDto>> getAllAnalyses() {
        return ResponseEntity.ok(analysisService.getAllAnalyses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnalysisResultDto> getAnalysisById(@PathVariable("id") String id) {
        return ResponseEntity.ok(analysisService.getAnalysisById(id));
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<AnalysisStatusResponse> getAnalysisStatus(@PathVariable("id") String id) {
        return ResponseEntity.ok(analysisService.getLiveAnalysisStatus(id));
    }

    @GetMapping("/{id}/results")
    public ResponseEntity<AnalysisResultDto> getAnalysisResults(@PathVariable("id") String id) {
        return ResponseEntity.ok(analysisService.getAnalysisById(id));
    }

    @GetMapping("/{id}/processed-video")
    public ResponseEntity<Resource> getProcessedVideo(@PathVariable("id") String id) {
        AnalysisResultDto dto = analysisService.getAnalysisById(id);
        String videoPath = "processed/" + id + "_processed.mp4";
        File file = new File(videoPath);
        if (!file.exists()) {
            String extension = "";
            if (dto.getVideoFilename() != null && dto.getVideoFilename().contains(".")) {
                extension = dto.getVideoFilename().substring(dto.getVideoFilename().lastIndexOf("."));
            }
            file = new File("uploads/" + id + extension);
        }

        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(file);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("video/mp4"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getName() + "\"")
                .body(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnalysis(@PathVariable("id") String id) {
        analysisService.deleteAnalysis(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/vehicles")
    public ResponseEntity<List<com.velosight.dto.VehicleObservationDto>> getVehiclesForAnalysis(@PathVariable("id") String id) {
        AnalysisResultDto dto = analysisService.getAnalysisById(id);
        if (dto.getVehicleObservations() == null) {
            return ResponseEntity.ok(java.util.Collections.emptyList());
        }
        return ResponseEntity.ok(dto.getVehicleObservations());
    }

    @GetMapping("/{id}/plates")
    public ResponseEntity<List<com.velosight.dto.VehicleObservationDto>> getPlatesForAnalysis(@PathVariable("id") String id) {
        AnalysisResultDto dto = analysisService.getAnalysisById(id);
        if (dto.getVehicleObservations() == null) {
            return ResponseEntity.ok(java.util.Collections.emptyList());
        }
        List<com.velosight.dto.VehicleObservationDto> platesOnly = dto.getVehicleObservations().stream()
                .filter(obs -> "READ".equals(obs.getPlateStatus()) || "LOW_CONFIDENCE".equals(obs.getPlateStatus()))
                .filter(obs -> obs.getPlateNumber() != null && !obs.getPlateNumber().isEmpty())
                .toList();
        return ResponseEntity.ok(platesOnly);
    }

    @GetMapping("/{id}/vehicles/{trackId}")
    public ResponseEntity<com.velosight.dto.VehicleObservationDto> getVehicleByTrackId(@PathVariable("id") String id, @PathVariable("trackId") Integer trackId) {
        AnalysisResultDto dto = analysisService.getAnalysisById(id);
        if (dto.getVehicleObservations() == null) {
            return ResponseEntity.notFound().build();
        }
        return dto.getVehicleObservations().stream()
                .filter(obs -> trackId.equals(obs.getTrackId()))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.badRequest().body("Error: " + e.getMessage());
    }
}
