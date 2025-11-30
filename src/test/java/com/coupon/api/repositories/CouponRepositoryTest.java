package com.coupon.api.repositories;

import com.coupon.api.models.Coupon;
import com.coupon.api.models.enums.CouponStatus;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@DataJpaTest
public class CouponRepositoryTest {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private EntityManager entityManager;

    private Coupon coupon;

    @BeforeEach
    void setUp() {
        coupon = new Coupon(
                null,
                "ABC123",
                "Foo Bar",
                BigDecimal.valueOf(0.5),
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                CouponStatus.ACTIVE,
                false,
                false
        );

        entityManager.persist(coupon);
    }

    @Test
    void testFindByIdAndIsNotDelete_GivenValidId_ShouldReturnAnOptionalWithCoupon() {
        UUID id = coupon.getId();

        Optional<Coupon> actualCoupon = couponRepository.findByIdAndIsNotDelete(id);

        Assertions.assertTrue(actualCoupon.isPresent());
    }

    @Test
    void testFindByIdAndIsNotDelete_GivenInvalidId_ShouldReturnAnEmptyOptional() {
        UUID id = UUID.randomUUID();

        Optional<Coupon> actualCoupon = couponRepository.findByIdAndIsNotDelete(id);

        Assertions.assertTrue(actualCoupon.isEmpty());
    }

    @Test
    void testFindByIdAndIsNotDelete_GivenIdFromDeletedCoupon_ShouldReturnAnEmptyOptional() {
        UUID id = coupon.getId();

        coupon.setStatus(CouponStatus.DELETED);

        entityManager.persist(coupon);

        Optional<Coupon> actualCoupon = couponRepository.findByIdAndIsNotDelete(id);

        Assertions.assertTrue(actualCoupon.isEmpty());
    }
}
