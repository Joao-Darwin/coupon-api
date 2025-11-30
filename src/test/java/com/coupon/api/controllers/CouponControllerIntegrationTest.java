package com.coupon.api.controllers;

import com.coupon.api.dtos.coupons.request.CreateCouponDTO;
import com.coupon.api.dtos.coupons.response.CouponDTO;
import com.coupon.api.models.enums.CouponStatus;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CouponControllerIntegrationTest {

    private static String basePath;
    private static RequestSpecification requestSpecification;
    private static String code;
    private static String description;
    private static Double discountValue;
    private static LocalDateTime expirationDate;
    private static CreateCouponDTO couponToCreate;

    @BeforeAll
    void setUp() {
        basePath = "/api/v1/coupons";

        requestSpecification = new RequestSpecBuilder()
                .setBasePath(basePath)
                .setPort(8888)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        code = "ABC123";
        description = "Foo Bar";
        discountValue = 0.8;
        expirationDate = LocalDateTime.of(2050, 12, 1, 5, 5, 15);
        couponToCreate = new CreateCouponDTO(
                code,
                description,
                discountValue,
                expirationDate,
                false
        );
    }

    @Test
    void integrationTestCreateCoupon_GivenValidCoupon_ShouldCreateAndReturnCreatedCoupon() {
        CouponStatus status = CouponStatus.ACTIVE;

        CouponDTO response = RestAssured
                .given()
                .spec(requestSpecification)
                .contentType(ContentType.JSON)
                .body(couponToCreate)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .body()
                .as(CouponDTO.class);

        Assertions.assertEquals(code, response.code());
        Assertions.assertEquals(description, response.description());
        Assertions.assertEquals(discountValue, response.discountValue().doubleValue());
        Assertions.assertEquals(expirationDate, response.expirationDate());
        Assertions.assertEquals(status, response.status());
        Assertions.assertFalse(response.published());
        Assertions.assertFalse(response.redeemed());
    }

    @Test
    void integrationTestFindCouponById_GivenValidId_ShouldReturnACoupon() {
        CouponStatus status = CouponStatus.ACTIVE;

        CouponDTO created = RestAssured
                .given()
                .spec(requestSpecification)
                .contentType(ContentType.JSON)
                .body(couponToCreate)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(CouponDTO.class);

        CouponDTO response = RestAssured
                .given()
                .spec(requestSpecification)
                .basePath(basePath + "/" + created.id())
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(CouponDTO.class);

        Assertions.assertEquals(code, response.code());
        Assertions.assertEquals(description, response.description());
        Assertions.assertEquals(discountValue, response.discountValue().doubleValue());
        Assertions.assertEquals(expirationDate, response.expirationDate());
        Assertions.assertEquals(status, response.status());
        Assertions.assertFalse(response.published());
        Assertions.assertFalse(response.redeemed());
    }

    @Test
    void integrationTestDelete_GivenValidId_ShouldDeleteCouponAndReturnNoContent() {
        CouponDTO created = RestAssured
                .given()
                .spec(requestSpecification)
                .contentType(ContentType.JSON)
                .body(couponToCreate)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(CouponDTO.class);

        RestAssured
                .given()
                .spec(requestSpecification)
                .basePath(basePath + "/" + created.id())
                .when()
                .delete()
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
