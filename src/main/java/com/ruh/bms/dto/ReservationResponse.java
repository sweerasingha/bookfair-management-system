package com.ruh.bms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponse {

    private Long id;
    private String reservationCode;
    private String qrCode;
    private String status;
    private Long userId;
    private String userName;
    private Long eventId;
    private String eventName;
    private Long stallId;
    private String stallNumber;
    private Set<String> genres;
    private LocalDateTime createdAt;
    private LocalDateTime confirmedAt;
}
