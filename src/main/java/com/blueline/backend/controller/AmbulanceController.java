package com.blueline.backend.controller;

import com.blueline.backend.dto.ambulance.*;
import com.blueline.backend.service.AmbulanceService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ambulances")
public class AmbulanceController {

    private final AmbulanceService ambulanceService;

    public AmbulanceController(AmbulanceService ambulanceService) {
        this.ambulanceService = ambulanceService;
    }

    @GetMapping
    public List<AmbulanceDto> findAll() {
        return ambulanceService.findAll();
    }

    @GetMapping("/{id}")
    public AmbulanceDto findById(@PathVariable Long id) {
        return ambulanceService.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public AmbulanceDto create(@Valid @RequestBody CreateAmbulanceRequest request) {
        return ambulanceService.create(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public AmbulanceDto update(@PathVariable Long id, @RequestBody UpdateAmbulanceRequest request) {
        return ambulanceService.update(id, request);
    }

    @PatchMapping("/{id}/location")
    @PreAuthorize("hasAnyRole('AMBULANCIER', 'CONTROL', 'ADMIN')")
    public AmbulanceDto updateLocation(@PathVariable Long id, @Valid @RequestBody LocationUpdateRequest request) {
        return ambulanceService.updateLocation(id, request);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('AMBULANCIER', 'CONTROL', 'ADMIN')")
    public AmbulanceDto updateStatus(@PathVariable Long id, @Valid @RequestBody StatusUpdateRequest request) {
        return ambulanceService.updateStatus(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        ambulanceService.delete(id);
    }
}
