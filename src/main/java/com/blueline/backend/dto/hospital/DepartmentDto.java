package com.blueline.backend.dto.hospital;

public record DepartmentDto(
        String name,
        Integer available,
        Integer total
) {}
