package com.blueline.backend.controller;

import com.blueline.backend.dto.alert.AlertDto;
import com.blueline.backend.dto.alert.CreateAlertRequest;
import com.blueline.backend.security.UserPrincipal;
import com.blueline.backend.service.AlertService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping
    public List<AlertDto> findAll() {
        return alertService.findAll();
    }

    @PostMapping
    public AlertDto create(@Valid @RequestBody CreateAlertRequest request, @AuthenticationPrincipal UserPrincipal principal) {
        return alertService.create(request, principal.getUser());
    }

    @PatchMapping("/{id}/dismiss")
    public AlertDto dismiss(@PathVariable Long id) {
        return alertService.dismiss(id);
    }
}
