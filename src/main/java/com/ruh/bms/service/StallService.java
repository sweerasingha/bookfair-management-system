package com.ruh.bms.service;

import com.ruh.bms.dto.StallResponse;
import com.ruh.bms.model.Stall;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface StallService {
    @Transactional(readOnly = true)
    List<StallResponse> getStallsByEvent(Long eventId);

    @Transactional(readOnly = true)
    List<StallResponse> getAvailableStallsByEvent(Long eventId);

    @Transactional(readOnly = true)
    StallResponse getStallById(Long id);

    @Transactional
    StallResponse createStall(Stall stall);

    @Transactional
    StallResponse updateStall(Long id, Stall stallDetails);

    @Transactional
    void deleteStall(Long id);
}
