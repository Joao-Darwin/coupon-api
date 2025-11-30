package com.coupon.api.dtos.coupons.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateCouponDTO(
        @NotBlank(message = "Código do cupom é um campo obrigatório")
        String code,
        @NotBlank(message = "Descrição do cupom é um campo obrigatório")
        String description,
        @DecimalMin(value = "0.5", message = "O desconto deve ser no mínimo 0.5")
        @NotNull(message = "Valor do cupom é um campo obrigatório")
        Double discountValue,
        @Future(message = "A data de expiração deve ser uma data futura")
        LocalDateTime expirationDate,
        boolean published
) {
}
