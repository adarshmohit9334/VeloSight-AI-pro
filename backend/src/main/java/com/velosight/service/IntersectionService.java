package com.velosight.service;

import com.velosight.dto.IntersectionDto;
import com.velosight.entity.Intersection;
import com.velosight.repository.IntersectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IntersectionService {

    @Autowired
    private IntersectionRepository intersectionRepository;

    public List<IntersectionDto> getAllIntersections() {
        return intersectionRepository.findAll().stream().map(this::convertToDto).toList();
    }

    public IntersectionDto createIntersection(IntersectionDto dto) {
        Intersection intersection = Intersection.builder()
                .name(dto.getName())
                .location(dto.getLocation())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .status(dto.getStatus() != null ? dto.getStatus() : "ACTIVE")
                .cameraCount(dto.getCameraCount() != null ? dto.getCameraCount() : 0)
                .build();

        return convertToDto(intersectionRepository.save(intersection));
    }

    public IntersectionDto updateIntersection(Long id, IntersectionDto dto) {
        Intersection intersection = intersectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Intersection not found with id: " + id));

        if (dto.getName() != null) intersection.setName(dto.getName());
        if (dto.getLocation() != null) intersection.setLocation(dto.getLocation());
        if (dto.getLatitude() != null) intersection.setLatitude(dto.getLatitude());
        if (dto.getLongitude() != null) intersection.setLongitude(dto.getLongitude());
        if (dto.getStatus() != null) intersection.setStatus(dto.getStatus());

        return convertToDto(intersectionRepository.save(intersection));
    }

    public void deleteIntersection(Long id) {
        intersectionRepository.deleteById(id);
    }

    private IntersectionDto convertToDto(Intersection i) {
        return IntersectionDto.builder()
                .id(i.getId())
                .name(i.getName())
                .location(i.getLocation())
                .latitude(i.getLatitude())
                .longitude(i.getLongitude())
                .status(i.getStatus())
                .cameraCount(i.getCameraCount())
                .createdAt(i.getCreatedAt())
                .build();
    }
}
