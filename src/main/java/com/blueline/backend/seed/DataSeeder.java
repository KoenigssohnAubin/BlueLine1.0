package com.blueline.backend.seed;

import com.blueline.backend.entity.*;
import com.blueline.backend.entity.enums.*;
import com.blueline.backend.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

/**
 * Seeds the London demo dataset (mirrors mobile/src/data/mockData.js) into an empty database
 * so the API is immediately usable with the app's existing demo accounts.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AmbulanceRepository ambulanceRepository;
    private final HospitalRepository hospitalRepository;
    private final MissionRepository missionRepository;
    private final AlertRepository alertRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, AmbulanceRepository ambulanceRepository,
                       HospitalRepository hospitalRepository, MissionRepository missionRepository,
                       AlertRepository alertRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.ambulanceRepository = ambulanceRepository;
        this.hospitalRepository = hospitalRepository;
        this.missionRepository = missionRepository;
        this.alertRepository = alertRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }

        Map<String, User> users = seedUsers();
        Map<String, Ambulance> ambulances = seedAmbulances(users);
        Map<String, Hospital> hospitals = seedHospitals();
        seedMissions(ambulances, hospitals);
        seedAlerts();
    }

    private Map<String, User> seedUsers() {
        User ambulancier = user("ambulancier", "demo123", Role.AMBULANCIER, "James Morgan", "LAS-2847", "+44 7700 900123", "j.morgan@blueline.co.uk");
        User admin = user("admin", "admin123", Role.ADMIN, "Sarah Clarke", "ADM-001", "+44 20 7946 0001", "s.clarke@blueline.co.uk");
        User control = user("control", "control123", Role.CONTROL, "Mark Davidson", "CTL-015", "+44 20 7946 0002", "m.davidson@blueline.co.uk");
        User mary = user("mary.lewis", "demo123", Role.AMBULANCIER, "Mary Lewis", "LAS-3012", "+44 7700 900124", "m.lewis@blueline.co.uk");
        User paul = user("paul.barnes", "demo123", Role.AMBULANCIER, "Paul Barnes", "LAS-2901", "+44 7700 900125", "p.barnes@blueline.co.uk");
        User lucy = user("lucy.parker", "demo123", Role.AMBULANCIER, "Lucy Parker", "LAS-3145", "+44 7700 900126", "l.parker@blueline.co.uk");
        lucy.setStatus(UserStatus.INACTIF);
        User anthony = user("anthony.gardner", "demo123", Role.AMBULANCIER, "Anthony Gardner", "LAS-3267", "+44 7700 900127", "a.gardner@blueline.co.uk");
        User cameron = user("cameron.ross", "demo123", Role.AMBULANCIER, "Cameron Ross", "LAS-2765", "+44 7700 900128", "c.ross@blueline.co.uk");

        List<User> saved = userRepository.saveAll(List.of(ambulancier, admin, control, mary, paul, lucy, anthony, cameron));

        return saved.stream().collect(java.util.stream.Collectors.toMap(User::getUsername, u -> u));
    }

    private User user(String username, String rawPassword, Role role, String name, String badge, String phone, String email) {
        User u = new User();
        u.setUsername(username);
        u.setPassword(passwordEncoder.encode(rawPassword));
        u.setRole(role);
        u.setName(name);
        u.setBadge(badge);
        u.setPhone(phone);
        u.setEmail(email);
        u.setStatus(UserStatus.ACTIF);
        return u;
    }

    private Map<String, Ambulance> seedAmbulances(Map<String, User> users) {
        Ambulance a1 = ambulance("LAS-E1-001", "Ford Transit Custom", 2023, AmbulanceStatus.EN_MISSION, users.get("ambulancier"), 51.5074, -0.1278);
        Ambulance a2 = ambulance("LAS-E1-002", "Mercedes Sprinter", 2022, AmbulanceStatus.DISPONIBLE, users.get("mary.lewis"), 51.5155, -0.0922);
        Ambulance a3 = ambulance("LAS-E1-003", "Ford Transit", 2021, AmbulanceStatus.EN_MISSION, users.get("paul.barnes"), 51.4994, -0.1245);
        Ambulance a4 = ambulance("LAS-NW1-001", "Mercedes Vito", 2023, AmbulanceStatus.MAINTENANCE, users.get("lucy.parker"), 51.5246, -0.1340);
        Ambulance a5 = ambulance("LAS-W1-001", "Ford Transit Custom", 2024, AmbulanceStatus.DISPONIBLE, users.get("anthony.gardner"), 51.5168, -0.1594);
        Ambulance a6 = ambulance("LAS-W2-001", "Mercedes Sprinter", 2022, AmbulanceStatus.EN_MISSION, users.get("cameron.ross"), 51.5169, -0.1745);

        List<Ambulance> saved = ambulanceRepository.saveAll(List.of(a1, a2, a3, a4, a5, a6));
        return saved.stream().collect(java.util.stream.Collectors.toMap(Ambulance::getCode, a -> a));
    }

    private Ambulance ambulance(String code, String model, int year, AmbulanceStatus status, User driver, double lat, double lng) {
        Ambulance a = new Ambulance();
        a.setCode(code);
        a.setModel(model);
        a.setYear(year);
        a.setStatus(status);
        a.setDriver(driver);
        a.setLat(lat);
        a.setLng(lng);
        a.setLastUpdate(Instant.now());
        return a;
    }

    private Map<String, Hospital> seedHospitals() {
        Hospital royalLondon = hospital("Royal London Hospital", "Whitechapel Road, London E1 1FR", 51.5189, -0.0596,
                "020 7377 7000", HospitalStatus.DISPONIBLE, List.of("A&E", "Cardiology", "Trauma", "Neurology"), 94,
                Map.of("Urgences", new int[]{4, 20}, "Cardiologie", new int[]{2, 15}, "Traumatologie", new int[]{6, 18}));

        Hospital stThomas = hospital("St Thomas' Hospital", "Westminster Bridge Road, SE1 7EH", 51.4988, -0.1174,
                "020 7188 7188", HospitalStatus.DISPONIBLE, List.of("A&E", "Haematology", "Dermatology", "Oncology"), 87,
                Map.of("Urgences", new int[]{8, 25}, "Cardiologie", new int[]{5, 20}, "Traumatologie", new int[]{3, 15}));

        Hospital kingsCollege = hospital("King's College Hospital", "Denmark Hill, London SE5 9RS", 51.4681, -0.0946,
                "020 3299 9000", HospitalStatus.SATURATION, List.of("A&E", "Neurology", "Cardiology", "Surgery"), 42,
                Map.of("Urgences", new int[]{1, 30}, "Cardiologie", new int[]{0, 25}, "Traumatologie", new int[]{8, 22}));

        Hospital ucl = hospital("University College Hospital", "235 Euston Road, London NW1 2BU", 51.5246, -0.1340,
                "020 3456 7890", HospitalStatus.DISPONIBLE, List.of("A&E", "Urology", "Gynaecology", "Surgery"), 78,
                Map.of("Urgences", new int[]{12, 22}, "Cardiologie", new int[]{7, 18}, "Traumatologie", new int[]{5, 14}));

        Hospital stMary = hospital("St Mary's Hospital", "Praed Street, Paddington, W2 1NY", 51.5169, -0.1745,
                "020 3312 6666", HospitalStatus.DISPONIBLE, List.of("A&E", "Cardiology", "Pulmonology", "Vascular Surgery"), 81,
                Map.of("Urgences", new int[]{9, 28}, "Cardiologie", new int[]{4, 20}, "Traumatologie", new int[]{7, 16}));

        List<Hospital> saved = hospitalRepository.saveAll(List.of(royalLondon, stThomas, kingsCollege, ucl, stMary));
        return saved.stream().collect(java.util.stream.Collectors.toMap(Hospital::getName, h -> h));
    }

    private Hospital hospital(String name, String address, double lat, double lng, String phone, HospitalStatus status,
                               List<String> specialties, int score, Map<String, int[]> departments) {
        Hospital h = new Hospital();
        h.setName(name);
        h.setAddress(address);
        h.setLat(lat);
        h.setLng(lng);
        h.setPhone(phone);
        h.setStatus(status);
        h.setSpecialties(specialties);
        h.setScore(score);

        departments.forEach((deptName, availableTotal) -> {
            HospitalDepartment dept = new HospitalDepartment();
            dept.setHospital(h);
            dept.setName(deptName);
            dept.setAvailable(availableTotal[0]);
            dept.setTotal(availableTotal[1]);
            h.getDepartments().add(dept);
        });

        return h;
    }

    private void seedMissions(Map<String, Ambulance> ambulances, Map<String, Hospital> hospitals) {
        LocalDate today = LocalDate.now(ZoneOffset.UTC);

        Mission m1 = new Mission();
        m1.setCode("MIS-2026-1847");
        m1.setStatus(MissionStatus.EN_COURS);
        m1.setPriority(MissionPriority.CRITIQUE);
        m1.setType("Cardiac Arrest");
        m1.setPatientName("Anonymous Patient");
        m1.setPatientAge(62);
        m1.setPatientCondition("Cardiorespiratory arrest");
        m1.setPickupAddress("14 Oxford Street, London W1D");
        m1.setPickupLat(51.5154);
        m1.setPickupLng(-0.1410);
        m1.setDestinationHospital(hospitals.get("Royal London Hospital"));
        m1.setAmbulance(ambulances.get("LAS-E1-001"));
        m1.setDispatchTime(atTime(today, 10, 38, 0));
        m1.setEtaMinutes(6);
        m1.setDistanceKm(3.7);
        m1.setAiRoute(List.of(
                new RoutePoint(51.5154, -0.1410),
                new RoutePoint(51.5160, -0.1200),
                new RoutePoint(51.5175, -0.0950),
                new RoutePoint(51.5185, -0.0700),
                new RoutePoint(51.5189, -0.0596)
        ));
        m1.setAlerts(List.of("Heavy traffic on Oxford Street — diversion suggested"));

        Mission m2 = new Mission();
        m2.setCode("MIS-2026-1848");
        m2.setStatus(MissionStatus.EN_ATTENTE);
        m2.setPriority(MissionPriority.HAUTE);
        m2.setType("Road Traffic Accident");
        m2.setPatientName("Mr. David Ahmed");
        m2.setPatientAge(34);
        m2.setPatientCondition("Suspected head trauma");
        m2.setPickupAddress("A1 Archway Road, London N19");
        m2.setPickupLat(51.5650);
        m2.setPickupLng(-0.1373);
        m2.setDestinationHospital(hospitals.get("University College Hospital"));
        m2.setEtaMinutes(11);
        m2.setDistanceKm(6.6);

        Mission m3 = new Mission();
        m3.setCode("MIS-2026-1849");
        m3.setStatus(MissionStatus.EN_ATTENTE);
        m3.setPriority(MissionPriority.NORMALE);
        m3.setType("General Illness");
        m3.setPatientName("Mrs. Margaret Wilson");
        m3.setPatientAge(78);
        m3.setPatientCondition("Severe hypoglycaemia");
        m3.setPickupAddress("8 Kensington Gardens Square, London W2");
        m3.setPickupLat(51.5117);
        m3.setPickupLng(-0.1898);
        m3.setDestinationHospital(hospitals.get("King's College Hospital"));
        m3.setAlerts(List.of("King's College Hospital at capacity — alternative recommended"));

        Mission m4 = new Mission();
        m4.setCode("MIS-2026-1842");
        m4.setStatus(MissionStatus.TERMINEE);
        m4.setPriority(MissionPriority.HAUTE);
        m4.setType("Stroke");
        m4.setPatientName("Mr. Robert White");
        m4.setPatientAge(71);
        m4.setPatientCondition("Ischaemic stroke");
        m4.setPickupAddress("22 Brick Lane, London E1");
        m4.setPickupLat(51.5222);
        m4.setPickupLng(-0.0718);
        m4.setDestinationHospital(hospitals.get("Royal London Hospital"));
        m4.setAmbulance(ambulances.get("LAS-E1-001"));
        m4.setDispatchTime(atTime(today, 8, 15, 0));
        m4.setArrivalTime(atTime(today, 8, 26, 0));
        m4.setDurationMinutes(11);
        m4.setDistanceKm(2.9);

        Mission m5 = new Mission();
        m5.setCode("MIS-2026-1838");
        m5.setStatus(MissionStatus.TERMINEE);
        m5.setPriority(MissionPriority.NORMALE);
        m5.setType("Fracture");
        m5.setPatientName("Mrs. Alice Brown");
        m5.setPatientAge(45);
        m5.setPatientCondition("Wrist fracture");
        m5.setPickupAddress("5 King's Road, London SW3");
        m5.setPickupLat(51.4878);
        m5.setPickupLng(-0.1595);
        m5.setDestinationHospital(hospitals.get("St Thomas' Hospital"));
        m5.setAmbulance(ambulances.get("LAS-E1-001"));
        m5.setDispatchTime(atTime(today, 6, 30, 0));
        m5.setArrivalTime(atTime(today, 6, 52, 0));
        m5.setDurationMinutes(22);
        m5.setDistanceKm(7.4);

        missionRepository.saveAll(List.of(m1, m2, m3, m4, m5));
    }

    private Instant atTime(LocalDate date, int hour, int minute, int second) {
        return date.atTime(LocalTime.of(hour, minute, second)).toInstant(ZoneOffset.UTC);
    }

    private void seedAlerts() {
        Alert al1 = alert(AlertType.ACCIDENT, AlertSeverity.HAUTE, "Accident on Oxford Street — 2 lanes blocked", "Oxford Street, London W1");
        Alert al2 = alert(AlertType.METEO, AlertSeverity.MOYENNE, "Heavy rain forecast in 45 minutes — North London", "Islington — Hackney");
        Alert al3 = alert(AlertType.SATURATION, AlertSeverity.HAUTE, "King's College Hospital — A&E at capacity (98%)", "Denmark Hill, SE5");
        Alert al4 = alert(AlertType.TRAVAUX, AlertSeverity.BASSE, "Roadworks on Victoria Embankment — diversion recommended", "Victoria Embankment, SW1");

        alertRepository.saveAll(List.of(al1, al2, al3, al4));
    }

    private Alert alert(AlertType type, AlertSeverity severity, String message, String location) {
        Alert a = new Alert();
        a.setType(type);
        a.setSeverity(severity);
        a.setMessage(message);
        a.setLocation(location);
        a.setActive(true);
        return a;
    }
}
