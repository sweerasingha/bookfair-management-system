package com.ruh.bms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StallResponse {

    private Long id;
    private String stallNumber;
    private Long eventId;
    private String size;
    private BigDecimal pricePerStall;
    private Boolean available;
    private String location;
}
