package com.blueline.backend.service;

import com.blueline.backend.dto.auth.AuthResponse;
import com.blueline.backend.dto.auth.LoginRequest;
import com.blueline.backend.entity.User;
import com.blueline.backend.repository.UserRepository;
import com.blueline.backend.security.JwtService;
import com.blueline.backend.security.UserPrincipal;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserService userService;

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService,
                        UserRepository userRepository, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new IllegalStateException("Authenticated user vanished: " + request.username()));

        user.setLastLogin(Instant.now());
        userRepository.save(user);

        String token = jwtService.generateToken(user.getUsername(), user.getRole().name());
        return new AuthResponse(token, userService.toDto(user));
    }

    @Transactional(readOnly = true)
    public com.blueline.backend.dto.user.UserDto me(UserPrincipal principal) {
        return userService.toDto(principal.getUser());
    }
}
