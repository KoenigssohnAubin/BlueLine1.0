package com.blueline.backend.dto.user;

public record UpdateUserRequest(
        String name,
        String badge,
        String phone,
        String email,
        String status
) {}
