package com.techforb.app.modules.quotes.application.dto;

import com.techforb.app.modules.quotes.domain.QuoteDiscount;
import java.math.BigDecimal;

public record DiscountResponse(String code, BigDecimal percentage) {

    public static DiscountResponse from(QuoteDiscount discount) {
        return new DiscountResponse(discount.code(), discount.percentage());
    }
}
