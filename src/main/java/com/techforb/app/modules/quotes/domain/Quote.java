package com.techforb.app.modules.quotes.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/** Cotización guardada; recalcular no modifica el número, la creación ni el vencimiento. */
public class Quote {

    private final String id;
    private final Instant createdAt;
    private final LocalDate expiresOn;
    private QuoteInput input;
    private BigDecimal insuredAmount;
    private List<Coverage> coverages;
    private List<DiscountType> discounts;
    private Instant updatedAt;

    public Quote(String id, QuoteInput input, Instant createdAt, LocalDate expiresOn, LocalDate pricingDate) {
        this.id = Objects.requireNonNull(id);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.expiresOn = Objects.requireNonNull(expiresOn);
        recalculate(input, createdAt, pricingDate);
    }

    public void recalculate(QuoteInput newInput, Instant updatedAt, LocalDate pricingDate) {
        Objects.requireNonNull(newInput);
        List<DiscountType> newDiscounts = new ArrayList<>();
        if (newInput.claimsLastThreeYears() == 0) {
            newDiscounts.add(DiscountType.NO_CLAIMS);
        }
        if (newInput.paymentMethod() == PaymentMethod.AUTOMATIC_DEBIT) {
            newDiscounts.add(DiscountType.AUTOMATIC_DEBIT);
        }
        BigDecimal discountRate = newDiscounts.stream().map(DiscountType::getRate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal riskFactor = BigDecimal.ONE;
        if (newInput.use() == VehicleUse.COMMERCIAL) {
            riskFactor = riskFactor.add(new BigDecimal("0.30"));
        }
        if (newInput.gnc()) {
            riskFactor = riskFactor.add(new BigDecimal("0.12"));
        }
        riskFactor = riskFactor.add(new BigDecimal("0.15")
                .multiply(BigDecimal.valueOf(newInput.claimsLastThreeYears())));
        if (Period.between(newInput.birthDate(), pricingDate).getYears() < 25) {
            riskFactor = riskFactor.add(new BigDecimal("0.20"));
        }
        if (pricingDate.getYear() - newInput.vehicle().year() > 10) {
            riskFactor = riskFactor.add(new BigDecimal("0.10"));
        }
        BigDecimal finalRiskFactor = riskFactor;
        BigDecimal payableRate = BigDecimal.ONE.subtract(discountRate);
        List<Coverage> newCoverages = Arrays.stream(CoverageType.values())
                .map(type -> new Coverage(type, newInput.vehicle().insuredValue()
                        .multiply(type.getMonthlyFactor()).multiply(finalRiskFactor).multiply(payableRate)
                        .setScale(2, RoundingMode.HALF_EVEN)))
                .toList();
        this.input = newInput;
        this.insuredAmount = newInput.vehicle().insuredValue();
        this.discounts = List.copyOf(newDiscounts);
        this.coverages = newCoverages;
        this.updatedAt = Objects.requireNonNull(updatedAt);
    }

    public String getId() {
        return id;
    }

    public QuoteInput getInput() {
        return input;
    }

    public BigDecimal getInsuredAmount() {
        return insuredAmount;
    }

    public List<Coverage> getCoverages() {
        return coverages;
    }

    public List<DiscountType> getDiscounts() {
        return discounts;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public LocalDate getExpiresOn() {
        return expiresOn;
    }
}
