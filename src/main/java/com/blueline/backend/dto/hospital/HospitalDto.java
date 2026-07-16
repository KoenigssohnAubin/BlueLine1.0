package com.blueline.backend.dto.hospital;

import java.util.List;

public record HospitalDto(
        Long id,
        String name,
        String address,
        Double lat,
        Double lng,
        String phone,
        String status,
        List<String> specialties,
        Integer score,
        List<DepartmentDto> departments
) {}
