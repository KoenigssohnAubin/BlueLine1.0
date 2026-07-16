package com.blueline.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "hospital_departments")
public class HospitalDepartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer available;

    @Column(nullable = false)
    private Integer total;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Hospital getHospital() { return hospital; }
    public void setHospital(Hospital hospital) { this.hospital = hospital; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getAvailable() { return available; }
    public void setAvailable(Integer available) { this.available = available; }

    public Integer getTotal() { return total; }
    public void setTotal(Integer total) { this.total = total; }
}
