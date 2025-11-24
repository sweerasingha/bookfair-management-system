package com.ruh.bms.repository;

import com.ruh.bms.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByStatus(Event.EventStatus status);

    List<Event> findByRegistrationOpenTrue();

    List<Event> findByStartDateBetween(LocalDate startDate, LocalDate endDate);
}