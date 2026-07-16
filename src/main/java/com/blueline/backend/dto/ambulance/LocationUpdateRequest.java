package com.blueline.backend.dto.ambulance;

import jakarta.validation.constraints.NotNull;

public record LocationUpdateRequest(
        @NotNull Double lat,
        @NotNull Double lng
) {}
