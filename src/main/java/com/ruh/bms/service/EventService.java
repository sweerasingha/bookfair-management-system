package com.ruh.bms.service;

import com.ruh.bms.dto.EventResponse;
import com.ruh.bms.exception.ResourceNotFoundException;
import com.ruh.bms.model.Event;
import com.ruh.bms.repository.EventRepository;
import com.ruh.bms.repository.StallRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {

    private final EventRepository eventRepository;
    private final StallRepository stallRepository;

    @Transactional(readOnly = true)
    public List<EventResponse> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EventResponse> getUpcomingEvents() {
        return eventRepository.findByStatus(Event.EventStatus.UPCOMING).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EventResponse> getOpenForRegistrationEvents() {
        return eventRepository.findByRegistrationOpenTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EventResponse getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", id));
        return mapToResponse(event);
    }

    @Transactional
    public EventResponse createEvent(Event event) {
        Event savedEvent = eventRepository.save(event);
        log.info("Event created: {}", savedEvent.getName());
        return mapToResponse(savedEvent);
    }

    @Transactional
    public EventResponse updateEvent(Long id, Event eventDetails) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", id));

        event.setName(eventDetails.getName());
        event.setDescription(eventDetails.getDescription());
        event.setStartDate(eventDetails.getStartDate());
        event.setEndDate(eventDetails.getEndDate());
        event.setVenue(eventDetails.getVenue());
        event.setStatus(eventDetails.getStatus());
        event.setRegistrationOpen(eventDetails.getRegistrationOpen());

        Event updatedEvent = eventRepository.save(event);
        log.info("Event updated: {}", updatedEvent.getName());
        return mapToResponse(updatedEvent);
    }

    @Transactional
    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", id));
        eventRepository.delete(event);
        log.info("Event deleted: {}", event.getName());
    }

    private EventResponse mapToResponse(Event event) {
        Long totalStalls = stallRepository.countByEventId(event.getId());
        Long availableStalls = stallRepository.countAvailableByEventId(event.getId());

        return EventResponse.builder()
                .id(event.getId())
                .name(event.getName())
                .description(event.getDescription())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .venue(event.getVenue())
                .status(event.getStatus().name())
                .registrationOpen(event.getRegistrationOpen())
                .totalStalls(totalStalls.intValue())
                .availableStalls(availableStalls.intValue())
                .build();
    }
}