package com.techforb.app.modules.policyholders.api;

import com.techforb.app.modules.policyholders.application.VehicleService;
import com.techforb.app.modules.policyholders.application.dto.VehicleRequest;
import com.techforb.app.modules.policyholders.application.dto.VehicleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vehicles")
@Tag(name = "Vehículos de asegurados")
public class VehicleController {

    private final VehicleService service;

    public VehicleController(VehicleService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registra un vehículo de un asegurado")
    public VehicleResponse createVehicle(@Valid @RequestBody VehicleRequest request) {
        return service.createVehicle(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulta un vehículo")
    public VehicleResponse getVehicle(@PathVariable String id) {
        return service.getVehicle(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Edita un vehículo")
    public VehicleResponse updateVehicle(@PathVariable String id, @Valid @RequestBody VehicleRequest request) {
        return service.updateVehicle(id, request);
    }
}
