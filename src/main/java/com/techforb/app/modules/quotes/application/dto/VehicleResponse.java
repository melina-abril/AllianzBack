package com.techforb.app.modules.quotes.application.dto;

import com.techforb.app.modules.quotes.domain.Vehicle;
import java.math.BigDecimal;

public record VehicleResponse(String id, String brand, String model, String version, int year, BigDecimal insuredValue) {

    public static VehicleResponse from(Vehicle vehicle) {
        return new VehicleResponse(vehicle.id(), vehicle.brand(), vehicle.model(), vehicle.version(),
                vehicle.year(), vehicle.insuredValue());
    }
}
