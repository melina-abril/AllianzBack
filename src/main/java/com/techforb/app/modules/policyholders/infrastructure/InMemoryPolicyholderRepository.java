package com.techforb.app.modules.policyholders.infrastructure;

import com.techforb.app.modules.policyholders.domain.Policyholder;
import com.techforb.app.modules.policyholders.domain.Policyholder.ClaimSummary;
import com.techforb.app.modules.policyholders.domain.Policyholder.PersonType;
import com.techforb.app.modules.policyholders.domain.Policyholder.PolicySummary;
import com.techforb.app.modules.policyholders.domain.Policyholder.Status;
import com.techforb.app.modules.policyholders.domain.Policyholder.Vehicle;
import com.techforb.app.modules.policyholders.domain.PolicyholderRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

/** Datos DUMMY en memoria: reemplazar por un repositorio persistente y asociaciones reales. */
@Repository
public class InMemoryPolicyholderRepository implements PolicyholderRepository {

    private final Map<String, Policyholder> policyholders = new ConcurrentHashMap<>();

    public InMemoryPolicyholderRepository() {
        save(new Policyholder("ph-1001", PersonType.INDIVIDUAL, "María González", "28456789",
                null, "maria.gonzalez@example.com", "+54 11 4567-8901", "Av. Santa Fe 1450, CABA",
                Status.ACTIVE, Instant.parse("2026-03-12T14:00:00Z"),
                List.of(new Vehicle("veh-1001", "AE123BC", "Toyota", "Corolla", 2022, "MTR482901", "CHS1001829"),
                        new Vehicle("veh-1002", "AB456CD", "Volkswagen", "Gol", 2017, "MTR392810", "CHS1002830")),
                List.of(new PolicySummary("POL-1001", "ACTIVE", "Terceros completo", List.of("veh-1001"),
                        LocalDate.parse("2026-01-01"), LocalDate.parse("2026-12-31")),
                        new PolicySummary("POL-0901", "EXPIRED", "Responsabilidad civil", List.of("veh-1002"),
                                LocalDate.parse("2024-01-01"), LocalDate.parse("2024-12-31"))),
                List.of(new ClaimSummary("SIN-1001", "IN_REVIEW", "veh-1001", "AE123BC", "Daños por granizo",
                        LocalDate.parse("2026-02-18")))));
        save(new Policyholder("ph-1002", PersonType.LEGAL_ENTITY, "Transportes del Sur S. A.", "30712345678",
                "Lucía Fernández", "administracion@example.com", "+54 11 4321-7788", "Av. Rivadavia 3200, CABA",
                Status.ACTIVE, Instant.parse("2026-03-10T09:15:00Z"),
                List.of(new Vehicle("veh-1003", "AG789EF", "Ford", "Ranger", 2023, "MTR883729", "CHS1003921")),
                List.of(new PolicySummary("POL-1002", "ACTIVE", "Todo riesgo con franquicia", List.of("veh-1003"),
                        LocalDate.parse("2026-02-01"), LocalDate.parse("2027-01-31"))), List.of()));
        save(new Policyholder("ph-1003", PersonType.INDIVIDUAL, "Juan Pérez", "32123456",
                null, null, "+54 351 555-0123", "San Martín 450, Córdoba",
                Status.ACTIVE, Instant.parse("2026-02-20T11:30:00Z"), List.of(), List.of(), List.of()));
        save(new Policyholder("ph-1004", PersonType.INDIVIDUAL, "Ana Martínez", "26111222",
                null, "ana.martinez@example.com", null, "Mitre 870, Rosario",
                Status.INACTIVE, Instant.parse("2026-01-15T17:00:00Z"),
                List.of(new Vehicle("veh-1004", "AC987GH", "Chevrolet", "Onix", 2019, "MTR771100", "CHS1004110")),
                List.of(new PolicySummary("POL-0802", "EXPIRED", "Responsabilidad civil", List.of("veh-1004"),
                        LocalDate.parse("2024-04-01"), LocalDate.parse("2025-03-31"))),
                List.of(new ClaimSummary("SIN-0802", "CLOSED", "veh-1004", "AC987GH", "Rotura de parabrisas",
                        LocalDate.parse("2024-08-09")))));
    }

    @Override
    public List<Policyholder> findAll() {
        List<Policyholder> all = new ArrayList<>(policyholders.values());
        all.sort(Comparator.comparing(Policyholder::getName, String.CASE_INSENSITIVE_ORDER));
        return all;
    }

    @Override
    public Optional<Policyholder> findById(String id) {
        return Optional.ofNullable(policyholders.get(id));
    }

    @Override
    public Policyholder save(Policyholder policyholder) {
        policyholders.put(policyholder.getId(), policyholder);
        return policyholder;
    }
}
