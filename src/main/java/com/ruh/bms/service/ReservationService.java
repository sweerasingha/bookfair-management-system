package com.ruh.bms.service;

import com.ruh.bms.dto.ReservationRequest;
import com.ruh.bms.dto.ReservationResponse;
import com.ruh.bms.exception.BadRequestException;
import com.ruh.bms.exception.ResourceNotFoundException;
import com.ruh.bms.model.*;
import com.ruh.bms.repository.*;
import com.ruh.bms.util.QRCodeGenerator;
import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final StallRepository stallRepository;
    private final GenreRepository genreRepository;
    private final QRCodeGenerator qrCodeGenerator;
    private final EmailService emailService;

    private static final int MAX_RESERVATIONS_PER_USER = 3;

    @Transactional
    public ReservationResponse createReservation(Long userId, ReservationRequest request) {
        // Validate user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        // Validate event
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", request.getEventId()));

        // Check if registration is open
        if (!event.getRegistrationOpen()) {
            throw new BadRequestException("Registration is not open for this event");
        }

        // Validate stall
        Stall stall = stallRepository.findById(request.getStallId())
                .orElseThrow(() -> new ResourceNotFoundException("Stall", "id", request.getStallId()));

        // Check if stall is available
        if (!stall.getAvailable()) {
            throw new BadRequestException("This stall is not available");
        }

        // Check if stall belongs to the event
        if (!stall.getEvent().getId().equals(event.getId())) {
            throw new BadRequestException("Stall does not belong to the selected event");
        }

        // Check reservation limit
        Long userReservationCount = reservationRepository.countActiveReservationsByUserAndEvent(userId, event.getId());
        if (userReservationCount >= MAX_RESERVATIONS_PER_USER) {
            throw new BadRequestException("You have reached the maximum of " + MAX_RESERVATIONS_PER_USER + " reservations for this event");
        }

        // Create reservation
        Reservation reservation = Reservation.builder()
                .user(user)
                .event(event)
                .stall(stall)
                .status(Reservation.ReservationStatus.CONFIRMED)
                .confirmedAt(LocalDateTime.now())
                .build();

        // Add genres if provided
        if (request.getGenreIds() != null && !request.getGenreIds().isEmpty()) {
            Set<Genre> genres = request.getGenreIds().stream()
                    .map(genreId -> genreRepository.findById(genreId)
                            .orElseThrow(() -> new ResourceNotFoundException("Genre", "id", genreId)))
                    .collect(Collectors.toSet());
            reservation.setGenres(genres);
        }

        // Mark stall as unavailable
        stall.setAvailable(false);
        stallRepository.save(stall);

        // Save reservation
        reservation = reservationRepository.save(reservation);
        log.info("Reservation created: {} for user: {}", reservation.getReservationCode(), user.getUsername());

        // Generate QR code
        try {
            String qrCodeData = reservation.getReservationCode();
            String qrCodeBase64 = qrCodeGenerator.generateQRCodeBase64(qrCodeData);
            reservation.setQrCode(qrCodeBase64);
            reservation.setQrCodeSent(true);
            reservation = reservationRepository.save(reservation);

            // Send confirmation email asynchronously
            emailService.sendReservationConfirmationEmail(
                    user.getEmail(),
                    user.getFullName(),
                    reservation.getReservationCode(),
                    event.getName(),
                    stall.getStallNumber(),
                    qrCodeBase64
            );

            log.info("QR code generated and email sent for reservation: {}", reservation.getReservationCode());
        } catch (WriterException | IOException e) {
            log.error("Failed to generate QR code for reservation: {}", reservation.getReservationCode(), e);
        }

        return mapToResponse(reservation);
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> getUserReservations(Long userId) {
        List<Reservation> reservations = reservationRepository.findByUserIdWithGenres(userId);
        return reservations.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> getEventReservations(Long eventId) {
        List<Reservation> reservations = reservationRepository.findByEventIdWithGenres(eventId);
        return reservations.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReservationResponse getReservationById(Long id) {
        Reservation reservation = reservationRepository.findByIdWithGenres(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", "id", id));
        return mapToResponse(reservation);
    }

    @Transactional(readOnly = true)
    public ReservationResponse getReservationByCode(String code) {
        Reservation reservation = reservationRepository.findByReservationCodeWithGenres(code)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", "code", code));
        return mapToResponse(reservation);
    }

    @Transactional
    public void cancelReservation(Long id, Long userId) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", "id", id));

        // Check if user owns the reservation
        if (!reservation.getUser().getId().equals(userId)) {
            throw new BadRequestException("You are not authorized to cancel this reservation");
        }

        // Cancel reservation
        reservation.setStatus(Reservation.ReservationStatus.CANCELLED);
        reservation.setCancelledAt(LocalDateTime.now());
        reservationRepository.save(reservation);

        // Mark stall as available
        Stall stall = reservation.getStall();
        stall.setAvailable(true);
        stallRepository.save(stall);

        log.info("Reservation cancelled: {}", reservation.getReservationCode());
    }

    @Transactional
    public void checkInReservation(String reservationCode) {
        Reservation reservation = reservationRepository.findByReservationCode(reservationCode)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", "reservationCode", reservationCode));

        if (reservation.getStatus() != Reservation.ReservationStatus.CONFIRMED) {
            throw new BadRequestException("Reservation is not in confirmed status");
        }

        reservation.setStatus(Reservation.ReservationStatus.CHECKED_IN);
        reservationRepository.save(reservation);

        log.info("Reservation checked in: {}", reservation.getReservationCode());
    }

    @Transactional
    public ReservationResponse updateReservationGenres(Long reservationId, Long userId, List<Long> genreIds) {
        // Validate reservation exists
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", "id", reservationId));

        // Check if user owns the reservation
        if (!reservation.getUser().getId().equals(userId)) {
            throw new BadRequestException("You are not authorized to update this reservation");
        }

        // Check if reservation is active (not cancelled)
        if (reservation.getStatus() == Reservation.ReservationStatus.CANCELLED) {
            throw new BadRequestException("Cannot update genres for a cancelled reservation");
        }

        // Validate and fetch genres
        if (genreIds == null || genreIds.isEmpty()) {
            throw new BadRequestException("At least one genre must be selected");
        }

        Set<Genre> genres = genreIds.stream()
                .map(genreId -> genreRepository.findById(genreId)
                        .orElseThrow(() -> new ResourceNotFoundException("Genre", "id", genreId)))
                .collect(Collectors.toSet());

        // Update genres
        reservation.setGenres(genres);
        reservation = reservationRepository.save(reservation);

        log.info("Genres updated for reservation: {}", reservation.getReservationCode());

        return mapToResponse(reservation);
    }

    private ReservationResponse mapToResponse(Reservation reservation) {
        Set<String> genreNames = reservation.getGenres().stream()
                .map(Genre::getName)
                .collect(Collectors.toSet());

        return ReservationResponse.builder()
                .id(reservation.getId())
                .reservationCode(reservation.getReservationCode())
                .qrCode(reservation.getQrCode())
                .status(reservation.getStatus().name())
                .userId(reservation.getUser().getId())
                .userName(reservation.getUser().getFullName())
                .eventId(reservation.getEvent().getId())
                .eventName(reservation.getEvent().getName())
                .stallId(reservation.getStall().getId())
                .stallNumber(reservation.getStall().getStallNumber())
                .genres(genreNames)
                .createdAt(reservation.getCreatedAt())
                .confirmedAt(reservation.getConfirmedAt())
                .build();
    }
}
