package com.ruh.bms.service;

import com.ruh.bms.dto.StallResponse;
import com.ruh.bms.exception.BadRequestException;
import com.ruh.bms.exception.ResourceNotFoundException;
import com.ruh.bms.model.Event;
import com.ruh.bms.model.Stall;
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
public class StallServiceImpl implements StallService {

    private final StallRepository stallRepository;
    private final EventRepository eventRepository;

    @Transactional(readOnly = true)
    @Override
    public List<StallResponse> getStallsByEvent(Long eventId) {
        return stallRepository.findByEventIdWithEvent(eventId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<StallResponse> getAvailableStallsByEvent(Long eventId) {
        return stallRepository.findAvailableStallsByEventOrderedBySizeAndNumber(eventId).stream()
                .map(stall -> mapToResponseWithEventId(stall, eventId))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public StallResponse getStallById(Long id) {
        Stall stall = stallRepository.findByIdWithEvent(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stall", "id", id));
        return mapToResponse(stall);
    }

    @Transactional
    @Override
    public StallResponse createStall(Stall stall) {
        // Validate event exists
        Event event = eventRepository.findById(stall.getEvent().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", stall.getEvent().getId()));

        // Check if stall number already exists for this event
        if (stallRepository.findByEventIdAndStallNumber(event.getId(), stall.getStallNumber()).isPresent()) {
            throw new BadRequestException("Stall number " + stall.getStallNumber() + " already exists for this event");
        }

        stall.setEvent(event);
        Stall savedStall = stallRepository.save(stall);
        log.info("Stall created: {} for event: {}", savedStall.getStallNumber(), event.getName());
        return mapToResponse(savedStall);
    }

    @Transactional
    @Override
    public StallResponse updateStall(Long id, Stall stallDetails) {
        Stall stall = stallRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stall", "id", id));

        stall.setStallNumber(stallDetails.getStallNumber());
        stall.setSize(stallDetails.getSize());
        stall.setPricePerStall(stallDetails.getPricePerStall());
        stall.setAvailable(stallDetails.getAvailable());
        stall.setLocation(stallDetails.getLocation());

        Stall updatedStall = stallRepository.save(stall);
        log.info("Stall updated: {}", updatedStall.getStallNumber());
        return mapToResponse(updatedStall);
    }

    @Transactional
    @Override
    public void deleteStall(Long id) {
        Stall stall = stallRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stall", "id", id));
        stallRepository.delete(stall);
        log.info("Stall deleted: {}", stall.getStallNumber());
    }

    private StallResponse mapToResponse(Stall stall) {
        return StallResponse.builder()
                .id(stall.getId())
                .stallNumber(stall.getStallNumber())
                .eventId(stall.getEvent().getId())
                .size(stall.getSize().name())
                .pricePerStall(stall.getPricePerStall())
                .available(stall.getAvailable())
                .location(stall.getLocation())
                .build();
    }
    
    private StallResponse mapToResponseWithEventId(Stall stall, Long eventId) {
        return StallResponse.builder()
                .id(stall.getId())
                .stallNumber(stall.getStallNumber())
                .eventId(eventId)
                .size(stall.getSize().name())
                .pricePerStall(stall.getPricePerStall())
                .available(stall.getAvailable())
                .location(stall.getLocation())
                .build();
    }
}
