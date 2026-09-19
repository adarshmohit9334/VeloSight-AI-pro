package com.velosight.repository;

import com.velosight.entity.Intersection;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IntersectionRepository extends JpaRepository<Intersection, Long> {
    List<Intersection> findByStatus(String status);
}
