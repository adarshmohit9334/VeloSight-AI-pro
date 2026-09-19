package com.velosight.repository;

import com.velosight.entity.AnalysisSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface AnalysisSessionRepository extends JpaRepository<AnalysisSession, Long> {
    Optional<AnalysisSession> findByAnalysisIdStr(String analysisIdStr);
    List<AnalysisSession> findTop10ByOrderByCreatedAtDesc();
    
    @Query("SELECT SUM(a.totalVehicles) FROM AnalysisSession a WHERE a.status = 'COMPLETED'")
    Long getTotalVehicleCountSum();

    Page<AnalysisSession> findByStatus(String status, Pageable pageable);
}
