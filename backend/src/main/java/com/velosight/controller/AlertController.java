package com.velosight.controller;

import com.velosight.dto.AlertDto;
import com.velosight.dto.AlertStatusUpdateRequest;
import com.velosight.service.AlertService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    @Autowired
    private AlertService alertService;

    @GetMapping
    public ResponseEntity<List<AlertDto>> getAllAlerts() {
        return ResponseEntity.ok(alertService.getAllAlerts());
    }

    @PutMapping("/{id}/acknowledge")
    public ResponseEntity<AlertDto> acknowledgeAlert(@PathVariable("id") Long id) {
        return ResponseEntity.ok(alertService.updateAlertStatus(id, "ACKNOWLEDGED"));
    }

    @PutMapping("/{id}/resolve")
    public ResponseEntity<AlertDto> resolveAlert(@PathVariable("id") Long id) {
        return ResponseEntity.ok(alertService.updateAlertStatus(id, "RESOLVED"));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<AlertDto> updateAlertStatus(@PathVariable("id") Long id, @Valid @RequestBody AlertStatusUpdateRequest request) {
        return ResponseEntity.ok(alertService.updateAlertStatus(id, request.getStatus()));
    }
}
