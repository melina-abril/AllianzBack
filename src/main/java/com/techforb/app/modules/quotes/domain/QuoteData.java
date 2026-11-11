package com.techforb.app.modules.quotes.domain;

import java.time.LocalDate;

/** Datos ingresados para una cotización; no dependen de HTTP ni de los DTOs. */
public record QuoteData(
        String dni,
        String brand,
        String model,
        String version,
        int year,
        VehicleUse vehicleUse,
        boolean hasCng,
        String postalCode,
        LocalDate birthDate,
        int yearsLicensed,
        int claimsLastThreeYears,
        PaymentMethod paymentMethod) {
}
