package com.velosight.controller;

import com.velosight.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/traffic")
    public ResponseEntity<Map<String, Object>> getTrafficReport(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate) {
        return ResponseEntity.ok(reportService.generateTrafficReport(startDate, endDate));
    }

    @GetMapping("/congestion")
    public ResponseEntity<Map<String, Object>> getCongestionReport(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate) {
        return ResponseEntity.ok(reportService.generateTrafficReport(startDate, endDate));
    }
}
