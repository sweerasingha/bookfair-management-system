package com.ruh.bms.repository;

import com.ruh.bms.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT DISTINCT r FROM Reservation r LEFT JOIN FETCH r.genres WHERE r.user.id = :userId")
    List<Reservation> findByUserIdWithGenres(@Param("userId") Long userId);

    @Query("SELECT DISTINCT r FROM Reservation r LEFT JOIN FETCH r.genres WHERE r.event.id = :eventId")
    List<Reservation> findByEventIdWithGenres(@Param("eventId") Long eventId);

    @Query("SELECT r FROM Reservation r LEFT JOIN FETCH r.genres WHERE r.id = :id")
    Optional<Reservation> findByIdWithGenres(@Param("id") Long id);

    @Query("SELECT r FROM Reservation r LEFT JOIN FETCH r.genres WHERE r.reservationCode = :code")
    Optional<Reservation> findByReservationCodeWithGenres(@Param("code") String code);

    List<Reservation> findByUserId(Long userId);

    List<Reservation> findByEventId(Long eventId);

    List<Reservation> findByUserIdAndEventId(Long userId, Long eventId);

    Optional<Reservation> findByReservationCode(String reservationCode);

    Optional<Reservation> findByQrCode(String qrCode);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.user.id = :userId AND r.event.id = :eventId AND r.status != 'CANCELLED'")
    Long countActiveReservationsByUserAndEvent(@Param("userId") Long userId, @Param("eventId") Long eventId);

    List<Reservation> findByStatus(Reservation.ReservationStatus status);
}
