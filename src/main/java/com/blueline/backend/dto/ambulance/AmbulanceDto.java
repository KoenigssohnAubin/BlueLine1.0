package com.blueline.backend.dto.ambulance;

import java.time.Instant;

public record AmbulanceDto(
        Long id,
        String code,
        String model,
        Integer year,
        String status,
        String driver,
        Double lat,
        Double lng,
        Instant lastUpdate
) {}
