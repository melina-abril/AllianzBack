package com.techforb.app.modules.quotes.application.dto;

import com.techforb.app.modules.quotes.domain.Quote;
import java.math.BigDecimal;

public record DiscountResponse(Quote.DiscountType type, BigDecimal rate) {

    public static DiscountResponse from(Quote.Discount discount) {
        return new DiscountResponse(discount.type(), discount.rate());
    }
}
