package com.velosight.repository;

import com.velosight.entity.Camera;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CameraRepository extends JpaRepository<Camera, Long> {
    Optional<Camera> findByCameraIdStr(String cameraIdStr);
    List<Camera> findByIntersectionId(Long intersectionId);
}
