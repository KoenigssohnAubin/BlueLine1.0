package com.blueline.backend.service;

import com.blueline.backend.dto.mission.*;
import com.blueline.backend.entity.Ambulance;
import com.blueline.backend.entity.Hospital;
import com.blueline.backend.entity.Mission;
import com.blueline.backend.entity.enums.AmbulanceStatus;
import com.blueline.backend.entity.enums.MissionPriority;
import com.blueline.backend.entity.enums.MissionStatus;
import com.blueline.backend.exception.BadRequestException;
import com.blueline.backend.exception.ResourceNotFoundException;
import com.blueline.backend.repository.AmbulanceRepository;
import com.blueline.backend.repository.HospitalRepository;
import com.blueline.backend.repository.MissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.Year;
import java.util.List;

@Service
public class MissionService {

    private final MissionRepository missionRepository;
    private final AmbulanceRepository ambulanceRepository;
    private final HospitalRepository hospitalRepository;

    public MissionService(MissionRepository missionRepository, AmbulanceRepository ambulanceRepository,
                           HospitalRepository hospitalRepository) {
        this.missionRepository = missionRepository;
        this.ambulanceRepository = ambulanceRepository;
        this.hospitalRepository = hospitalRepository;
    }

    @Transactional(readOnly = true)
    public List<MissionDto> findAll(String status) {
        List<Mission> missions = status != null
                ? missionRepository.findByStatus(parseStatus(status))
                : missionRepository.findAll();
        return missions.stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public MissionDto findById(Long id) {
        return toDto(getOrThrow(id));
    }

    @Transactional(readOnly = true)
    public MissionDto findActive() {
        return missionRepository.findFirstByStatus(MissionStatus.EN_COURS)
                .map(this::toDto)
                .orElse(null);
    }

    @Transactional
    public MissionDto create(CreateMissionRequest request) {
        Mission mission = new Mission();
        mission.setCode(generateCode());
        mission.setStatus(MissionStatus.EN_ATTENTE);
        mission.setPriority(parsePriority(request.priority()));
        mission.setType(request.type());
        mission.setPatientName(request.patient().name());
        mission.setPatientAge(request.patient().age());
        mission.setPatientCondition(request.patient().condition());
        mission.setPickupAddress(request.pickup().address());
        mission.setPickupLat(request.pickup().lat());
        mission.setPickupLng(request.pickup().lng());

        if (request.destinationHospitalId() != null) {
            Hospital hospital = hospitalRepository.findById(request.destinationHospitalId())
                    .orElseThrow(() -> new ResourceNotFoundException("Hospital not found: " + request.destinationHospitalId()));
            mission.setDestinationHospital(hospital);
        }

        return toDto(missionRepository.save(mission));
    }

    @Transactional
    public MissionDto accept(Long id, AcceptMissionRequest request) {
        Mission mission = getOrThrow(id);
        if (mission.getStatus() != MissionStatus.EN_ATTENTE) {
            throw new BadRequestException("Mission is not awaiting dispatch: " + mission.getCode());
        }

        Ambulance ambulance = ambulanceRepository.findByCode(request.ambulanceCode())
                .orElseThrow(() -> new ResourceNotFoundException("Ambulance not found: " + request.ambulanceCode()));

        mission.setAmbulance(ambulance);
        mission.setStatus(MissionStatus.EN_COURS);
        mission.setDispatchTime(Instant.now());

        ambulance.setStatus(AmbulanceStatus.EN_MISSION);
        ambulanceRepository.save(ambulance);

        return toDto(missionRepository.save(mission));
    }

    @Transactional
    public MissionDto complete(Long id) {
        Mission mission = getOrThrow(id);
        if (mission.getStatus() != MissionStatus.EN_COURS) {
            throw new BadRequestException("Mission is not in progress: " + mission.getCode());
        }

        Instant now = Instant.now();
        mission.setStatus(MissionStatus.TERMINEE);
        mission.setArrivalTime(now);
        if (mission.getDispatchTime() != null) {
            mission.setDurationMinutes((int) Duration.between(mission.getDispatchTime(), now).toMinutes());
        }

        Ambulance ambulance = mission.getAmbulance();
        if (ambulance != null) {
            ambulance.setStatus(AmbulanceStatus.DISPONIBLE);
            ambulanceRepository.save(ambulance);
        }

        return toDto(missionRepository.save(mission));
    }

    private Mission getOrThrow(Long id) {
        return missionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mission not found: " + id));
    }

    private MissionStatus parseStatus(String status) {
        try {
            return MissionStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid mission status: " + status);
        }
    }

    private MissionPriority parsePriority(String priority) {
        try {
            return MissionPriority.valueOf(priority.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid mission priority: " + priority);
        }
    }

    private String generateCode() {
        int year = Year.now().getValue();
        long sequence = missionRepository.count() + 1;
        String candidate;
        do {
            candidate = String.format("MIS-%d-%04d", year, sequence++);
        } while (missionRepository.existsByCode(candidate));
        return candidate;
    }

    public MissionDto toDto(Mission mission) {
        Hospital hospital = mission.getDestinationHospital();
        DestinationDto destination = hospital != null
                ? new DestinationDto(hospital.getId(), hospital.getName(), hospital.getAddress(), hospital.getLat(), hospital.getLng())
                : null;

        Ambulance ambulance = mission.getAmbulance();

        List<RoutePointDto> route = mission.getAiRoute().stream()
                .map(p -> new RoutePointDto(p.getLat(), p.getLng()))
                .toList();

        return new MissionDto(
                mission.getId(),
                mission.getCode(),
                mission.getStatus().name(),
                mission.getPriority().name(),
                mission.getType(),
                new PatientDto(mission.getPatientName(), mission.getPatientAge(), mission.getPatientCondition()),
                new LocationDto(mission.getPickupAddress(), mission.getPickupLat(), mission.getPickupLng()),
                destination,
                ambulance != null ? ambulance.getCode() : null,
                ambulance != null && ambulance.getDriver() != null ? ambulance.getDriver().getName() : null,
                mission.getDispatchTime(),
                mission.getArrivalTime(),
                mission.getEtaMinutes(),
                mission.getDistanceKm(),
                mission.getDurationMinutes(),
                route,
                List.copyOf(mission.getAlerts())
        );
    }
}
