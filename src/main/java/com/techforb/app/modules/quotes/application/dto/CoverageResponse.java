package com.techforb.app.modules.quotes.application.dto;

import com.techforb.app.modules.quotes.domain.Quote;
import java.math.BigDecimal;

public record CoverageResponse(Quote.CoverageType type, BigDecimal monthlyPremium) {

    public static CoverageResponse from(Quote.Coverage coverage) {
        return new CoverageResponse(coverage.type(), coverage.monthlyPremium());
    }
}
