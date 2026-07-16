package com.blueline.backend.dto.alert;

import java.time.Instant;

public record AlertDto(
        Long id,
        String type,
        String severity,
        String message,
        String location,
        Instant createdAt,
        boolean active,
        String reportedBy
) {}
