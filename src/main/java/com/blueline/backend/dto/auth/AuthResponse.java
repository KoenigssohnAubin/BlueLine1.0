package com.blueline.backend.dto.auth;

import com.blueline.backend.dto.user.UserDto;

public record AuthResponse(String token, UserDto user) {}
