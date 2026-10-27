package com.techforb.app.modules.policyholders.application;

import com.techforb.app.modules.policyholders.application.dto.PolicyholderDetailResponse;
import com.techforb.app.modules.policyholders.application.dto.PolicyholderRequest;
import com.techforb.app.modules.policyholders.application.dto.PolicyholderResponse;
import com.techforb.app.modules.policyholders.application.dto.VehicleRequest;
import com.techforb.app.modules.policyholders.application.dto.VehicleResponse;
import com.techforb.app.modules.policyholders.domain.DocumentType;
import com.techforb.app.modules.policyholders.domain.PersonType;
import com.techforb.app.modules.policyholders.domain.Policyholder;
import com.techforb.app.modules.policyholders.domain.PolicyholderRepository;
import com.techforb.app.modules.policyholders.domain.Vehicle;
import com.techforb.app.shared.error.BusinessRuleException;
import com.techforb.app.shared.error.ConflictException;
import com.techforb.app.shared.error.NotFoundException;
import com.techforb.app.shared.web.PageResponse;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
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
        String term = search == null ? "" : search.trim().toLowerCase(Locale.ROOT);
        String digits = normalizeDocument(term);
        boolean documentSearch = !term.isEmpty() && term.matches("[0-9 .-]+") && !digits.isEmpty();
        List<Policyholder> filtered = repository.findAll().stream()
                .filter(holder -> term.isEmpty()
                        || (documentSearch ? holder.getDocumentNumber().equals(digits)
                                : holder.getName().toLowerCase(Locale.ROOT).contains(term)))
                .toList();
        // Respuesta DUMMY basada en registros en memoria; reemplazar al conectar la persistencia real.
        return PageResponse.of(filtered, page, size, PolicyholderResponse::from);
    }

    public PolicyholderDetailResponse getPolicyholder(String id) {
        return detail(find(id));
    }

    public PolicyholderDetailResponse createPolicyholder(PolicyholderRequest request) {
        String document = validatedDocument(request);
        assertDocumentAvailable(document, null);
        Policyholder holder = new Policyholder(UUID.randomUUID().toString(), request.personType(),
                request.name().trim(), request.documentType(), document, request.primaryContact(),
                request.email(), request.phone(), request.address(), Instant.now(clock));
        return detail(repository.save(holder));
    }

    public PolicyholderDetailResponse updatePolicyholder(String id, PolicyholderRequest request) {
        Policyholder holder = find(id);
        assertActive(holder);
        String document = validatedDocument(request);
        assertDocumentAvailable(document, id);
        holder.updateDetails(request.personType(), request.name().trim(), request.documentType(),
                document, request.primaryContact(), request.email(), request.phone(), request.address(),
                Instant.now(clock));
        return detail(repository.save(holder));
    }

    public void deactivatePolicyholder(String id) {
        Policyholder holder = find(id);
        assertActive(holder);
        if (holder.activePolicyCount(LocalDate.now(clock)) > 0) {
            throw new BusinessRuleException("POLICYHOLDER_HAS_ACTIVE_POLICIES",
                    "No se puede dar de baja a un asegurado con pólizas vigentes.");
        }
        holder.deactivate(Instant.now(clock));
        repository.save(holder);
    }

    public VehicleResponse createVehicle(String id, VehicleRequest request) {
        Policyholder holder = find(id);
        assertActive(holder);
        String plate = validatedPlate(request.plate());
        assertPlateAvailable(plate, null);
        Vehicle vehicle = new Vehicle(UUID.randomUUID().toString(), plate, request.brand().trim(),
                request.model().trim(), request.year(), request.engine().trim(), request.chassis().trim());
        holder.addVehicle(vehicle, Instant.now(clock));
        repository.save(holder);
        return VehicleResponse.from(vehicle, holder.getPolicies(), LocalDate.now(clock));
    }

    public VehicleResponse updateVehicle(String id, String vehicleId, VehicleRequest request) {
        Policyholder holder = find(id);
        assertActive(holder);
        Vehicle vehicle = holder.getVehicles().stream().filter(item -> item.getId().equals(vehicleId))
                .findFirst().orElseThrow(() -> new NotFoundException("No existe el vehículo " + vehicleId + " para este asegurado."));
        String plate = validatedPlate(request.plate());
        assertPlateAvailable(plate, vehicleId);
        vehicle.update(plate, request.brand().trim(), request.model().trim(), request.year(),
                request.engine().trim(), request.chassis().trim());
        holder.touch(Instant.now(clock));
        repository.save(holder);
        return VehicleResponse.from(vehicle, holder.getPolicies(), LocalDate.now(clock));
    }

    private PolicyholderDetailResponse detail(Policyholder holder) {
        // Respuesta DUMMY con pólizas y siniestros de ejemplo; reemplazar por registros reales.
        return PolicyholderDetailResponse.from(holder, LocalDate.now(clock));
    }

    private Policyholder find(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("No existe el asegurado " + id + "."));
    }

    private void assertActive(Policyholder holder) {
        if (!holder.isActive()) {
            throw new BusinessRuleException("POLICYHOLDER_INACTIVE", "El asegurado ya está dado de baja.");
        }
    }

    private String validatedDocument(PolicyholderRequest request) {
        String number = normalizeDocument(request.documentNumber());
        if ((request.personType() == PersonType.NATURAL && request.documentType() != DocumentType.DNI)
                || (request.personType() == PersonType.LEGAL && request.documentType() != DocumentType.CUIT)) {
            throw new BusinessRuleException("DOCUMENT_TYPE_MISMATCH", "El tipo de documento no corresponde al tipo de persona.");
        }
        if ((request.documentType() == DocumentType.DNI && (number.length() < 7 || number.length() > 8))
                || (request.documentType() == DocumentType.CUIT && number.length() != 11)) {
            throw new BusinessRuleException("INVALID_DOCUMENT_NUMBER", "Ingresá un DNI de 7 u 8 dígitos o un CUIT de 11 dígitos, según corresponda.");
        }
        return number;
    }

    private static String normalizeDocument(String value) {
        return value.replaceAll("[^0-9]", "");
    }

    private static String validatedPlate(String value) {
        String plate = value.toUpperCase(Locale.ROOT).replaceAll("[^A-Z0-9]", "");
        if (!plate.matches("[A-Z]{3}[0-9]{3}|[A-Z]{2}[0-9]{3}[A-Z]{2}")) {
            throw new BusinessRuleException("INVALID_PLATE", "Ingresá una patente válida.");
        }
        return plate;
    }

    private void assertDocumentAvailable(String document, String exceptId) {
        repository.findByDocumentNumber(document)
                .filter(existing -> !existing.getId().equals(exceptId))
                .ifPresent(existing -> {
                    throw new ConflictException("Ya existe un asegurado con ese DNI o CUIT.", "documentNumber");
                });
    }

    private void assertPlateAvailable(String plate, String exceptVehicleId) {
        repository.findByPlate(plate)
                .filter(existing -> !existing.getId().equals(exceptVehicleId))
                .ifPresent(existing -> {
                    throw new ConflictException("Ya existe un vehículo con esa patente.", "plate");
                });
    }
}
