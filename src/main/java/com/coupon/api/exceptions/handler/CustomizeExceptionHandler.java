package com.coupon.api.exceptions.handler;

import com.coupon.api.dtos.exceptions.response.ResponseEntityException;
import com.coupon.api.exceptions.BadRequestException;
import com.coupon.api.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@RestController
public class CustomizeExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseEntityException> handlerAllExceptions(Exception exception, WebRequest webRequest) {
        ResponseEntityException responseEntityException = new ResponseEntityException(Instant.now(), exception.getMessage(), webRequest.getDescription(false));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseEntityException);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResponseEntityException> handlerAllExceptions(BadRequestException exception, WebRequest webRequest) {
        ResponseEntityException responseEntityException = new ResponseEntityException(Instant.now(), exception.getMessage(), webRequest.getDescription(false));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseEntityException);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponseEntityException> handlerAllExceptions(NotFoundException exception, WebRequest webRequest) {
        ResponseEntityException responseEntityException = new ResponseEntityException(Instant.now(), exception.getMessage(), webRequest.getDescription(false));

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseEntityException);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("moment", Instant.now());
        response.put("message", "Algo está errado na validação dos campos");
        response.put("details", ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> {
                    Map<String, String> fieldError = new HashMap<>();
                    fieldError.put("field", error.getField());
                    fieldError.put("message", error.getDefaultMessage());
                    return fieldError;
                }).collect(Collectors.toList()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
