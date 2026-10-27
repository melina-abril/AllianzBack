package com.techforb.app.modules.quotes.domain;

import java.math.BigDecimal;

public record Coverage(CoverageType type, BigDecimal monthlyPremium) {
}
