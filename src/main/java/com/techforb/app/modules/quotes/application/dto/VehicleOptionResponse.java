package com.techforb.app.modules.quotes.application.dto;

import com.techforb.app.modules.quotes.domain.VehicleOption;

public record VehicleOptionResponse(String brand, String model, String version, int year) {

    public static VehicleOptionResponse from(VehicleOption option) {
        return new VehicleOptionResponse(option.brand(), option.model(), option.version(), option.year());
    }
}
