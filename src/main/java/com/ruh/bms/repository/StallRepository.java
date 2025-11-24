package com.ruh.bms.repository;

import com.ruh.bms.model.Stall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StallRepository extends JpaRepository<Stall, Long> {
    
    List<Stall> findByEventId(Long eventId);
    
    List<Stall> findByEventIdAndAvailableTrue(Long eventId);
    
    List<Stall> findByEventIdAndSize(Long eventId, Stall.StallSize size);
    
    Optional<Stall> findByEventIdAndStallNumber(Long eventId, String stallNumber);
    
    @Query("SELECT s FROM Stall s WHERE s.event.id = :eventId AND s.available = true ORDER BY s.size, s.stallNumber")
    List<Stall> findAvailableStallsByEventOrderedBySizeAndNumber(@Param("eventId") Long eventId);
    
    @Query("SELECT s FROM Stall s JOIN FETCH s.event WHERE s.id = :id")
    Optional<Stall> findByIdWithEvent(@Param("id") Long id);
    
    @Query("SELECT s FROM Stall s JOIN FETCH s.event WHERE s.event.id = :eventId")
    List<Stall> findByEventIdWithEvent(@Param("eventId") Long eventId);
    
    @Query("SELECT COUNT(s) FROM Stall s WHERE s.event.id = :eventId")
    Long countByEventId(@Param("eventId") Long eventId);
    
    @Query("SELECT COUNT(s) FROM Stall s WHERE s.event.id = :eventId AND s.available = true")
    Long countAvailableByEventId(@Param("eventId") Long eventId);
}
