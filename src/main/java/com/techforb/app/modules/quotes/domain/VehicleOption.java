package com.techforb.app.modules.quotes.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Opción del catálogo y valuación de ejemplo utilizada para calcular las coberturas. */
public record VehicleOption(String brand, String model, String version, int year, BigDecimal insuredValue) {

    public VehicleOption {
        insuredValue = insuredValue.setScale(2, RoundingMode.HALF_EVEN);
    }
}
