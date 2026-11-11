package com.techforb.app.modules.quotes.application.dto;

import com.techforb.app.modules.quotes.domain.PaymentMethod;
import com.techforb.app.modules.quotes.domain.Quote;
import com.techforb.app.modules.quotes.domain.VehicleUse;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public record QuoteResponse(
        String id,
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
        PaymentMethod paymentMethod,
        BigDecimal insuredValue,
        List<CoverageResponse> coverages,
        List<DiscountResponse> discounts,
        Quote.Status status,
        Instant createdAt,
        Instant updatedAt,
        LocalDate expiresOn) {

    public static QuoteResponse from(Quote quote) {
        var data = quote.getData();
        return new QuoteResponse(quote.getId(), data.dni(), data.brand(), data.model(), data.version(),
                data.year(), data.vehicleUse(), data.hasCng(), data.postalCode(), data.birthDate(),
                data.yearsLicensed(), data.claimsLastThreeYears(), data.paymentMethod(), quote.getInsuredValue(),
                quote.getCoverages().stream().map(CoverageResponse::from).toList(),
                quote.getDiscounts().stream().map(DiscountResponse::from).toList(), quote.getStatus(),
                quote.getCreatedAt(), quote.getUpdatedAt(), quote.getExpiresOn());
    }
}
