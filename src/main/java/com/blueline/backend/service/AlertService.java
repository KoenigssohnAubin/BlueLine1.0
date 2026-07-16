package com.blueline.backend.service;

import com.blueline.backend.dto.alert.AlertDto;
import com.blueline.backend.dto.alert.CreateAlertRequest;
import com.blueline.backend.entity.Alert;
import com.blueline.backend.entity.User;
import com.blueline.backend.entity.enums.AlertSeverity;
import com.blueline.backend.entity.enums.AlertType;
import com.blueline.backend.exception.BadRequestException;
import com.blueline.backend.exception.ResourceNotFoundException;
import com.blueline.backend.repository.AlertRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AlertService {

    private final AlertRepository alertRepository;

    public AlertService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    @Transactional(readOnly = true)
    public List<AlertDto> findAll() {
        return alertRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional
    public AlertDto create(CreateAlertRequest request, User reportedBy) {
        Alert alert = new Alert();
        alert.setType(parseType(request.type()));
        alert.setSeverity(parseSeverity(request.severity()));
        alert.setMessage(request.message());
        alert.setLocation(request.location());
        alert.setReportedBy(reportedBy);
        return toDto(alertRepository.save(alert));
    }

    @Transactional
    public AlertDto dismiss(Long id) {
        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found: " + id));
        alert.setActive(false);
        return toDto(alertRepository.save(alert));
    }

    private AlertType parseType(String type) {
        try {
            return AlertType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid alert type: " + type);
        }
    }

    private AlertSeverity parseSeverity(String severity) {
        try {
            return AlertSeverity.valueOf(severity.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid alert severity: " + severity);
        }
    }

    public AlertDto toDto(Alert alert) {
        return new AlertDto(
                alert.getId(),
                alert.getType().name(),
                alert.getSeverity().name(),
                alert.getMessage(),
                alert.getLocation(),
                alert.getCreatedAt(),
                alert.isActive(),
                alert.getReportedBy() != null ? alert.getReportedBy().getName() : null
        );
    }
}
