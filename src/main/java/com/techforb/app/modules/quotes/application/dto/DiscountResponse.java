package com.techforb.app.modules.quotes.application.dto;

import com.techforb.app.modules.quotes.domain.DiscountType;
import java.math.BigDecimal;

public record DiscountResponse(DiscountType type, String name, BigDecimal rate) {

    public static DiscountResponse from(DiscountType discount) {
        return new DiscountResponse(discount, discount.getLabel(), discount.getRate());
    }
}
