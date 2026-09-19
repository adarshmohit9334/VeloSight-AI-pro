package com.velosight.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/settings")
public class SettingsController {

    private static Map<String, Object> settingsMap = new HashMap<>();

    static {
        settingsMap.put("yoloConfidenceThreshold", 0.40);
        settingsMap.put("frameSkipRate", 2);
        settingsMap.put("densityThresholdLow", 25.0);
        settingsMap.put("densityThresholdHigh", 75.0);
        settingsMap.put("maxFileUploadSizeMb", 500);
        settingsMap.put("demoMode", false);
        settingsMap.put("aiServiceUrl", "http://localhost:8000");
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getSettings() {
        return ResponseEntity.ok(settingsMap);
    }

    @PutMapping
    public ResponseEntity<Map<String, Object>> updateSettings(@RequestBody Map<String, Object> newSettings) {
        settingsMap.putAll(newSettings);
        return ResponseEntity.ok(settingsMap);
    }
}
