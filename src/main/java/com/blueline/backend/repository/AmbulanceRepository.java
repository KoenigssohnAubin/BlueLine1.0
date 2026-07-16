package com.blueline.backend.repository;

import com.blueline.backend.entity.Ambulance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AmbulanceRepository extends JpaRepository<Ambulance, Long> {
    Optional<Ambulance> findByCode(String code);
    boolean existsByCode(String code);
    Optional<Ambulance> findByDriver_Id(Long driverId);
}
