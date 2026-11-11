package com.techforb.app.modules.policyholders.application;

import com.techforb.app.modules.policyholders.application.dto.PolicyholderRequest;
import com.techforb.app.modules.policyholders.application.dto.PolicyholderResponse;
import com.techforb.app.modules.policyholders.application.dto.PolicyholderSummaryResponse;
import com.techforb.app.modules.policyholders.domain.PersonType;
import com.techforb.app.modules.policyholders.domain.Policyholder;
import com.techforb.app.modules.policyholders.domain.PolicyholderRepository;
import com.techforb.app.modules.policyholders.domain.PolicyholderStatus;
import com.techforb.app.modules.policyholders.domain.VehicleRepository;
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
    private final VehicleRepository vehicleRepository;
    private final Clock clock;

    public PolicyholderService(PolicyholderRepository repository, VehicleRepository vehicleRepository, Clock clock) {
        this.repository = repository;
        this.vehicleRepository = vehicleRepository;
        this.clock = clock;
    }

    public PageResponse<PolicyholderSummaryResponse> listPolicyholders(String name, String documentNumber, int page, int size) {
        String term = name == null ? "" : name.trim().toLowerCase(Locale.ROOT);
        String document = normalizeDocument(documentNumber);
        List<Policyholder> filtered = repository.findAll().stream()
                .filter(holder -> term.isEmpty() || holder.getName().toLowerCase(Locale.ROOT).contains(term))
                .filter(holder -> document.isEmpty() || holder.getDocumentNumber().equals(document))
                .toList();
        return PageResponse.of(filtered, page, size, PolicyholderSummaryResponse::from);
    }

    public PolicyholderResponse getPolicyholder(String id) {
        return response(findEntity(id));
    }

    public PolicyholderResponse createPolicyholder(PolicyholderRequest request) {
        String document = validatedDocument(request);
        assertDocumentAvailable(document, null);
        Policyholder holder = new Policyholder(UUID.randomUUID().toString(), request.personType(), request.name(),
                document, PolicyholderStatus.ACTIVE, request.primaryContact(), request.email(), request.phone(),
                request.address(), List.of(), List.of(), Instant.now(clock));
        return response(repository.save(holder));
    }

    public PolicyholderResponse updatePolicyholder(String id, PolicyholderRequest request) {
        Policyholder holder = findEntity(id);
        String document = validatedDocument(request);
        assertDocumentAvailable(document, id);
        holder.updateDetails(request.personType(), request.name(), document, request.primaryContact(),
                request.email(), request.phone(), request.address(), Instant.now(clock));
        return response(repository.save(holder));
    }

    public void deactivatePolicyholder(String id) {
        Policyholder holder = findEntity(id);
        if (holder.getStatus() == PolicyholderStatus.INACTIVE) {
            throw new BusinessRuleException("POLICYHOLDER_ALREADY_INACTIVE", "El asegurado ya está dado de baja.");
        }
        if (holder.activePolicyCount(LocalDate.now(clock)) > 0) {
            throw new BusinessRuleException("POLICYHOLDER_HAS_ACTIVE_POLICIES", "No podés dar de baja a un asegurado con pólizas vigentes.");
        }
        holder.deactivate(Instant.now(clock));
        repository.save(holder);
    }

    Policyholder findEntity(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("No existe el asegurado " + id + "."));
    }

    private PolicyholderResponse response(Policyholder holder) {
        return PolicyholderResponse.from(holder, vehicleRepository.findByPolicyholderId(holder.getId()), LocalDate.now(clock));
    }

    private String validatedDocument(PolicyholderRequest request) {
        String document = normalizeDocument(request.documentNumber());
        int length = request.personType() == PersonType.INDIVIDUAL ? 8 : 11;
        if (!document.matches("\\d{" + length + "}")) {
            throw new BusinessRuleException("INVALID_DOCUMENT", "Ingresá un DNI de 8 dígitos o un CUIT de 11 dígitos según el tipo de persona.");
        }
        return document;
    }

    private void assertDocumentAvailable(String document, String exceptId) {
        repository.findByDocumentNumber(document)
                .filter(existing -> !existing.getId().equals(exceptId))
                .ifPresent(existing -> {
                    throw new ConflictException("Ya existe un asegurado con ese DNI o CUIT.", "documentNumber");
                });
    }

    private String normalizeDocument(String document) {
        return document == null ? "" : document.replaceAll("[\\s.\u002d/]", "");
    }
}
