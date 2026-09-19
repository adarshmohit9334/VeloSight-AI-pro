package com.velosight.controller;

import com.velosight.dto.IntersectionDto;
import com.velosight.service.IntersectionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/intersections")
public class IntersectionController {

    @Autowired
    private IntersectionService intersectionService;

    @GetMapping
    public ResponseEntity<List<IntersectionDto>> getAllIntersections() {
        return ResponseEntity.ok(intersectionService.getAllIntersections());
    }

    @PostMapping
    public ResponseEntity<IntersectionDto> createIntersection(@Valid @RequestBody IntersectionDto dto) {
        return ResponseEntity.ok(intersectionService.createIntersection(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IntersectionDto> updateIntersection(@PathVariable("id") Long id, @RequestBody IntersectionDto dto) {
        return ResponseEntity.ok(intersectionService.updateIntersection(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIntersection(@PathVariable("id") Long id) {
        intersectionService.deleteIntersection(id);
        return ResponseEntity.noContent().build();
    }
}
