package com.blueline.backend.dto.ambulance;

import jakarta.validation.constraints.NotBlank;

public record CreateAmbulanceRequest(
        @NotBlank String code,
        String model,
        Integer year
) {}
