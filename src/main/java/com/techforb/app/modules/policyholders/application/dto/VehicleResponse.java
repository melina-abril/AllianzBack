package com.techforb.app.modules.policyholders.application.dto;

import com.techforb.app.modules.policyholders.domain.Vehicle;
import java.time.Instant;

public record VehicleResponse(
        String id, String policyholderId, String plate, String make, String model, int year,
        String engineNumber, String chassisNumber, boolean covered, Instant updatedAt) {

    public static VehicleResponse from(Vehicle vehicle, boolean covered) {
        return new VehicleResponse(vehicle.getId(), vehicle.getPolicyholderId(), vehicle.getPlate(),
                vehicle.getMake(), vehicle.getModel(), vehicle.getYear(), vehicle.getEngineNumber(),
                vehicle.getChassisNumber(), covered, vehicle.getUpdatedAt());
    }
}
