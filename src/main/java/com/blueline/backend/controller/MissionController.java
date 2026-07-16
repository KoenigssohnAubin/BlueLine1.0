package com.blueline.backend.controller;

import com.blueline.backend.dto.mission.*;
import com.blueline.backend.service.MissionService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/missions")
public class MissionController {

    private final MissionService missionService;

    public MissionController(MissionService missionService) {
        this.missionService = missionService;
    }

    @GetMapping
    public List<MissionDto> findAll(@RequestParam(required = false) String status) {
        return missionService.findAll(status);
    }

    @GetMapping("/active")
    public MissionDto findActive() {
        return missionService.findActive();
    }

    @GetMapping("/{id}")
    public MissionDto findById(@PathVariable Long id) {
        return missionService.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('CONTROL', 'ADMIN')")
    public MissionDto create(@Valid @RequestBody CreateMissionRequest request) {
        return missionService.create(request);
    }

    @PatchMapping("/{id}/accept")
    @PreAuthorize("hasAnyRole('AMBULANCIER', 'CONTROL', 'ADMIN')")
    public MissionDto accept(@PathVariable Long id, @Valid @RequestBody AcceptMissionRequest request) {
        return missionService.accept(id, request);
    }

    @PatchMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('AMBULANCIER', 'CONTROL', 'ADMIN')")
    public MissionDto complete(@PathVariable Long id) {
        return missionService.complete(id);
    }
}
