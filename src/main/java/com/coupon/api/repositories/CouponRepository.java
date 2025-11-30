package com.coupon.api.repositories;

import com.coupon.api.models.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, UUID> {

    @Query("SELECT coupon FROM Coupon coupon WHERE coupon.id = :id AND coupon.status != 'DELETED'")
    Optional<Coupon> findByIdAndIsNotDelete(UUID id);
}
