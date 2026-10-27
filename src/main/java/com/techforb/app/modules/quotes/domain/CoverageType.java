package com.techforb.app.modules.quotes.domain;

import java.math.BigDecimal;

/** Factores de cotización de ejemplo; reemplazar por las tarifas vigentes al integrarlas. */
public enum CoverageType {
    CIVIL_LIABILITY("Responsabilidad Civil", "0.0018"),
    COMPREHENSIVE_THIRD_PARTY("Terceros Completo", "0.0032"),
    COMPREHENSIVE_THIRD_PARTY_HAIL("Terceros Completo con Granizo", "0.0038"),
    ALL_RISK_WITH_DEDUCTIBLE("Todo Riesgo con franquicia", "0.0054");

    private final String label;
    private final BigDecimal monthlyFactor;

    CoverageType(String label, String monthlyFactor) {
        this.label = label;
        this.monthlyFactor = new BigDecimal(monthlyFactor);
    }

    public String getLabel() {
        return label;
    }

    public BigDecimal getMonthlyFactor() {
        return monthlyFactor;
    }
}
