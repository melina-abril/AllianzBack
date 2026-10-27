package com.techforb.app.modules.quotes.application.dto;

import com.techforb.app.modules.quotes.domain.PaymentMethod;
import com.techforb.app.modules.quotes.domain.Quote;
import com.techforb.app.modules.quotes.domain.VehicleUse;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/** Cotización guardada; los descuentos informados ya están incluidos en cada premio. */
public record QuoteResponse(
        String number, String status, Instant createdAt, LocalDate expiresOn,
        String brand, String model, String version, int year, VehicleUse use, boolean gnc,
        String postalCode, LocalDate birthDate, int licenseYears, int claimsLastThreeYears,
        PaymentMethod paymentMethod, String dni, BigDecimal insuredAmount,
        List<CoverageResponse> coverages, List<DiscountResponse> discounts) {

    public static QuoteResponse from(Quote quote) {
        var details = quote.getDetails();
        return new QuoteResponse(quote.getNumber(), "SAVED", quote.getCreatedAt(), quote.getExpiresOn(),
                details.brand(), details.model(), details.version(), details.year(), details.use(), details.gnc(),
                details.postalCode(), details.birthDate(), details.licenseYears(), details.claimsLastThreeYears(),
                details.paymentMethod(), details.dni(), quote.getInsuredAmount(),
                quote.getCoverages().stream().map(CoverageResponse::from).toList(),
                quote.getDiscounts().stream().map(DiscountResponse::from).toList());
    }
}
