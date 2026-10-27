package com.techforb.app.modules.quotes.application.dto;

import com.techforb.app.modules.quotes.domain.Coverage;
import com.techforb.app.modules.quotes.domain.CoveragePremium;
import java.math.BigDecimal;

public record CoverageResponse(Coverage coverage, BigDecimal monthlyPremium) {

    public static CoverageResponse from(CoveragePremium premium) {
        return new CoverageResponse(premium.coverage(), premium.monthlyPremium());
    }
}
