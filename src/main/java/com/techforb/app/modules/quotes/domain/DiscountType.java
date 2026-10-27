package com.techforb.app.modules.quotes.domain;

import java.math.BigDecimal;

public enum DiscountType {
    NO_CLAIMS("Sin siniestros en los últimos 3 años", "0.10"),
    AUTOMATIC_DEBIT("Pago con débito automático", "0.05");

    private final String label;
    private final BigDecimal rate;

    DiscountType(String label, String rate) {
        this.label = label;
        this.rate = new BigDecimal(rate);
    }

    public String getLabel() {
        return label;
    }

    public BigDecimal getRate() {
        return rate;
    }
}
