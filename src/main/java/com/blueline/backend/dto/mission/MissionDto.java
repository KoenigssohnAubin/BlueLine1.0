package com.blueline.backend.dto.mission;

import java.time.Instant;
import java.util.List;

public record MissionDto(
        Long id,
        String code,
        String status,
        String priority,
        String type,
        PatientDto patient,
        LocationDto pickup,
        DestinationDto destination,
        String ambulanceCode,
        String driver,
        Instant dispatchTime,
        Instant arrivalTime,
        Integer etaMinutes,
        Double distanceKm,
        Integer durationMinutes,
        List<RoutePointDto> aiRoute,
        List<String> alerts
) {}
