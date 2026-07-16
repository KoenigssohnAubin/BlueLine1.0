package com.blueline.backend.controller;

import com.blueline.backend.dto.stats.StatsDto;
import com.blueline.backend.service.StatsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats")
@PreAuthorize("hasAnyRole('ADMIN', 'CONTROL')")
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping
    public StatsDto stats(@RequestParam(defaultValue = "today") String period) {
        return statsService.compute(period);
    }
}
