package com.ruh.bms.controller;

import com.ruh.bms.dto.ApiResponse;
import com.ruh.bms.dto.EventResponse;
import com.ruh.bms.model.Event;
import com.ruh.bms.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllEvents() {
        List<EventResponse> events = eventService.getAllEvents();
        return ResponseEntity.ok(
                new ApiResponse(true, "Events retrieved successfully", events)
        );
    }

    @GetMapping("/upcoming")
    public ResponseEntity<ApiResponse> getUpcomingEvents() {
        List<EventResponse> events = eventService.getUpcomingEvents();
        return ResponseEntity.ok(
                new ApiResponse(true, "Upcoming events retrieved successfully", events)
        );
    }

    @GetMapping("/open-registration")
    public ResponseEntity<ApiResponse> getOpenForRegistrationEvents() {
        List<EventResponse> events = eventService.getOpenForRegistrationEvents();
        return ResponseEntity.ok(
                new ApiResponse(true, "Events open for registration retrieved successfully", events)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getEventById(@PathVariable Long id) {
        EventResponse event = eventService.getEventById(id);
        return ResponseEntity.ok(
                new ApiResponse(true, "Event retrieved successfully", event)
        );
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ORGANIZER')")
    public ResponseEntity<ApiResponse> createEvent(@Valid @RequestBody Event event) {
        EventResponse createdEvent = eventService.createEvent(event);
        return new ResponseEntity<>(
                new ApiResponse(true, "Event created successfully", createdEvent),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ORGANIZER')")
    public ResponseEntity<ApiResponse> updateEvent(@PathVariable Long id, @Valid @RequestBody Event event) {
        EventResponse updatedEvent = eventService.updateEvent(id, event);
        return ResponseEntity.ok(
                new ApiResponse(true, "Event updated successfully", updatedEvent)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ORGANIZER')")
    public ResponseEntity<ApiResponse> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.ok(
                new ApiResponse(true, "Event deleted successfully")
        );
    }
}