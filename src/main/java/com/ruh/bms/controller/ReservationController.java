package com.ruh.bms.controller;

import com.ruh.bms.dto.ApiResponse;
import com.ruh.bms.dto.ReservationRequest;
import com.ruh.bms.dto.ReservationResponse;
import com.ruh.bms.dto.UpdateGenresRequest;
import com.ruh.bms.security.UserPrincipal;
import com.ruh.bms.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    @PreAuthorize("hasAnyRole('VENDOR', 'PUBLISHER')")
    public ResponseEntity<ApiResponse> createReservation(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody ReservationRequest request) {
        ReservationResponse reservation = reservationService.createReservation(currentUser.getId(), request);
        return new ResponseEntity<>(
                new ApiResponse(true, "Reservation created successfully", reservation),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/my-reservations")
    public ResponseEntity<ApiResponse> getMyReservations(@AuthenticationPrincipal UserPrincipal currentUser) {
        List<ReservationResponse> reservations = reservationService.getUserReservations(currentUser.getId());
        return ResponseEntity.ok(
                new ApiResponse(true, "Reservations retrieved successfully", reservations)
        );
    }
