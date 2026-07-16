package com.blueline.backend.repository;

import com.blueline.backend.entity.Mission;
import com.blueline.backend.entity.enums.MissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface MissionRepository extends JpaRepository<Mission, Long> {
    List<Mission> findByStatus(MissionStatus status);
    Optional<Mission> findFirstByStatus(MissionStatus status);
    List<Mission> findByAmbulance_Code(String ambulanceCode);
    List<Mission> findByDispatchTimeAfter(Instant since);
    boolean existsByCode(String code);
}
