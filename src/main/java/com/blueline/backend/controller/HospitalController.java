package com.blueline.backend.controller;

import com.blueline.backend.dto.hospital.*;
import com.blueline.backend.service.HospitalService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hospitals")
public class HospitalController {

    private final HospitalService hospitalService;

    public HospitalController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @GetMapping
    public List<HospitalDto> findAll() {
        return hospitalService.findAll();
    }

    @GetMapping("/{id}")
    public HospitalDto findById(@PathVariable Long id) {
        return hospitalService.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public HospitalDto create(@Valid @RequestBody CreateHospitalRequest request) {
        return hospitalService.create(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public HospitalDto update(@PathVariable Long id, @RequestBody UpdateHospitalRequest request) {
        return hospitalService.update(id, request);
    }

    @PatchMapping("/{id}/capacity")
    @PreAuthorize("hasAnyRole('ADMIN', 'CONTROL')")
    public HospitalDto updateCapacity(@PathVariable Long id, @Valid @RequestBody CapacityUpdateRequest request) {
        return hospitalService.updateCapacity(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        hospitalService.delete(id);
    }
}
