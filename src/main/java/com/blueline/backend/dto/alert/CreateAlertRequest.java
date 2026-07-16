package com.blueline.backend.dto.alert;

import jakarta.validation.constraints.NotBlank;

public record CreateAlertRequest(
        @NotBlank String type,
        @NotBlank String severity,
        @NotBlank String message,
        String location
) {}
