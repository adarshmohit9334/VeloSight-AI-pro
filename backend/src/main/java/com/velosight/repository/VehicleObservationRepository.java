package com.velosight.repository;

import com.velosight.entity.AnalysisSession;
import com.velosight.entity.VehicleObservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleObservationRepository extends JpaRepository<VehicleObservation, Long> {
    List<VehicleObservation> findByAnalysisSession(AnalysisSession session);
    Optional<VehicleObservation> findByAnalysisSessionAndTrackId(AnalysisSession session, Integer trackId);
    void deleteByAnalysisSession(AnalysisSession session);
}
