package com.techforb.app.modules.quotes.domain;

import java.time.LocalDate;

public record QuoteInput(
        Vehicle vehicle,
        VehicleUse use,
        boolean gnc,
        String postalCode,
        LocalDate birthDate,
        int registrationYears,
        int claimsLastThreeYears,
        PaymentMethod paymentMethod,
        String dni) {
}
