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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
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
        BigDecimal discountValue = BigDecimal.valueOf(0.8);
        LocalDateTime expirationDate = LocalDateTime.now();
        boolean published = false;

        CreateCouponDTO createCouponDTO = new CreateCouponDTO(
                code,
                description,
                discountValue.doubleValue(),
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
        Assertions.assertEquals(discountValue.doubleValue(), actualCoupon.discountValue());
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

    @Test
    void testFindById_GivenValidId_ShouldReturnCoupon() {
        UUID id = UUID.randomUUID();
        String code = "ABC123";
        String description = "Foo Bar";
        BigDecimal discountValue = BigDecimal.valueOf(0.8);
        LocalDateTime expirationDate = LocalDateTime.now();
        CouponStatus status = CouponStatus.ACTIVE;
        boolean published = false;
        boolean redeemed = false;

        Coupon couponExpected = new Coupon(
                id,
                code,
                description,
                discountValue,
                expirationDate,
                LocalDateTime.now(),
                LocalDateTime.now(),
                status,
                published,
                redeemed
        );

        Mockito.when(couponRepository.findById(id)).thenReturn(Optional.of(couponExpected));

        CouponDTO actualCoupon = couponService.findById(id);

        Assertions.assertEquals(id, actualCoupon.id());
        Assertions.assertEquals(code, actualCoupon.code());
        Assertions.assertEquals(description, actualCoupon.description());
        Assertions.assertEquals(discountValue.doubleValue(), actualCoupon.discountValue());
        Assertions.assertEquals(expirationDate, actualCoupon.expirationDate());
        Assertions.assertEquals(status, actualCoupon.status());
        Assertions.assertFalse(actualCoupon.published());
        Assertions.assertFalse(actualCoupon.redeemed());

        Mockito.verify(couponRepository).findById(id);
    }

    @Test
    void testFindById_GivenInvalidId_ShouldThrowABadRequestException() {
        UUID id = UUID.randomUUID();
        String exceptionMessage = "Cupom não encontrado";

        Mockito.when(couponRepository.findById(id)).thenReturn(Optional.empty());

        BadRequestException badRequestException = Assertions.assertThrows(
                BadRequestException.class,
                () -> couponService.findById(id)
        );

        Assertions.assertEquals(exceptionMessage, badRequestException.getMessage());

        Mockito.verify(couponRepository).findById(id);
    }

    @Test
    void testDelete_GivenValidId_ShouldUpdateCouponStatusToDelete() {
        UUID id = UUID.randomUUID();
        String code = "ABC123";
        String description = "Foo Bar";
        BigDecimal discountValue = BigDecimal.valueOf(0.8);
        LocalDateTime expirationDate = LocalDateTime.now();
        CouponStatus status = CouponStatus.ACTIVE;
        boolean published = false;
        boolean redeemed = false;

        Coupon coupon = new Coupon(
                id,
                code,
                description,
                discountValue,
                expirationDate,
                LocalDateTime.now(),
                LocalDateTime.now(),
                status,
                published,
                redeemed
        );

        Coupon updatedCoupon = new Coupon(
                id,
                code,
                description,
                discountValue,
                expirationDate,
                LocalDateTime.now(),
                LocalDateTime.now(),
                CouponStatus.DELETED,
                published,
                redeemed
        );

        Mockito.when(couponRepository.findByIdAndIsNotDelete(id)).thenReturn(Optional.of(coupon));
        Mockito.when(couponRepository.save(Mockito.any(Coupon.class))).thenReturn(updatedCoupon);

        couponService.delete(id);

        Mockito.verify(couponRepository).findByIdAndIsNotDelete(id);
        Mockito.verify(couponRepository).save(Mockito.any(Coupon.class));
    }

    @Test
    void testDelete_GivenInvalidId_ShouldThrowABadRequestException() {
        UUID id = UUID.randomUUID();

        Mockito.when(couponRepository.findByIdAndIsNotDelete(id)).thenReturn(Optional.empty());

        BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> couponService.delete(id)
        );

        Assertions.assertEquals("Cupom não encontrado ou já excluído", exception.getMessage());

        Mockito.verify(couponRepository).findByIdAndIsNotDelete(id);
        Mockito.verify(couponRepository, Mockito.never()).save(Mockito.any(Coupon.class));
    }

    @Test
    void testDelete_GivenIdFromDeletedCoupon_ShouldThrowABadRequestException() {
        UUID id = UUID.randomUUID();

        Mockito.when(couponRepository.findByIdAndIsNotDelete(id)).thenReturn(Optional.empty());

        BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> couponService.delete(id)
        );

        Assertions.assertEquals("Cupom não encontrado ou já excluído", exception.getMessage());

        Mockito.verify(couponRepository).findByIdAndIsNotDelete(id);
        Mockito.verify(couponRepository, Mockito.never()).save(Mockito.any(Coupon.class));
    }
}
