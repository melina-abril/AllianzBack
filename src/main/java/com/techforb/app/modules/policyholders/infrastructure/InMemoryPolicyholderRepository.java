package com.techforb.app.modules.policyholders.infrastructure;

import com.techforb.app.modules.policyholders.domain.ClaimRecord;
import com.techforb.app.modules.policyholders.domain.ClaimStatus;
import com.techforb.app.modules.policyholders.domain.DocumentType;
import com.techforb.app.modules.policyholders.domain.PersonType;
import com.techforb.app.modules.policyholders.domain.PolicyRecord;
import com.techforb.app.modules.policyholders.domain.PolicyStatus;
import com.techforb.app.modules.policyholders.domain.Policyholder;
import com.techforb.app.modules.policyholders.domain.PolicyholderRepository;
import com.techforb.app.modules.policyholders.domain.Vehicle;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

/** Datos DUMMY en memoria: sustituir por persistencia y registros de pólizas y siniestros reales. */
@Repository
public class InMemoryPolicyholderRepository implements PolicyholderRepository {

    private final Map<String, Policyholder> policyholders = new ConcurrentHashMap<>();

    public InMemoryPolicyholderRepository(Clock clock) {
        LocalDate today = LocalDate.now(clock);
        Instant now = Instant.now(clock);

        Policyholder ana = new Policyholder("ph-1001", PersonType.NATURAL, "Ana Martínez", DocumentType.DNI,
                "28456789", null, "ana.martinez@example.com", "11 4567-8901", "Av. Santa Fe 1234, CABA", now);
        ana.addVehicle(new Vehicle("veh-1001", "AF123BC", "Toyota", "Corolla", 2022, "2ZRFAE82901", "9BRBD3HE5N1234567"), now);
        ana.addVehicle(new Vehicle("veh-1002", "AB123CD", "Volkswagen", "Gol Trend", 2018, "CFZ912345", "9BWAB45U8JT012345"), now);
        ana.addPolicy(new PolicyRecord("POL-1001", PolicyStatus.ACTIVE, "Terceros Completo con Granizo",
                List.of("veh-1001"), today.minusMonths(2), today.plusMonths(10)));
        ana.addPolicy(new PolicyRecord("POL-0901", PolicyStatus.EXPIRED, "Responsabilidad Civil",
                List.of("veh-1002"), today.minusYears(2), today.minusYears(1)));
        ana.addClaim(new ClaimRecord("SIN-1001", ClaimStatus.IN_REVIEW, "veh-1001",
                "Daños por granizo", today.minusDays(20)));
        save(ana);

        Policyholder company = new Policyholder("ph-1002", PersonType.LEGAL, "Logística del Sur SA", DocumentType.CUIT,
                "30712345678", "María Fernández", "flota@example.com", "11 4300-1122", "Av. Rivadavia 3456, CABA", now);
        company.addVehicle(new Vehicle("veh-1003", "AE456FG", "Ford", "Ranger", 2023, "P5AT231045", "8AFAR22K0PJ123456"), now);
        company.addPolicy(new PolicyRecord("POL-1002", PolicyStatus.ACTIVE, "Todo Riesgo con franquicia",
                List.of("veh-1003"), today.minusMonths(1), today.plusMonths(11)));
        save(company);

        Policyholder pedro = new Policyholder("ph-1003", PersonType.NATURAL, "Pedro Gómez", DocumentType.DNI,
                "32123456", null, null, "11 5555-2100", "Calle 9 487, La Plata", now);
        pedro.addVehicle(new Vehicle("veh-1004", "AC789HI", "Fiat", "Cronos", 2020, "552730188", "8AP359A2NLU123456"), now);
        pedro.addClaim(new ClaimRecord("SIN-0902", ClaimStatus.CLOSED, "veh-1004",
                "Rotura de parabrisas", today.minusMonths(8)));
        save(pedro);

        Policyholder lucia = new Policyholder("ph-1004", PersonType.NATURAL, "Lucía Torres", DocumentType.DNI,
                "35678901", null, "lucia.torres@example.com", null, "San Martín 210, Córdoba", now);
        save(lucia);
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
    public Optional<Policyholder> findByDocumentNumber(String documentNumber) {
        return policyholders.values().stream()
                .filter(holder -> holder.getDocumentNumber().equals(documentNumber)).findFirst();
    }

    @Override
    public Optional<Vehicle> findByPlate(String plate) {
        return policyholders.values().stream().flatMap(holder -> holder.getVehicles().stream())
                .filter(vehicle -> vehicle.getPlate().equalsIgnoreCase(plate)).findFirst();
    }

    @Override
    public Policyholder save(Policyholder policyholder) {
        policyholders.put(policyholder.getId(), policyholder);
        return policyholder;
    }
}
