package com.velosight.controller;

import com.velosight.dto.AnalysisResultDto;
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
            @RequestParam(value = "cameraId", required = false) Long cameraId) throws IOException {
        return ResponseEntity.ok(analysisService.uploadAndStartAnalysis(file, cameraId));
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
    public ResponseEntity<AnalysisResultDto> getAnalysisStatus(@PathVariable("id") String id) {
        return ResponseEntity.ok(analysisService.getAnalysisById(id));
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
            file = new File("uploads/" + dto.getVideoFilename());
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
}
