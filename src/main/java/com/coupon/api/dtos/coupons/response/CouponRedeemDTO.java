package com.coupon.api.dtos.coupons.response;

import java.util.UUID;

public record CouponRedeemDTO(
        UUID id,
        boolean redeemed
) {
}
