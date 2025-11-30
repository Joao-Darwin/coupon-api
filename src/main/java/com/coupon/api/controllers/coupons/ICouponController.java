package com.coupon.api.controllers.coupons;

import com.coupon.api.dtos.coupons.request.CreateCouponDTO;
import com.coupon.api.dtos.coupons.response.CouponDTO;
import org.springframework.http.ResponseEntity;

public interface ICouponController {
    ResponseEntity<CouponDTO> create(CreateCouponDTO createCouponDTO);
}
