package com.blueline.backend.repository;

import com.blueline.backend.entity.HospitalDepartment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalDepartmentRepository extends JpaRepository<HospitalDepartment, Long> {
}
