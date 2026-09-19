package com.velosight.service;

import com.velosight.entity.AnalysisSession;
import com.velosight.repository.AnalysisSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AnalyticsService {

    @Autowired
    private AnalysisSessionRepository analysisSessionRepository;

    public Map<String, Object> getTrafficAnalytics() {
        List<AnalysisSession> sessions = analysisSessionRepository.findAll();

        Map<String, Object> response = new HashMap<>();
        
        List<Map<String, Object>> hourlyData = new ArrayList<>();
        String[] hours = {"06:00", "08:00", "10:00", "12:00", "14:00", "16:00", "18:00", "20:00"};
        int[] baselineVehicles = {140, 420, 310, 250, 280, 490, 560, 310};
        int[] baselineDensity = {25, 78, 55, 42, 48, 85, 92, 58};
        int[] baselineCongestion = {15, 68, 45, 30, 38, 79, 88, 50};

        for (int i = 0; i < hours.length; i++) {
            Map<String, Object> point = new HashMap<>();
            point.put("time", hours[i]);
            point.put("volume", baselineVehicles[i]);
            point.put("density", baselineDensity[i]);
            point.put("congestion", baselineCongestion[i]);
            point.put("avgSpeed", 55 - (baselineCongestion[i] * 0.4));
            hourlyData.add(point);
        }

        response.put("hourlyTrends", hourlyData);
        response.put("totalAnalysesProcessed", sessions.size());
        response.put("averageSystemDensity", 64.5);
        response.put("peakTrafficHour", "18:00 - 19:00");
        return response;
    }
}
