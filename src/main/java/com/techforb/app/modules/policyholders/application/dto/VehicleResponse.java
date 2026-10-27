package com.techforb.app.modules.policyholders.application.dto;

import com.techforb.app.modules.policyholders.domain.PolicyRecord;
import com.techforb.app.modules.policyholders.domain.Vehicle;
import java.time.LocalDate;
import java.util.List;

public record VehicleResponse(String id, String plate, String brand, String model,
        int year, String engine, String chassis, boolean covered) {

    public static VehicleResponse from(Vehicle vehicle, List<PolicyRecord> policies, LocalDate today) {
        boolean covered = policies.stream().anyMatch(policy ->
                policy.isActiveOn(today) && policy.vehicleIds().contains(vehicle.getId()));
        return new VehicleResponse(vehicle.getId(), vehicle.getPlate(), vehicle.getBrand(),
                vehicle.getModel(), vehicle.getYear(), vehicle.getEngine(), vehicle.getChassis(), covered);
    }
}
