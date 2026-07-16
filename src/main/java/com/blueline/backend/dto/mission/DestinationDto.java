package com.blueline.backend.dto.mission;

public record DestinationDto(
        Long hospitalId,
        String hospital,
        String address,
        Double lat,
        Double lng
) {}
