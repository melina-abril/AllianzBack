package com.techforb.app.modules.quotes.application.dto;

import com.techforb.app.modules.quotes.domain.PaymentMethod;
import com.techforb.app.modules.quotes.domain.Quote;
import com.techforb.app.modules.quotes.domain.QuoteInput;
import com.techforb.app.modules.quotes.domain.VehicleUse;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/** Resultado guardado: los descuentos indicados ya están incluidos en cada premio mensual. */
public record QuoteResponse(
        String id,
        String number,
        String status,
        VehicleResponse vehicle,
        VehicleUse use,
        boolean gnc,
        String postalCode,
        LocalDate birthDate,
        int registrationYears,
        int claimsLastThreeYears,
        PaymentMethod paymentMethod,
        String dni,
        BigDecimal insuredAmount,
        List<CoverageResponse> coverages,
        List<DiscountResponse> discounts,
        Instant createdAt,
        Instant updatedAt,
        LocalDate expiresOn) {

    public static QuoteResponse from(Quote quote) {
        QuoteInput input = quote.getInput();
        return new QuoteResponse(quote.getId(), quote.getId(), "SAVED", VehicleResponse.from(input.vehicle()),
                input.use(), input.gnc(), input.postalCode(), input.birthDate(), input.registrationYears(),
                input.claimsLastThreeYears(), input.paymentMethod(), input.dni(), quote.getInsuredAmount(),
                quote.getCoverages().stream().map(CoverageResponse::from).toList(),
                quote.getDiscounts().stream().map(DiscountResponse::from).toList(),
                quote.getCreatedAt(), quote.getUpdatedAt(), quote.getExpiresOn());
    }
}
