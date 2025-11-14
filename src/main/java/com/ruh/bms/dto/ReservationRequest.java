package com.ruh.bms.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationRequest {

    @NotNull(message = "Event ID is required")
    private Long eventId;

    @NotNull(message = "Stall ID is required")
    private Long stallId;

    private Set<Long> genreIds;
}
