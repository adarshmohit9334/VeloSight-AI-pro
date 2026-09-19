package com.velosight.controller;

import com.velosight.dto.CameraDto;
import com.velosight.service.CameraService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cameras")
public class CameraController {

    @Autowired
    private CameraService cameraService;

    @GetMapping
    public ResponseEntity<List<CameraDto>> getAllCameras() {
        return ResponseEntity.ok(cameraService.getAllCameras());
    }

    @PostMapping
    public ResponseEntity<CameraDto> createCamera(@Valid @RequestBody CameraDto dto) {
        return ResponseEntity.ok(cameraService.createCamera(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CameraDto> updateCamera(@PathVariable("id") Long id, @RequestBody CameraDto dto) {
        return ResponseEntity.ok(cameraService.updateCamera(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCamera(@PathVariable("id") Long id) {
        cameraService.deleteCamera(id);
        return ResponseEntity.noContent().build();
    }
}
