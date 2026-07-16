package com.blueline.backend.dto.user;

import java.time.Instant;

public record UserDto(
        Long id,
        String username,
        String role,
        String name,
        String badge,
        String phone,
        String email,
        String status,
        String vehicleCode,
        Instant lastLogin
) {}
