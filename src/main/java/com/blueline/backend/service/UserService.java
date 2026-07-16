package com.blueline.backend.service;

import com.blueline.backend.dto.user.CreateUserRequest;
import com.blueline.backend.dto.user.UpdateUserRequest;
import com.blueline.backend.dto.user.UserDto;
import com.blueline.backend.entity.Ambulance;
import com.blueline.backend.entity.User;
import com.blueline.backend.entity.enums.Role;
import com.blueline.backend.entity.enums.UserStatus;
import com.blueline.backend.exception.BadRequestException;
import com.blueline.backend.exception.ResourceNotFoundException;
import com.blueline.backend.repository.AmbulanceRepository;
import com.blueline.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AmbulanceRepository ambulanceRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, AmbulanceRepository ambulanceRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.ambulanceRepository = ambulanceRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        return userRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public UserDto findById(Long id) {
        return toDto(getOrThrow(id));
    }

    @Transactional
    public UserDto create(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new BadRequestException("Username already taken: " + request.username());
        }
        User user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(parseRole(request.role()));
        user.setName(request.name());
        user.setBadge(request.badge());
        user.setPhone(request.phone());
        user.setEmail(request.email());
        return toDto(userRepository.save(user));
    }

    @Transactional
    public UserDto update(Long id, UpdateUserRequest request) {
        User user = getOrThrow(id);
        if (request.name() != null) user.setName(request.name());
        if (request.badge() != null) user.setBadge(request.badge());
        if (request.phone() != null) user.setPhone(request.phone());
        if (request.email() != null) user.setEmail(request.email());
        if (request.status() != null) user.setStatus(UserStatus.valueOf(request.status().toUpperCase()));
        return toDto(userRepository.save(user));
    }

    @Transactional
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found: " + id);
        }
        userRepository.deleteById(id);
    }

    private User getOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
    }

    private Role parseRole(String role) {
        try {
            return Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid role: " + role);
        }
    }

    public UserDto toDto(User user) {
        String vehicleCode = ambulanceRepository.findByDriver_Id(user.getId())
                .map(Ambulance::getCode)
                .orElse(null);

        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getRole().name(),
                user.getName(),
                user.getBadge(),
                user.getPhone(),
                user.getEmail(),
                user.getStatus().name(),
                vehicleCode,
                user.getLastLogin()
        );
    }
}
