package com.velosight.controller;

import com.velosight.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/traffic")
    public ResponseEntity<Map<String, Object>> getTrafficAnalytics() {
        return ResponseEntity.ok(analyticsService.getTrafficAnalytics());
    }

    @GetMapping("/density")
    public ResponseEntity<Map<String, Object>> getDensityAnalytics() {
        return ResponseEntity.ok(analyticsService.getTrafficAnalytics());
    }

    @GetMapping("/congestion")
    public ResponseEntity<Map<String, Object>> getCongestionAnalytics() {
        return ResponseEntity.ok(analyticsService.getTrafficAnalytics());
    }

    @GetMapping("/vehicles")
    public ResponseEntity<Map<String, Object>> getVehicleAnalytics() {
        return ResponseEntity.ok(analyticsService.getTrafficAnalytics());
    }
}
