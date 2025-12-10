package com.coupon.api.controllers.coupons;

import com.coupon.api.dtos.coupons.request.CreateCouponDTO;
import com.coupon.api.dtos.coupons.response.CouponDTO;
import com.coupon.api.dtos.coupons.response.CouponRedeemDTO;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface ICouponController {
    ResponseEntity<CouponDTO> create(CreateCouponDTO createCouponDTO);
    ResponseEntity<CouponRedeemDTO> use(UUID id);
    ResponseEntity<CouponDTO> findById(UUID id);
    ResponseEntity<Void> delete(UUID id);
}
