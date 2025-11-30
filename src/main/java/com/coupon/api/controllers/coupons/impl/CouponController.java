package com.coupon.api.controllers.coupons.impl;

import com.coupon.api.controllers.coupons.ICouponController;
import com.coupon.api.dtos.coupons.request.CreateCouponDTO;
import com.coupon.api.dtos.coupons.response.CouponDTO;
import com.coupon.api.services.CouponService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("${base-path}/coupons")
public class CouponController implements ICouponController {

    private final CouponService couponService;

    @Autowired
    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping
    public ResponseEntity<CouponDTO> create(@Valid @RequestBody CreateCouponDTO createCouponDTO) {
        CouponDTO couponCreated = couponService.create(createCouponDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(couponCreated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CouponDTO> findById(@PathVariable UUID id) {
        CouponDTO coupon = couponService.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(coupon);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        couponService.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
