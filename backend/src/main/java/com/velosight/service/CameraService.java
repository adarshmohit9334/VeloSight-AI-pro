package com.velosight.service;

import com.velosight.dto.CameraDto;
import com.velosight.entity.Camera;
import com.velosight.entity.Intersection;
import com.velosight.repository.CameraRepository;
import com.velosight.repository.IntersectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CameraService {

    @Autowired
    private CameraRepository cameraRepository;

    @Autowired
    private IntersectionRepository intersectionRepository;

    public List<CameraDto> getAllCameras() {
        return cameraRepository.findAll().stream().map(this::convertToDto).toList();
    }

    public CameraDto createCamera(CameraDto dto) {
        Intersection intersection = null;
        if (dto.getIntersectionId() != null) {
            intersection = intersectionRepository.findById(dto.getIntersectionId()).orElse(null);
            if (intersection != null) {
                intersection.setCameraCount((intersection.getCameraCount() != null ? intersection.getCameraCount() : 0) + 1);
                intersectionRepository.save(intersection);
            }
        }

        Camera camera = Camera.builder()
                .cameraName(dto.getCameraName())
                .cameraIdStr(dto.getCameraIdStr() != null ? dto.getCameraIdStr() : "CAM-" + System.currentTimeMillis())
                .direction(dto.getDirection() != null ? dto.getDirection() : "North")
                .streamUrl(dto.getStreamUrl())
                .status(dto.getStatus() != null ? dto.getStatus() : "ONLINE")
                .intersection(intersection)
                .build();

        return convertToDto(cameraRepository.save(camera));
    }

    public CameraDto updateCamera(Long id, CameraDto dto) {
        Camera camera = cameraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Camera not found with id: " + id));

        if (dto.getCameraName() != null) camera.setCameraName(dto.getCameraName());
        if (dto.getDirection() != null) camera.setDirection(dto.getDirection());
        if (dto.getStreamUrl() != null) camera.setStreamUrl(dto.getStreamUrl());
        if (dto.getStatus() != null) camera.setStatus(dto.getStatus());

        return convertToDto(cameraRepository.save(camera));
    }

    public void deleteCamera(Long id) {
        cameraRepository.deleteById(id);
    }

    private CameraDto convertToDto(Camera c) {
        return CameraDto.builder()
                .id(c.getId())
                .cameraName(c.getCameraName())
                .cameraIdStr(c.getCameraIdStr())
                .direction(c.getDirection())
                .streamUrl(c.getStreamUrl())
                .status(c.getStatus())
                .intersectionId(c.getIntersection() != null ? c.getIntersection().getId() : null)
                .intersectionName(c.getIntersection() != null ? c.getIntersection().getName() : "Unassigned")
                .createdAt(c.getCreatedAt())
                .build();
    }
}
