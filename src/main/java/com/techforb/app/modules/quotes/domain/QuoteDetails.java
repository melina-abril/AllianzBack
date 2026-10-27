package com.techforb.app.modules.quotes.domain;

import java.time.LocalDate;

public record QuoteDetails(String brand, String model, String version, int year, VehicleUse use,
        boolean gnc, String postalCode, LocalDate birthDate, int licenseYears,
        int claimsLastThreeYears, PaymentMethod paymentMethod, String dni) {
}
