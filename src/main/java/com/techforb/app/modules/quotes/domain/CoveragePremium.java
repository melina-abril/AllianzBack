package com.techforb.app.modules.quotes.domain;

import java.math.BigDecimal;

public record CoveragePremium(Coverage coverage, BigDecimal monthlyPremium) {
}
