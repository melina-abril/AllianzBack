package com.techforb.app.modules.quotes.domain;

import java.math.BigDecimal;

/** Valuación de ejemplo asociada a una combinación disponible en el catálogo. */
public record VehicleOption(String brand, String model, String version, int year, BigDecimal insuredAmount) {
}
