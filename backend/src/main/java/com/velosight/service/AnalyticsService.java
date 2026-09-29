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
        // Removed hardcoded baseline data. Return empty or real data when available.

        response.put("hourlyTrends", hourlyData);
        response.put("totalAnalysesProcessed", sessions.size());
        response.put("averageSystemDensity", 0.0);
        response.put("peakTrafficHour", "N/A");
        return response;
    }
}
