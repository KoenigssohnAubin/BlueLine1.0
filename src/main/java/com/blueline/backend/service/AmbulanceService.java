package com.blueline.backend.service;

import com.blueline.backend.dto.ambulance.*;
import com.blueline.backend.entity.Ambulance;
import com.blueline.backend.entity.User;
import com.blueline.backend.entity.enums.AmbulanceStatus;
import com.blueline.backend.exception.BadRequestException;
import com.blueline.backend.exception.ResourceNotFoundException;
import com.blueline.backend.repository.AmbulanceRepository;
import com.blueline.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class AmbulanceService {

    private final AmbulanceRepository ambulanceRepository;
    private final UserRepository userRepository;

    public AmbulanceService(AmbulanceRepository ambulanceRepository, UserRepository userRepository) {
        this.ambulanceRepository = ambulanceRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<AmbulanceDto> findAll() {
        return ambulanceRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public AmbulanceDto findById(Long id) {
        return toDto(getOrThrow(id));
    }

    @Transactional
    public AmbulanceDto create(CreateAmbulanceRequest request) {
        if (ambulanceRepository.existsByCode(request.code())) {
            throw new BadRequestException("Ambulance code already exists: " + request.code());
        }
        Ambulance ambulance = new Ambulance();
        ambulance.setCode(request.code());
        ambulance.setModel(request.model());
        ambulance.setYear(request.year());
        ambulance.setStatus(AmbulanceStatus.DISPONIBLE);
        ambulance.setLastUpdate(Instant.now());
        return toDto(ambulanceRepository.save(ambulance));
    }

    @Transactional
    public AmbulanceDto update(Long id, UpdateAmbulanceRequest request) {
        Ambulance ambulance = getOrThrow(id);
        if (request.model() != null) ambulance.setModel(request.model());
        if (request.year() != null) ambulance.setYear(request.year());
        if (request.status() != null) ambulance.setStatus(parseStatus(request.status()));
        if (request.driverUsername() != null) {
            if (request.driverUsername().isBlank()) {
                ambulance.setDriver(null);
            } else {
                User driver = userRepository.findByUsername(request.driverUsername())
                        .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.driverUsername()));
                ambulance.setDriver(driver);
            }
        }
        return toDto(ambulanceRepository.save(ambulance));
    }

    @Transactional
    public AmbulanceDto updateLocation(Long id, LocationUpdateRequest request) {
        Ambulance ambulance = getOrThrow(id);
        ambulance.setLat(request.lat());
        ambulance.setLng(request.lng());
        ambulance.setLastUpdate(Instant.now());
        return toDto(ambulanceRepository.save(ambulance));
    }

    @Transactional
    public AmbulanceDto updateStatus(Long id, StatusUpdateRequest request) {
        Ambulance ambulance = getOrThrow(id);
        ambulance.setStatus(parseStatus(request.status()));
        return toDto(ambulanceRepository.save(ambulance));
    }

    @Transactional
    public void delete(Long id) {
        if (!ambulanceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Ambulance not found: " + id);
        }
        ambulanceRepository.deleteById(id);
    }

    private Ambulance getOrThrow(Long id) {
        return ambulanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ambulance not found: " + id));
    }

    private AmbulanceStatus parseStatus(String status) {
        try {
            return AmbulanceStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid ambulance status: " + status);
        }
    }

    public AmbulanceDto toDto(Ambulance ambulance) {
        return new AmbulanceDto(
                ambulance.getId(),
                ambulance.getCode(),
                ambulance.getModel(),
                ambulance.getYear(),
                ambulance.getStatus().name(),
                ambulance.getDriver() != null ? ambulance.getDriver().getName() : null,
                ambulance.getLat(),
                ambulance.getLng(),
                ambulance.getLastUpdate()
        );
    }
}
