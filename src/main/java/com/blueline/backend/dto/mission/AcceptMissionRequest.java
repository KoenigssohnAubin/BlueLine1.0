package com.blueline.backend.dto.mission;

import jakarta.validation.constraints.NotBlank;

public record AcceptMissionRequest(
        @NotBlank String ambulanceCode
) {}
