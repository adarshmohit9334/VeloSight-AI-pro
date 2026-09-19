package com.velosight.service;

import com.velosight.dto.AlertDto;
import com.velosight.entity.Alert;
import com.velosight.repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertService {

    @Autowired
    private AlertRepository alertRepository;

    public List<AlertDto> getAllAlerts() {
        return alertRepository.findAll().stream().map(this::convertToDto).toList();
    }

    public AlertDto updateAlertStatus(Long id, String status) {
        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alert not found with id: " + id));

        alert.setStatus(status);
        return convertToDto(alertRepository.save(alert));
    }

    private AlertDto convertToDto(Alert a) {
        return AlertDto.builder()
                .id(a.getId())
                .type(a.getType())
                .severity(a.getSeverity())
                .message(a.getMessage())
                .timestamp(a.getTimestamp())
                .intersectionName(a.getIntersection() != null ? a.getIntersection().getName() : "Central Avenue Junction")
                .cameraName(a.getCamera() != null ? a.getCamera().getCameraName() : "Camera 01")
                .status(a.getStatus())
                .build();
    }
}
