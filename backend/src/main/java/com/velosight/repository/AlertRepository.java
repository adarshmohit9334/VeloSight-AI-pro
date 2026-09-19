package com.velosight.repository;

import com.velosight.entity.Alert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findTop5ByOrderByTimestampDesc();
    List<Alert> findByStatus(String status);
    Long countByStatus(String status);
    Page<Alert> findByStatus(String status, Pageable pageable);
}
