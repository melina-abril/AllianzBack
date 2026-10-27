package com.techforb.app.modules.quotes.domain;

import java.math.BigDecimal;

public record QuoteDiscount(String code, BigDecimal percentage) {
}
