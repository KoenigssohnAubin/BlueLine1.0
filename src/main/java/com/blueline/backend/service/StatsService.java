package com.blueline.backend.service;

import com.blueline.backend.dto.stats.StatsDto;
import com.blueline.backend.entity.Mission;
import com.blueline.backend.entity.enums.MissionStatus;
import com.blueline.backend.exception.BadRequestException;
import com.blueline.backend.repository.MissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Locale;

@Service
public class StatsService {

    private final MissionRepository missionRepository;

    public StatsService(MissionRepository missionRepository) {
        this.missionRepository = missionRepository;
    }

    @Transactional(readOnly = true)
    public StatsDto compute(String period) {
        Instant since = periodStart(period);
        List<Mission> missions = missionRepository.findByDispatchTimeAfter(since);

        long completed = missions.stream().filter(m -> m.getStatus() == MissionStatus.TERMINEE).count();
        long inProgress = missions.stream().filter(m -> m.getStatus() == MissionStatus.EN_COURS).count();

        double avgMinutes = missions.stream()
                .filter(m -> m.getDurationMinutes() != null)
                .mapToInt(Mission::getDurationMinutes)
                .average()
                .orElse(0);

        double successRate = missions.isEmpty() ? 0 : (completed * 100.0) / missions.size();

        return new StatsDto(
                period,
                missions.size(),
                completed,
                inProgress,
                String.format(Locale.US, "%.1f min", avgMinutes),
                Math.round(successRate * 10) / 10.0
        );
    }

    private Instant periodStart(String period) {
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        return switch (period.toLowerCase()) {
            case "today" -> today.atStartOfDay(ZoneOffset.UTC).toInstant();
            case "week" -> today.minusDays(7).atStartOfDay(ZoneOffset.UTC).toInstant();
            case "month" -> today.minusDays(30).atStartOfDay(ZoneOffset.UTC).toInstant();
            default -> throw new BadRequestException("Invalid stats period: " + period);
        };
    }
}
