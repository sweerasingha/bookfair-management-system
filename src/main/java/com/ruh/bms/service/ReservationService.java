package com.ruh.bms.service;

import com.ruh.bms.dto.ReservationRequest;
import com.ruh.bms.dto.ReservationResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ReservationService {

    @Transactional
    ReservationResponse createReservation(Long userId, ReservationRequest request);

    @Transactional(readOnly = true)
    List<ReservationResponse> getUserReservations(Long userId);

    @Transactional(readOnly = true)
    List<ReservationResponse> getEventReservations(Long eventId);

    @Transactional(readOnly = true)
    ReservationResponse getReservationById(Long id);

    @Transactional(readOnly = true)
    ReservationResponse getReservationByCode(String code);

    @Transactional
    void cancelReservation(Long id, Long userId);

    @Transactional
    void checkInReservation(String reservationCode);

    @Transactional
    ReservationResponse updateReservationGenres(Long reservationId, Long userId, List<Long> genreIds);

    @Transactional(readOnly = true)
    byte[] generateQRCodeImage(Long reservationId, Long userId);
}
