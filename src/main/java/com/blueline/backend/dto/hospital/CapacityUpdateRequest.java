package com.blueline.backend.dto.hospital;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CapacityUpdateRequest(
        @NotBlank String department,
        @NotNull Integer available,
        @NotNull Integer total
) {}
