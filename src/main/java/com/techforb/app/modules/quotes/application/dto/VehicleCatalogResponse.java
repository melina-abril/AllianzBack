package com.techforb.app.modules.quotes.application.dto;

import com.techforb.app.modules.quotes.domain.VehicleOption;
import java.util.List;

/** Cada opción es una combinación válida de marca, modelo, versión y año. */
public record VehicleCatalogResponse(List<VehicleOptionResponse> vehicles) {

    public static VehicleCatalogResponse from(List<VehicleOption> options) {
        return new VehicleCatalogResponse(options.stream().map(VehicleOptionResponse::from).toList());
    }
}
