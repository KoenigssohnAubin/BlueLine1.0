package com.blueline.backend.dto.user;

import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(
        @NotBlank String username,
        @NotBlank String password,
        @NotBlank String role,
        @NotBlank String name,
        String badge,
        String phone,
        String email
) {}
