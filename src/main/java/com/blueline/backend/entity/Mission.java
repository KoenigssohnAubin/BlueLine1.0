package com.blueline.backend.entity;

import com.blueline.backend.entity.enums.MissionPriority;
import com.blueline.backend.entity.enums.MissionStatus;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "missions")
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MissionStatus status = MissionStatus.EN_ATTENTE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MissionPriority priority;

    @Column(nullable = false)
    private String type;

    private String patientName;
    private Integer patientAge;
    private String patientCondition;

    @Column(nullable = false)
    private String pickupAddress;
    private Double pickupLat;
    private Double pickupLng;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_hospital_id")
    private Hospital destinationHospital;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ambulance_id")
    private Ambulance ambulance;

    private Instant dispatchTime;
    private Instant arrivalTime;

    private Integer etaMinutes;
    private Double distanceKm;
    private Integer durationMinutes;

    @ElementCollection
    @CollectionTable(name = "mission_route_points", joinColumns = @JoinColumn(name = "mission_id"))
    @OrderColumn(name = "point_order")
    private List<RoutePoint> aiRoute = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "mission_notes", joinColumns = @JoinColumn(name = "mission_id"))
    @Column(name = "note", length = 500)
    private List<String> alerts = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public MissionStatus getStatus() { return status; }
    public void setStatus(MissionStatus status) { this.status = status; }

    public MissionPriority getPriority() { return priority; }
    public void setPriority(MissionPriority priority) { this.priority = priority; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public Integer getPatientAge() { return patientAge; }
    public void setPatientAge(Integer patientAge) { this.patientAge = patientAge; }

    public String getPatientCondition() { return patientCondition; }
    public void setPatientCondition(String patientCondition) { this.patientCondition = patientCondition; }

    public String getPickupAddress() { return pickupAddress; }
    public void setPickupAddress(String pickupAddress) { this.pickupAddress = pickupAddress; }

    public Double getPickupLat() { return pickupLat; }
    public void setPickupLat(Double pickupLat) { this.pickupLat = pickupLat; }

    public Double getPickupLng() { return pickupLng; }
    public void setPickupLng(Double pickupLng) { this.pickupLng = pickupLng; }

    public Hospital getDestinationHospital() { return destinationHospital; }
    public void setDestinationHospital(Hospital destinationHospital) { this.destinationHospital = destinationHospital; }

    public Ambulance getAmbulance() { return ambulance; }
    public void setAmbulance(Ambulance ambulance) { this.ambulance = ambulance; }

    public Instant getDispatchTime() { return dispatchTime; }
    public void setDispatchTime(Instant dispatchTime) { this.dispatchTime = dispatchTime; }

    public Instant getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(Instant arrivalTime) { this.arrivalTime = arrivalTime; }

    public Integer getEtaMinutes() { return etaMinutes; }
    public void setEtaMinutes(Integer etaMinutes) { this.etaMinutes = etaMinutes; }

    public Double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(Double distanceKm) { this.distanceKm = distanceKm; }

    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }

    public List<RoutePoint> getAiRoute() { return aiRoute; }
    public void setAiRoute(List<RoutePoint> aiRoute) { this.aiRoute = aiRoute; }

    public List<String> getAlerts() { return alerts; }
    public void setAlerts(List<String> alerts) { this.alerts = alerts; }
}
