package com.blueline.backend.dto.mission;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateMissionRequest(
        @NotBlank String type,
        @NotBlank String priority,
        @Valid @NotNull PatientDto patient,
        @Valid @NotNull LocationDto pickup,
        Long destinationHospitalId
) {}
