package com.techforb.app.modules.policyholders.application;

import com.techforb.app.modules.policyholders.application.dto.VehicleRequest;
import com.techforb.app.modules.policyholders.application.dto.VehicleResponse;
import com.techforb.app.modules.policyholders.domain.Policyholder;
import com.techforb.app.modules.policyholders.domain.PolicyholderStatus;
import com.techforb.app.modules.policyholders.domain.Vehicle;
import com.techforb.app.modules.policyholders.domain.VehicleRepository;
import com.techforb.app.shared.error.BusinessRuleException;
import com.techforb.app.shared.error.ConflictException;
import com.techforb.app.shared.error.NotFoundException;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Locale;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {

    private final VehicleRepository repository;
    private final PolicyholderService policyholderService;
    private final Clock clock;

    public VehicleService(VehicleRepository repository, PolicyholderService policyholderService, Clock clock) {
        this.repository = repository;
        this.policyholderService = policyholderService;
        this.clock = clock;
    }

    public VehicleResponse createVehicle(VehicleRequest request) {
        Policyholder holder = activeHolder(request.policyholderId());
        String plate = validatedPlate(request.plate());
        assertPlateAvailable(plate, null);
        Vehicle vehicle = new Vehicle(UUID.randomUUID().toString(), holder.getId(), plate, request.make(),
                request.model(), request.year(), request.engineNumber(), request.chassisNumber(), Instant.now(clock));
        return response(repository.save(vehicle), holder);
    }

    public VehicleResponse getVehicle(String id) {
        Vehicle vehicle = find(id);
        return response(vehicle, policyholderService.findEntity(vehicle.getPolicyholderId()));
    }

    public VehicleResponse updateVehicle(String id, VehicleRequest request) {
        Vehicle vehicle = find(id);
        if (!vehicle.getPolicyholderId().equals(request.policyholderId())) {
            throw new BusinessRuleException("VEHICLE_OWNER_CHANGE_NOT_ALLOWED", "No podés cambiar el asegurado de un vehículo.");
        }
        Policyholder holder = activeHolder(request.policyholderId());
        String plate = validatedPlate(request.plate());
        assertPlateAvailable(plate, id);
        vehicle.updateDetails(plate, request.make(), request.model(), request.year(),
                request.engineNumber(), request.chassisNumber(), Instant.now(clock));
        return response(repository.save(vehicle), holder);
    }

    private Vehicle find(String id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("No existe el vehículo " + id + "."));
    }

    private Policyholder activeHolder(String id) {
        Policyholder holder = policyholderService.findEntity(id);
        if (holder.getStatus() != PolicyholderStatus.ACTIVE) {
            throw new BusinessRuleException("POLICYHOLDER_INACTIVE", "El asegurado está dado de baja.");
        }
        return holder;
    }

    private VehicleResponse response(Vehicle vehicle, Policyholder holder) {
        return VehicleResponse.from(vehicle, holder.isCovered(vehicle.getId(), LocalDate.now(clock)));
    }

    private String validatedPlate(String input) {
        String plate = input.toUpperCase(Locale.ROOT).replaceAll("[\\s-]", "");
        if (!plate.matches("[A-Z]{3}[0-9]{3}|[A-Z]{2}[0-9]{3}[A-Z]{2}")) {
            throw new BusinessRuleException("INVALID_PLATE", "Ingresá una patente válida.");
        }
        return plate;
    }

    private void assertPlateAvailable(String plate, String exceptId) {
        repository.findByPlate(plate)
                .filter(existing -> !existing.getId().equals(exceptId))
                .ifPresent(existing -> {
                    throw new ConflictException("Ya existe un vehículo con esa patente.", "plate");
                });
    }
}
