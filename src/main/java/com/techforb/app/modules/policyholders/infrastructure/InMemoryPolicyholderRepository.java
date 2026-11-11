package com.techforb.app.modules.policyholders.infrastructure;

import com.techforb.app.modules.policyholders.domain.ClaimRecord;
import com.techforb.app.modules.policyholders.domain.ClaimStatus;
import com.techforb.app.modules.policyholders.domain.PersonType;
import com.techforb.app.modules.policyholders.domain.PolicyRecord;
import com.techforb.app.modules.policyholders.domain.PolicyStatus;
import com.techforb.app.modules.policyholders.domain.Policyholder;
import com.techforb.app.modules.policyholders.domain.PolicyholderRepository;
import com.techforb.app.modules.policyholders.domain.PolicyholderStatus;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

/** Datos DUMMY en memoria, incluidas pólizas y denuncias asociadas: reemplazar al conectar los registros reales. */
@Repository
public class InMemoryPolicyholderRepository implements PolicyholderRepository {

    private final Map<String, Policyholder> policyholders = new ConcurrentHashMap<>();

    public InMemoryPolicyholderRepository() {
        save(new Policyholder("ph-1001", PersonType.INDIVIDUAL, "María González", "28123456",
                PolicyholderStatus.ACTIVE, "", "maria.gonzalez@example.com", "11 4567-8901",
                "Av. Santa Fe 1234, CABA",
                List.of(new PolicyRecord("POL-1001", PolicyStatus.ACTIVE, "Terceros Completo con Granizo",
                        List.of("veh-1001"), LocalDate.parse("2025-01-01"), LocalDate.parse("2035-12-31"))),
                List.of(new ClaimRecord("SIN-2001", ClaimStatus.IN_REVIEW, "veh-1001",
                        "Daños por granizo", LocalDate.parse("2026-03-12"))),
                Instant.parse("2026-03-13T14:00:00Z")));
        save(new Policyholder("ph-1002", PersonType.LEGAL_ENTITY, "Transportes del Sur S.A.", "30712345678",
                PolicyholderStatus.ACTIVE, "Lucía Fernández", "administracion@example.com", "11 4300-2200",
                "Av. Hipólito Yrigoyen 4500, Lanús", List.of(), List.of(),
                Instant.parse("2026-02-08T10:15:00Z")));
        save(new Policyholder("ph-1003", PersonType.INDIVIDUAL, "Jorge Ramírez", "20567890",
                PolicyholderStatus.INACTIVE, "", "", "", "Calle 12 345, La Plata",
                List.of(new PolicyRecord("POL-1002", PolicyStatus.EXPIRED, "Responsabilidad Civil",
                        List.of("veh-1003"), LocalDate.parse("2023-01-01"), LocalDate.parse("2024-01-01"))),
                List.of(), Instant.parse("2025-06-10T09:30:00Z")));
        save(new Policyholder("ph-1004", PersonType.INDIVIDUAL, "Sofía Pérez", "32456789",
                PolicyholderStatus.ACTIVE, "", "sofia.perez@example.com", "351 444-7788",
                "Bv. San Juan 820, Córdoba",
                List.of(new PolicyRecord("POL-1003", PolicyStatus.CANCELLED, "Todo Riesgo con franquicia",
                        List.of("veh-1004"), LocalDate.parse("2025-01-01"), LocalDate.parse("2025-12-31"))),
                List.of(new ClaimRecord("SIN-2002", ClaimStatus.CLOSED, "veh-1004",
                        "Colisión en estacionamiento", LocalDate.parse("2025-04-09"))),
                Instant.parse("2026-01-15T12:00:00Z")));
    }

    @Override
    public List<Policyholder> findAll() {
        return policyholders.values().stream().sorted(Comparator.comparing(Policyholder::getName)).toList();
    }

    @Override
    public Optional<Policyholder> findById(String id) {
        return Optional.ofNullable(policyholders.get(id));
    }

    @Override
    public Optional<Policyholder> findByDocumentNumber(String documentNumber) {
        return policyholders.values().stream()
                .filter(holder -> holder.getDocumentNumber().equals(documentNumber)).findFirst();
    }

    @Override
    public Policyholder save(Policyholder policyholder) {
        policyholders.put(policyholder.getId(), policyholder);
        return policyholder;
    }
}
