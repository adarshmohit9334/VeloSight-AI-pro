package com.velosight.repository;

import com.velosight.entity.TrafficMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface TrafficMetricRepository extends JpaRepository<TrafficMetric, Long> {
    List<TrafficMetric> findTop24ByOrderByTimestampDesc();
    List<TrafficMetric> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}
