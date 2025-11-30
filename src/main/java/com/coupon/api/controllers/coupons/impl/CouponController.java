package com.coupon.api.controllers.coupons.impl;

import com.coupon.api.controllers.coupons.ICouponController;
import com.coupon.api.services.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${base-path}/coupons")
public class CouponController implements ICouponController {

    private final CouponService couponService;

    @Autowired
    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }
}
