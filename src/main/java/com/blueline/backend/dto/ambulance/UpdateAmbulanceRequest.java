package com.blueline.backend.dto.ambulance;

public record UpdateAmbulanceRequest(
        String model,
        Integer year,
        String status,
        String driverUsername
) {}
