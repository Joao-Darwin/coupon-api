package com.coupon.api.dtos.coupons.response;

import com.coupon.api.models.enums.CouponStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record CouponDTO(
        UUID id,
        String code,
        String description,
        Double discountValue,
        LocalDateTime expirationDate,
        CouponStatus status,
        boolean published,
        boolean redeemed
) {
}
