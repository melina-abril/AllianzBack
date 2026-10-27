package com.techforb.app.modules.quotes.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Combinación cotizable y valuación de ejemplo. */
public record Vehicle(String id, String brand, String model, String version, int year, BigDecimal insuredValue) {

    public Vehicle {
        insuredValue = insuredValue.setScale(2, RoundingMode.HALF_EVEN);
    }
}
