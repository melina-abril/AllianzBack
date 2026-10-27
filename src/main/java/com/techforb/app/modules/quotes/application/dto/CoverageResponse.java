package com.techforb.app.modules.quotes.application.dto;

import com.techforb.app.modules.quotes.domain.Coverage;
import com.techforb.app.modules.quotes.domain.CoverageType;
import java.math.BigDecimal;

public record CoverageResponse(CoverageType type, String name, BigDecimal monthlyPremium) {

    public static CoverageResponse from(Coverage coverage) {
        return new CoverageResponse(coverage.type(), coverage.type().getLabel(), coverage.monthlyPremium());
    }
}
