package com.techforb.app.modules.policyholders.application.dto;

import com.techforb.app.modules.policyholders.domain.Policyholder;

public record VehicleResponse(String id, String plate, String brand, String model, int year,
        String engineNumber, String chassisNumber, boolean covered) {

    public static VehicleResponse from(Policyholder.Vehicle vehicle, boolean covered) {
        return new VehicleResponse(vehicle.id(), vehicle.plate(), vehicle.brand(), vehicle.model(), vehicle.year(),
                vehicle.engineNumber(), vehicle.chassisNumber(), covered);
    }
}
