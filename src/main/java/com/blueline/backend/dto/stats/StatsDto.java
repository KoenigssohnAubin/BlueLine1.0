package com.blueline.backend.dto.stats;

public record StatsDto(
        String period,
        long missions,
        long completed,
        long inProgress,
        String avgTime,
        double successRate
) {}
