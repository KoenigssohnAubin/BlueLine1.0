package com.blueline.backend.dto.mission;

public record LocationDto(
        String address,
        Double lat,
        Double lng
) {}
