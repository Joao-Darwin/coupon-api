package com.coupon.api.services;

import com.coupon.api.dtos.coupons.request.CreateCouponDTO;
import com.coupon.api.dtos.coupons.response.CouponDTO;
import com.coupon.api.exceptions.BadRequestException;
import com.coupon.api.exceptions.NotFoundException;
import com.coupon.api.models.Coupon;
import com.coupon.api.models.enums.CouponStatus;
import com.coupon.api.repositories.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class CouponService {

    private static final int CODE_LENGTH = 6;

    private final CouponRepository couponRepository;

    @Autowired
    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public CouponDTO create(CreateCouponDTO createCouponDTO) {
        String cleanCode = removeSpecialCharacters(createCouponDTO.code());

        if (cleanCode.length() != CODE_LENGTH) {
            throw new BadRequestException("Código do cupom deve igual a 6 caracteres alfanuméricos");
        }

        Coupon couponToCreate = new Coupon();

        couponToCreate.setCode(cleanCode);
        couponToCreate.setDescription(createCouponDTO.description());
        couponToCreate.setDiscountValue(BigDecimal.valueOf(createCouponDTO.discountValue()));
        couponToCreate.setExpirationDate(createCouponDTO.expirationDate());
        couponToCreate.setPublished(createCouponDTO.published());

        Coupon couponCreated = couponRepository.save(couponToCreate);

        return new CouponDTO(
                couponCreated.getId(),
                couponCreated.getCode(),
                couponCreated.getDescription(),
                couponCreated.getDiscountValue().doubleValue(),
                couponCreated.getExpirationDate(),
                couponCreated.getStatus(),
                couponCreated.isPublished(),
                couponCreated.isRedeemed()
        );
    }

    private String removeSpecialCharacters(String code) {
        return code.replaceAll("[^A-Za-z0-9]", "");
    }

    public CouponDTO findById(UUID id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cupom não encontrado"));

        return new CouponDTO(
                coupon.getId(),
                coupon.getCode(),
                coupon.getDescription(),
                coupon.getDiscountValue().doubleValue(),
                coupon.getExpirationDate(),
                coupon.getStatus(),
                coupon.isPublished(),
                coupon.isRedeemed()
        );
    }

    public void delete(UUID id) {
        Coupon coupon = couponRepository.findByIdAndIsNotDelete(id)
                .orElseThrow(() -> new NotFoundException("Cupom não encontrado ou já excluído"));

        coupon.setStatus(CouponStatus.DELETED);

        couponRepository.save(coupon);
    }
}
