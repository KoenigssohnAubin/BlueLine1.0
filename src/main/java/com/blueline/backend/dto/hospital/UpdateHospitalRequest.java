package com.blueline.backend.dto.hospital;

import java.util.List;

public record UpdateHospitalRequest(
        String name,
        String address,
        Double lat,
        Double lng,
        String phone,
        String status,
        List<String> specialties,
        Integer score
) {}
