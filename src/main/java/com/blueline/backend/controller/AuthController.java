package com.blueline.backend.controller;

import com.blueline.backend.dto.auth.AuthResponse;
import com.blueline.backend.dto.auth.LoginRequest;
import com.blueline.backend.dto.user.UserDto;
import com.blueline.backend.security.UserPrincipal;
import com.blueline.backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/me")
    public UserDto me(@AuthenticationPrincipal UserPrincipal principal) {
        return authService.me(principal);
    }
}
