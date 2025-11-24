package com.ruh.bms.controller;

import com.ruh.bms.dto.ApiResponse;
import com.ruh.bms.dto.StallResponse;
import com.ruh.bms.model.Stall;
import com.ruh.bms.service.StallService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stalls")
@RequiredArgsConstructor
public class StallController {

    private final StallService stallService;

    @GetMapping("/event/{eventId}")
    public ResponseEntity<ApiResponse> getStallsByEvent(@PathVariable Long eventId) {
        List<StallResponse> stalls = stallService.getStallsByEvent(eventId);
        return ResponseEntity.ok(
                new ApiResponse(true, "Stalls retrieved successfully", stalls)
        );
    }

    @GetMapping("/event/{eventId}/available")
    public ResponseEntity<ApiResponse> getAvailableStallsByEvent(@PathVariable Long eventId) {
        List<StallResponse> stalls = stallService.getAvailableStallsByEvent(eventId);
        return ResponseEntity.ok(
                new ApiResponse(true, "Available stalls retrieved successfully", stalls)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getStallById(@PathVariable Long id) {
        StallResponse stall = stallService.getStallById(id);
        return ResponseEntity.ok(
                new ApiResponse(true, "Stall retrieved successfully", stall)
        );
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ORGANIZER')")
    public ResponseEntity<ApiResponse> createStall(@Valid @RequestBody Stall stall) {
        StallResponse createdStall = stallService.createStall(stall);
        return new ResponseEntity<>(
                new ApiResponse(true, "Stall created successfully", createdStall),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ORGANIZER')")
    public ResponseEntity<ApiResponse> updateStall(@PathVariable Long id, @Valid @RequestBody Stall stall) {
        StallResponse updatedStall = stallService.updateStall(id, stall);
        return ResponseEntity.ok(
                new ApiResponse(true, "Stall updated successfully", updatedStall)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ORGANIZER')")
    public ResponseEntity<ApiResponse> deleteStall(@PathVariable Long id) {
        stallService.deleteStall(id);
        return ResponseEntity.ok(
                new ApiResponse(true, "Stall deleted successfully")
        );
    }
}
