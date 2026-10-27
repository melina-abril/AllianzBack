package com.techforb.app.modules.policyholders.application;

import com.techforb.app.modules.policyholders.application.dto.PolicyholderRequest;
import com.techforb.app.modules.policyholders.application.dto.PolicyholderResponse;
import com.techforb.app.modules.policyholders.application.dto.VehicleRequest;
import com.techforb.app.modules.policyholders.application.dto.VehicleResponse;
import com.techforb.app.modules.policyholders.domain.Policyholder;
import com.techforb.app.modules.policyholders.domain.PolicyholderRepository;
import com.techforb.app.shared.error.BusinessRuleException;
import com.techforb.app.shared.error.ConflictException;
import com.techforb.app.shared.error.NotFoundException;
import com.techforb.app.shared.web.PageResponse;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class PolicyholderService {

    private final PolicyholderRepository repository;
    private final Clock clock;

    public PolicyholderService(PolicyholderRepository repository, Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    public PageResponse<PolicyholderResponse> listPolicyholders(String search, int page, int size) {
        String term = search == null ? "" : search.trim();
        String document = normalizeDocument(term);
        List<Policyholder> matches = repository.findAll().stream()
                .filter(holder -> term.isEmpty()
                        || holder.getName().toLowerCase(Locale.ROOT).contains(term.toLowerCase(Locale.ROOT))
                        || (!document.isEmpty() && normalizeDocument(holder.getDocumentNumber()).equals(document)))
                .toList();
        // Datos de ejemplo en memoria: reemplazar por consultas reales al conectar la persistencia.
        return PageResponse.of(matches, page, size, PolicyholderResponse::from);
    }

    public PolicyholderResponse getPolicyholder(String id) {
        return PolicyholderResponse.from(find(id));
    }

    public PolicyholderResponse createPolicyholder(PolicyholderRequest request) {
        validateDocument(request);
        assertDocumentAvailable(request.documentNumber(), null);
        Policyholder holder = new Policyholder(UUID.randomUUID().toString(), request.personType(), request.name().trim(),
                normalizeDocument(request.documentNumber()), request.primaryContact(), request.email(), request.phone(),
                request.address(), Policyholder.Status.ACTIVE, Instant.now(clock), List.of(), List.of(), List.of());
        return PolicyholderResponse.from(repository.save(holder));
    }

    public PolicyholderResponse updatePolicyholder(String id, PolicyholderRequest request) {
        Policyholder holder = find(id);
        assertActive(holder);
        validateDocument(request);
        assertDocumentAvailable(request.documentNumber(), id);
        holder.updateDetails(request.personType(), request.name().trim(), normalizeDocument(request.documentNumber()),
                request.primaryContact(), request.email(), request.phone(), request.address(), Instant.now(clock));
        return PolicyholderResponse.from(repository.save(holder));
    }

    public VehicleResponse addVehicle(String id, VehicleRequest request) {
        Policyholder holder = find(id);
        assertActive(holder);
        assertPlateAvailable(request.plate(), null);
        Policyholder.Vehicle vehicle = toVehicle(UUID.randomUUID().toString(), request);
        holder.addVehicle(vehicle, Instant.now(clock));
        repository.save(holder);
        return VehicleResponse.from(vehicle, holder.isCovered(vehicle.id()));
    }

    public VehicleResponse updateVehicle(String id, String vehicleId, VehicleRequest request) {
        Policyholder holder = find(id);
        assertActive(holder);
        holder.getVehicles().stream().filter(vehicle -> vehicle.id().equals(vehicleId)).findFirst()
                .orElseThrow(() -> new NotFoundException("No existe el vehículo indicado para este asegurado."));
        assertPlateAvailable(request.plate(), vehicleId);
        Policyholder.Vehicle vehicle = toVehicle(vehicleId, request);
        holder.replaceVehicle(vehicle, Instant.now(clock));
        repository.save(holder);
        return VehicleResponse.from(vehicle, holder.isCovered(vehicleId));
    }

    public void deactivatePolicyholder(String id) {
        Policyholder holder = find(id);
        assertActive(holder);
        if (holder.activePolicyCount() > 0) {
            throw new BusinessRuleException("POLICYHOLDER_HAS_ACTIVE_POLICIES",
                    "No puedes dar de baja a un asegurado con pólizas vigentes.");
        }
        holder.deactivate(Instant.now(clock));
        repository.save(holder);
    }

    private Policyholder find(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("No existe el asegurado " + id + "."));
    }

    private void assertActive(Policyholder holder) {
        if (holder.getStatus() == Policyholder.Status.INACTIVE) {
            throw new BusinessRuleException("POLICYHOLDER_ALREADY_INACTIVE", "El asegurado ya está dado de baja.");
        }
    }

    private void validateDocument(PolicyholderRequest request) {
        int length = normalizeDocument(request.documentNumber()).length();
        if (request.personType() == Policyholder.PersonType.INDIVIDUAL && (length < 7 || length > 8)) {
            throw new BusinessRuleException("INVALID_DNI", "El DNI debe tener 7 u 8 dígitos.");
        }
        if (request.personType() == Policyholder.PersonType.LEGAL_ENTITY && length != 11) {
            throw new BusinessRuleException("INVALID_CUIT", "El CUIT debe tener 11 dígitos.");
        }
    }

    private void assertDocumentAvailable(String documentNumber, String exceptId) {
        String normalized = normalizeDocument(documentNumber);
        repository.findAll().stream()
                .filter(holder -> normalizeDocument(holder.getDocumentNumber()).equals(normalized))
                .filter(holder -> !holder.getId().equals(exceptId))
                .findFirst().ifPresent(holder -> {
                    throw new ConflictException("Ya existe un asegurado con ese DNI o CUIT.", "documentNumber");
                });
    }

    private void assertPlateAvailable(String plate, String exceptVehicleId) {
        String normalized = normalizePlate(plate);
        boolean used = repository.findAll().stream().flatMap(holder -> holder.getVehicles().stream())
                .anyMatch(vehicle -> normalizePlate(vehicle.plate()).equals(normalized)
                        && !vehicle.id().equals(exceptVehicleId));
        if (used) {
            throw new ConflictException("Ya existe un vehículo con esa patente.", "plate");
        }
    }

    private Policyholder.Vehicle toVehicle(String id, VehicleRequest request) {
        return new Policyholder.Vehicle(id, normalizePlate(request.plate()), request.brand().trim(),
                request.model().trim(), request.year(), request.engineNumber().trim(), request.chassisNumber().trim());
    }

    private static String normalizeDocument(String value) {
        return value.replaceAll("[^0-9]", "");
    }

    private static String normalizePlate(String value) {
        return value.replaceAll("[^A-Za-z0-9]", "").toUpperCase(Locale.ROOT);
    }
}
