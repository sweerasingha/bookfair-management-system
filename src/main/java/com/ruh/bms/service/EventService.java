package com.ruh.bms.service;

import com.ruh.bms.dto.EventResponse;
import com.ruh.bms.model.Event;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EventService {
    @Transactional(readOnly = true)
    List<EventResponse> getAllEvents();

    @Transactional(readOnly = true)
    List<EventResponse> getUpcomingEvents();

    @Transactional(readOnly = true)
    List<EventResponse> getOpenForRegistrationEvents();

    @Transactional(readOnly = true)
    EventResponse getEventById(Long id);

    @Transactional
    EventResponse createEvent(Event event);

    @Transactional
    EventResponse updateEvent(Long id, Event eventDetails);

    @Transactional
    void deleteEvent(Long id);
}
