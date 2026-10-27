package com.techforb.app.modules.policyholders.api;

import com.techforb.app.modules.policyholders.application.PolicyholderService;
import com.techforb.app.modules.policyholders.application.dto.PolicyholderRequest;
import com.techforb.app.modules.policyholders.application.dto.PolicyholderResponse;
import com.techforb.app.modules.policyholders.application.dto.VehicleRequest;
import com.techforb.app.modules.policyholders.application.dto.VehicleResponse;
import com.techforb.app.shared.web.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/policyholders")
@Tag(name = "Asegurados")
public class PolicyholderController {

    private final PolicyholderService service;

    public PolicyholderController(PolicyholderService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Buscar asegurados por nombre o documento")
    public PageResponse<PolicyholderResponse> listPolicyholders(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return service.listPolicyholders(search, page, size);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar ficha del asegurado")
    public PolicyholderResponse getPolicyholder(@PathVariable String id) {
        return service.getPolicyholder(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Dar de alta un asegurado")
    public PolicyholderResponse createPolicyholder(@Valid @RequestBody PolicyholderRequest request) {
        return service.createPolicyholder(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un asegurado")
    public PolicyholderResponse updatePolicyholder(@PathVariable String id, @Valid @RequestBody PolicyholderRequest request) {
        return service.updatePolicyholder(id, request);
    }

    @PostMapping("/{id}/vehicles")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Agregar un vehículo al asegurado")
    public VehicleResponse addVehicle(@PathVariable String id, @Valid @RequestBody VehicleRequest request) {
        return service.addVehicle(id, request);
    }

    @PutMapping("/{id}/vehicles/{vehicleId}")
    @Operation(summary = "Actualizar un vehículo del asegurado")
    public VehicleResponse updateVehicle(@PathVariable String id, @PathVariable String vehicleId,
            @Valid @RequestBody VehicleRequest request) {
        return service.updateVehicle(id, vehicleId, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Dar de baja un asegurado sin pólizas vigentes")
    public void deactivatePolicyholder(@PathVariable String id) {
        service.deactivatePolicyholder(id);
    }
}
