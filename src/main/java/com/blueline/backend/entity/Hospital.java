package com.blueline.backend.entity;

import com.blueline.backend.entity.enums.HospitalStatus;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hospitals")
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String address;

    private Double lat;

    private Double lng;

    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HospitalStatus status = HospitalStatus.DISPONIBLE;

    @ElementCollection
    @CollectionTable(name = "hospital_specialties", joinColumns = @JoinColumn(name = "hospital_id"))
    @Column(name = "specialty")
    private List<String> specialties = new ArrayList<>();

    private Integer score;

    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HospitalDepartment> departments = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Double getLat() { return lat; }
    public void setLat(Double lat) { this.lat = lat; }

    public Double getLng() { return lng; }
    public void setLng(Double lng) { this.lng = lng; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public HospitalStatus getStatus() { return status; }
    public void setStatus(HospitalStatus status) { this.status = status; }

    public List<String> getSpecialties() { return specialties; }
    public void setSpecialties(List<String> specialties) { this.specialties = specialties; }

    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }

    public List<HospitalDepartment> getDepartments() { return departments; }
    public void setDepartments(List<HospitalDepartment> departments) { this.departments = departments; }
}
