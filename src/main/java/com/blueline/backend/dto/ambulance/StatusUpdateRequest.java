package com.blueline.backend.dto.ambulance;

import jakarta.validation.constraints.NotBlank;

public record StatusUpdateRequest(
        @NotBlank String status
) {}
