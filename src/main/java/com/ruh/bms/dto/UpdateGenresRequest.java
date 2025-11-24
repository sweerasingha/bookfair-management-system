package com.ruh.bms.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateGenresRequest {

    @NotNull(message = "Genre IDs cannot be null")
    @NotEmpty(message = "At least one genre must be selected")
    private List<Long> genreIds;
}
