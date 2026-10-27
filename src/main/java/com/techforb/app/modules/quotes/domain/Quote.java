package com.techforb.app.modules.quotes.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Cotización guardada: la actualización conserva número, creación y vencimiento. */
public class Quote {

    private final String number;
    private final Instant createdAt;
    private final LocalDate expiresOn;
    private QuoteDetails details;
    private BigDecimal insuredAmount;
    private List<CoveragePremium> coverages;
    private List<QuoteDiscount> discounts;

    private Quote(String number, Instant createdAt, LocalDate expiresOn) {
        this.number = Objects.requireNonNull(number);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.expiresOn = Objects.requireNonNull(expiresOn);
    }

    public static Quote create(String number, QuoteDetails details, VehicleOption vehicle,
            Instant createdAt, LocalDate expiresOn) {
        Quote quote = new Quote(number, createdAt, expiresOn);
        quote.update(details, vehicle);
        return quote;
    }

    public void update(QuoteDetails newDetails, VehicleOption vehicle) {
        List<QuoteDiscount> newDiscounts = new ArrayList<>();
        if (newDetails.claimsLastThreeYears() == 0) {
            newDiscounts.add(new QuoteDiscount("NO_CLAIMS", new BigDecimal("0.10")));
        }
        if (newDetails.paymentMethod() == PaymentMethod.AUTO_DEBIT) {
            newDiscounts.add(new QuoteDiscount("AUTO_DEBIT", new BigDecimal("0.05")));
        }
        BigDecimal discountRate = newDiscounts.stream().map(QuoteDiscount::percentage)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal riskFactor = BigDecimal.ONE;
        if (newDetails.use() == VehicleUse.COMMERCIAL) {
            riskFactor = riskFactor.add(new BigDecimal("0.20"));
        }
        if (newDetails.gnc()) {
            riskFactor = riskFactor.add(new BigDecimal("0.08"));
        }
        riskFactor = riskFactor.add(new BigDecimal("0.12")
                .multiply(BigDecimal.valueOf(newDetails.claimsLastThreeYears())));
        if (newDetails.licenseYears() < 2) {
            riskFactor = riskFactor.add(new BigDecimal("0.10"));
        }
        List<CoveragePremium> newCoverages = new ArrayList<>();
        newCoverages.add(premium(Coverage.CIVIL_LIABILITY, "0.0020", vehicle, riskFactor, discountRate));
        newCoverages.add(premium(Coverage.COMPREHENSIVE_THIRD_PARTY, "0.0035", vehicle, riskFactor, discountRate));
        newCoverages.add(premium(Coverage.COMPREHENSIVE_THIRD_PARTY_WITH_HAIL, "0.0043", vehicle, riskFactor, discountRate));
        newCoverages.add(premium(Coverage.ALL_RISK_WITH_DEDUCTIBLE, "0.0065", vehicle, riskFactor, discountRate));
        this.details = newDetails;
        this.insuredAmount = vehicle.insuredAmount().setScale(2, RoundingMode.HALF_EVEN);
        this.discounts = List.copyOf(newDiscounts);
        this.coverages = List.copyOf(newCoverages);
    }

    private CoveragePremium premium(Coverage coverage, String rate, VehicleOption vehicle,
            BigDecimal riskFactor, BigDecimal discountRate) {
        BigDecimal amount = vehicle.insuredAmount().multiply(new BigDecimal(rate))
                .multiply(riskFactor).multiply(BigDecimal.ONE.subtract(discountRate))
                .setScale(2, RoundingMode.HALF_EVEN);
        return new CoveragePremium(coverage, amount);
    }

    public String getNumber() { return number; }
    public Instant getCreatedAt() { return createdAt; }
    public LocalDate getExpiresOn() { return expiresOn; }
    public QuoteDetails getDetails() { return details; }
    public BigDecimal getInsuredAmount() { return insuredAmount; }
    public List<CoveragePremium> getCoverages() { return coverages; }
    public List<QuoteDiscount> getDiscounts() { return discounts; }
}
