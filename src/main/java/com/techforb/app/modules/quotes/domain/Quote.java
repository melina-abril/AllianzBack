package com.techforb.app.modules.quotes.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Cotización en memoria: calcula cuatro premios y mantiene inalterable el vencimiento original. */
public class Quote {

    public enum CoverageType {
        CIVIL_LIABILITY,
        COMPREHENSIVE_THIRD_PARTY,
        COMPREHENSIVE_THIRD_PARTY_WITH_HAIL,
        ALL_RISK_WITH_DEDUCTIBLE
    }

    public enum DiscountType {
        CLAIM_FREE,
        AUTOMATIC_DEBIT
    }

    public enum Status {
        SAVED
    }

    public record Coverage(CoverageType type, BigDecimal monthlyPremium) {
    }

    public record Discount(DiscountType type, BigDecimal rate) {
    }

    private final String id;
    private final QuoteData data;
    private final BigDecimal insuredValue;
    private final List<Coverage> coverages;
    private final List<Discount> discounts;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final LocalDate expiresOn;

    public Quote(String id, QuoteData data, VehicleOption vehicle, Instant createdAt, LocalDate createdOn) {
        this(id, data, vehicle, createdAt, createdAt, createdOn.plusDays(15));
    }

    private Quote(String id, QuoteData data, VehicleOption vehicle, Instant createdAt, Instant updatedAt, LocalDate expiresOn) {
        this.id = Objects.requireNonNull(id);
        this.data = Objects.requireNonNull(data);
        this.insuredValue = vehicle.insuredValue();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.expiresOn = expiresOn;

        List<Discount> applied = new ArrayList<>();
        if (data.claimsLastThreeYears() == 0) {
            applied.add(new Discount(DiscountType.CLAIM_FREE, new BigDecimal("0.10")));
        }
        if (data.paymentMethod() == PaymentMethod.AUTOMATIC_DEBIT) {
            applied.add(new Discount(DiscountType.AUTOMATIC_DEBIT, new BigDecimal("0.05")));
        }
        this.discounts = List.copyOf(applied);
        BigDecimal totalDiscount = discounts.stream().map(Discount::rate).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal riskFactor = BigDecimal.ONE;
        if (data.vehicleUse() == VehicleUse.COMMERCIAL) {
            riskFactor = riskFactor.multiply(new BigDecimal("1.25"));
        }
        if (data.hasCng()) {
            riskFactor = riskFactor.multiply(new BigDecimal("1.12"));
        }
        if (Period.between(data.birthDate(), expiresOn.minusDays(15)).getYears() < 25) {
            riskFactor = riskFactor.multiply(new BigDecimal("1.20"));
        }
        if (data.yearsLicensed() < 2) {
            riskFactor = riskFactor.multiply(new BigDecimal("1.10"));
        }
        riskFactor = riskFactor.multiply(BigDecimal.ONE.add(new BigDecimal("0.12")
                .multiply(BigDecimal.valueOf(data.claimsLastThreeYears()))));
        this.coverages = List.of(
                coverage(CoverageType.CIVIL_LIABILITY, "0.0030", riskFactor, totalDiscount),
                coverage(CoverageType.COMPREHENSIVE_THIRD_PARTY, "0.0050", riskFactor, totalDiscount),
                coverage(CoverageType.COMPREHENSIVE_THIRD_PARTY_WITH_HAIL, "0.0065", riskFactor, totalDiscount),
                coverage(CoverageType.ALL_RISK_WITH_DEDUCTIBLE, "0.0110", riskFactor, totalDiscount));
    }

    private Coverage coverage(CoverageType type, String monthlyRate, BigDecimal riskFactor, BigDecimal discount) {
        BigDecimal premium = insuredValue.multiply(new BigDecimal(monthlyRate)).multiply(riskFactor)
                .multiply(BigDecimal.ONE.subtract(discount)).setScale(2, RoundingMode.HALF_EVEN);
        return new Coverage(type, premium);
    }

    public Quote recalculate(QuoteData newData, VehicleOption vehicle, Instant now) {
        return new Quote(id, newData, vehicle, createdAt, now, expiresOn);
    }

    public boolean isExpired(LocalDate today) {
        return today.isAfter(expiresOn);
    }

    public String getId() {
        return id;
    }

    public QuoteData getData() {
        return data;
    }

    public BigDecimal getInsuredValue() {
        return insuredValue;
    }

    public List<Coverage> getCoverages() {
        return coverages;
    }

    public List<Discount> getDiscounts() {
        return discounts;
    }

    public Status getStatus() {
        return Status.SAVED;
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
