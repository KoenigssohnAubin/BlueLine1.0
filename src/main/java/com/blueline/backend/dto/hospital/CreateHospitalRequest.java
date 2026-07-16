package com.blueline.backend.dto.hospital;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateHospitalRequest(
        @NotBlank String name,
        String address,
        @NotNull Double lat,
        @NotNull Double lng,
        String phone,
        List<String> specialties,
        Integer score,
        List<DepartmentDto> departments
) {}
