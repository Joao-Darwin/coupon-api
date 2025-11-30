package com.coupon.api.services;

import com.coupon.api.dtos.coupons.request.CreateCouponDTO;
import com.coupon.api.dtos.coupons.response.CouponDTO;
import com.coupon.api.exceptions.BadRequestException;
import com.coupon.api.models.Coupon;
import com.coupon.api.models.enums.CouponStatus;
import com.coupon.api.repositories.CouponRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class CouponServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    private CouponService couponService;

    @Test
    void testCreate_GivenValidDTO_ShouldCreateAndReturnACouponDTO() {
        String code = "ABC-123";
        String cleanCode = "ABC123";
        String description = "Foo Bar";
        Double discountValue = 0.8;
        LocalDateTime expirationDate = LocalDateTime.now();
        boolean published = false;

        CreateCouponDTO createCouponDTO = new CreateCouponDTO(
                code,
                description,
                discountValue,
                expirationDate,
                published
        );

        Coupon couponExpected = new Coupon(
                UUID.randomUUID(),
                cleanCode,
                description,
                discountValue,
                expirationDate,
                LocalDateTime.now(),
                LocalDateTime.now(),
                CouponStatus.ACTIVE,
                published,
                false
        );

        Mockito.when(couponRepository.save(Mockito.any(Coupon.class))).thenReturn(couponExpected);

        CouponDTO actualCoupon = couponService.create(createCouponDTO);

        Assertions.assertEquals(couponExpected.getId(), actualCoupon.id());
        Assertions.assertEquals(cleanCode, actualCoupon.code());
        Assertions.assertEquals(description, actualCoupon.description());
        Assertions.assertEquals(discountValue, actualCoupon.discountValue());
        Assertions.assertEquals(expirationDate, actualCoupon.expirationDate());
        Assertions.assertEquals(CouponStatus.ACTIVE, actualCoupon.status());
        Assertions.assertFalse(actualCoupon.published());
        Assertions.assertFalse(actualCoupon.redeemed());

        Mockito.verify(couponRepository).save(Mockito.any(Coupon.class));
    }

    @Test
    void testCreate_GivenDTOWithInvalidCode_ShouldThrowABadRequestException() {
        String code = "ABCD-123";
        String description = "Foo Bar";
        Double discountValue = 0.8;
        LocalDateTime expirationDate = LocalDateTime.now();
        boolean published = false;
        String exceptionMessage = "Código do cupom deve igual a 6 caracteres alfanuméricos";

        CreateCouponDTO createCouponDTO = new CreateCouponDTO(
                code,
                description,
                discountValue,
                expirationDate,
                published
        );

        BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> couponService.create(createCouponDTO)
        );

        Assertions.assertEquals(exceptionMessage, exception.getMessage());

        Mockito.verify(couponRepository, Mockito.never()).save(Mockito.any(Coupon.class));
    }
}
