package com.coupon.api.controllers;

import com.coupon.api.controllers.coupons.impl.CouponController;
import com.coupon.api.dtos.coupons.request.CreateCouponDTO;
import com.coupon.api.dtos.coupons.response.CouponDTO;
import com.coupon.api.exceptions.BadRequestException;
import com.coupon.api.exceptions.NotFoundException;
import com.coupon.api.models.enums.CouponStatus;
import com.coupon.api.services.CouponService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.UUID;

@WebMvcTest(controllers = CouponController.class)
public class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Value("${base-path}/coupons")
    private String basePath;

    @MockitoBean
    private CouponService couponService;

    @Test
    void testCreate_ShouldReturnCreatedHttpCodeWithCreatedCouponOnBody() throws Exception {
        String code = "ABC-123";
        String cleanCode = "ABC123";
        String description = "Foo Bar";
        Double discountValue = 0.8;
        LocalDateTime expirationDate = LocalDateTime.of(2050, 12, 1, 5, 5, 15);
        CouponStatus status = CouponStatus.ACTIVE;
        boolean published = false;
        boolean redeemed = false;

        CreateCouponDTO createCouponDTO = new CreateCouponDTO(
                code,
                description,
                discountValue,
                expirationDate,
                published
        );

        CouponDTO couponDTO = new CouponDTO(
                UUID.randomUUID(),
                cleanCode,
                description,
                discountValue,
                expirationDate,
                status,
                published,
                redeemed
        );

        Mockito.when(couponService.create(Mockito.any(CreateCouponDTO.class))).thenReturn(couponDTO);

        ResultActions result = mockMvc
                .perform(MockMvcRequestBuilders.post(basePath)
                    .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCouponDTO)));

        result.andExpect(MockMvcResultMatchers.status().isCreated());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(couponDTO.id().toString()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(cleanCode));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.description").value(description));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.discountValue").value(discountValue));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.expirationDate").value(expirationDate.toString()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(status.toString()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.published").value(published));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.redeemed").value(redeemed));

        Mockito.verify(couponService).create(Mockito.any(CreateCouponDTO.class));
    }

    @Test
    void testFindById_GivenValidId_ShouldReturnOkHttpCodeWithCouponOnBody() throws Exception {
        UUID id = UUID.randomUUID();
        String code = "ABC123";
        String description = "Foo Bar";
        Double discountValue = 0.8;
        LocalDateTime expirationDate = LocalDateTime.of(2050, 12, 1, 5, 5, 15);
        CouponStatus status = CouponStatus.ACTIVE;
        boolean published = false;
        boolean redeemed = false;

        CouponDTO couponDTO = new CouponDTO(
                id,
                code,
                description,
                discountValue,
                expirationDate,
                status,
                published,
                redeemed
        );

        Mockito.when(couponService.findById(id)).thenReturn(couponDTO);

        ResultActions result = mockMvc
                .perform(MockMvcRequestBuilders.get(basePath + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isOk());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(couponDTO.id().toString()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(code));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.description").value(description));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.discountValue").value(discountValue));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.expirationDate").value(expirationDate.toString()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(status.toString()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.published").value(published));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.redeemed").value(redeemed));

        Mockito.verify(couponService).findById(id);
    }

    @Test
    void testFindById_GivenInvalidId_ShouldReturnBadRequestHttpCode() throws Exception {
        UUID id = UUID.randomUUID();
        String exceptionMessage = "Cupom não encontrado";

        Mockito.when(couponService.findById(id)).thenThrow(new NotFoundException(exceptionMessage));

        ResultActions result = mockMvc
                .perform(MockMvcRequestBuilders.get(basePath + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isNotFound());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.message").value(exceptionMessage));

        Mockito.verify(couponService).findById(id);
    }

    @Test
    void testDelete_GivenValidId_ShouldReturnNoContentCode() throws Exception {
        UUID id = UUID.randomUUID();

        Mockito.doNothing().when(couponService).delete(id);

        ResultActions result = mockMvc
                .perform(MockMvcRequestBuilders.delete(basePath + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(couponService).delete(id);
    }

    @Test
    void testDelete_GivenInvalidId_ShouldReturnBadRequestCode() throws Exception {
        UUID id = UUID.randomUUID();
        String exceptionMessage = "Cupom não encontrado ou já excluído";

        Mockito.doThrow(new NotFoundException(exceptionMessage)).when(couponService).delete(id);

        ResultActions result = mockMvc
                .perform(MockMvcRequestBuilders.delete(basePath + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isNotFound());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.message").value(exceptionMessage));

        Mockito.verify(couponService).delete(id);
    }

    @Test
    void testDelete_GivenIdFromDeletedCoupon_ShouldReturnBadRequestCode() throws Exception {
        UUID id = UUID.randomUUID();
        String exceptionMessage = "Cupom não encontrado ou já excluído";

        Mockito.doThrow(new BadRequestException(exceptionMessage)).when(couponService).delete(id);

        ResultActions result = mockMvc
                .perform(MockMvcRequestBuilders.delete(basePath + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.message").value(exceptionMessage));

        Mockito.verify(couponService).delete(id);
    }
}
