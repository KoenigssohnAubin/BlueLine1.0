package com.blueline.backend.service;

import com.blueline.backend.dto.hospital.*;
import com.blueline.backend.entity.Hospital;
import com.blueline.backend.entity.HospitalDepartment;
import com.blueline.backend.entity.enums.HospitalStatus;
import com.blueline.backend.exception.BadRequestException;
import com.blueline.backend.exception.ResourceNotFoundException;
import com.blueline.backend.repository.HospitalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class HospitalService {

    private final HospitalRepository hospitalRepository;

    public HospitalService(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    @Transactional(readOnly = true)
    public List<HospitalDto> findAll() {
        return hospitalRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public HospitalDto findById(Long id) {
        return toDto(getOrThrow(id));
    }

    @Transactional
    public HospitalDto create(CreateHospitalRequest request) {
        Hospital hospital = new Hospital();
        hospital.setName(request.name());
        hospital.setAddress(request.address());
        hospital.setLat(request.lat());
        hospital.setLng(request.lng());
        hospital.setPhone(request.phone());
        hospital.setStatus(HospitalStatus.DISPONIBLE);
        hospital.setSpecialties(request.specialties() != null ? request.specialties() : new ArrayList<>());
        hospital.setScore(request.score());

        if (request.departments() != null) {
            List<HospitalDepartment> departments = new ArrayList<>();
            for (DepartmentDto d : request.departments()) {
                HospitalDepartment dept = new HospitalDepartment();
                dept.setHospital(hospital);
                dept.setName(d.name());
                dept.setAvailable(d.available());
                dept.setTotal(d.total());
                departments.add(dept);
            }
            hospital.setDepartments(departments);
        }

        return toDto(hospitalRepository.save(hospital));
    }

    @Transactional
    public HospitalDto update(Long id, UpdateHospitalRequest request) {
        Hospital hospital = getOrThrow(id);
        if (request.name() != null) hospital.setName(request.name());
        if (request.address() != null) hospital.setAddress(request.address());
        if (request.lat() != null) hospital.setLat(request.lat());
        if (request.lng() != null) hospital.setLng(request.lng());
        if (request.phone() != null) hospital.setPhone(request.phone());
        if (request.status() != null) hospital.setStatus(parseStatus(request.status()));
        if (request.specialties() != null) hospital.setSpecialties(request.specialties());
        if (request.score() != null) hospital.setScore(request.score());
        return toDto(hospitalRepository.save(hospital));
    }

    @Transactional
    public HospitalDto updateCapacity(Long id, CapacityUpdateRequest request) {
        Hospital hospital = getOrThrow(id);
        HospitalDepartment department = hospital.getDepartments().stream()
                .filter(d -> d.getName().equalsIgnoreCase(request.department()))
                .findFirst()
                .orElseGet(() -> {
                    HospitalDepartment newDept = new HospitalDepartment();
                    newDept.setHospital(hospital);
                    newDept.setName(request.department());
                    hospital.getDepartments().add(newDept);
                    return newDept;
                });
        department.setAvailable(request.available());
        department.setTotal(request.total());
        return toDto(hospitalRepository.save(hospital));
    }

    @Transactional
    public void delete(Long id) {
        if (!hospitalRepository.existsById(id)) {
            throw new ResourceNotFoundException("Hospital not found: " + id);
        }
        hospitalRepository.deleteById(id);
    }

    private Hospital getOrThrow(Long id) {
        return hospitalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found: " + id));
    }

    private HospitalStatus parseStatus(String status) {
        try {
            return HospitalStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid hospital status: " + status);
        }
    }

    public HospitalDto toDto(Hospital hospital) {
        List<DepartmentDto> departments = hospital.getDepartments().stream()
                .map(d -> new DepartmentDto(d.getName(), d.getAvailable(), d.getTotal()))
                .toList();

        return new HospitalDto(
                hospital.getId(),
                hospital.getName(),
                hospital.getAddress(),
                hospital.getLat(),
                hospital.getLng(),
                hospital.getPhone(),
                hospital.getStatus().name(),
                List.copyOf(hospital.getSpecialties()),
                hospital.getScore(),
                departments
        );
    }
}
