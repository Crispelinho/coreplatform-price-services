package com.inditex.coreplatform.price_service.infrastructure.rest.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.MissingRequestValueException;
import org.springframework.web.server.ServerWebInputException;

import com.inditex.coreplatform.price_service.application.exceptions.MissingPriceApplicationRequestParamException;
import com.inditex.coreplatform.price_service.infrastructure.rest.controllers.dtos.ErrorPriceResponse;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String UNKNOWN = "unknown";

    @ExceptionHandler(ServerWebInputException.class)
    public ResponseEntity<ErrorPriceResponse> handleWebInputException(ServerWebInputException ex, ServerHttpRequest request) {
        Throwable cause = ex.getCause();

        String message;
        if (cause instanceof org.springframework.beans.TypeMismatchException tmex) {
            String name = tmex.getPropertyName() != null ? tmex.getPropertyName() : UNKNOWN;
            Object value = tmex.getValue();
            Class<?> requiredType = tmex.getRequiredType();

            String typeName = requiredType != null ? requiredType.getSimpleName() : UNKNOWN;
            message = String.format("Invalid parameter '%s': '%s'. Expected type is %s.",
                    name,
                    value != null ? value : UNKNOWN,
                    typeName);
        } else {
            message = "Invalid parameter: " + ex.getReason();
        }

        return buildErrorResponse(HttpStatus.BAD_REQUEST, message, request);
    }

    @ExceptionHandler(MissingRequestValueException.class)
    public ResponseEntity<ErrorPriceResponse> handleMissingParams(MissingRequestValueException ex, ServerHttpRequest request) {
        String name = ex.getName();
        String message = String.format("Missing required parameter '%s'.", name);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, message, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorPriceResponse> handleConstraintViolation(ConstraintViolationException ex, ServerHttpRequest request) {
        String message = ex.getConstraintViolations()
                .stream()
                .map(v -> String.format("'%s' %s", v.getPropertyPath(), v.getMessage()))
                .findFirst()
                .orElse("Invalid parameter.");
        return buildErrorResponse(HttpStatus.BAD_REQUEST, message, request);
    }

    @ExceptionHandler(MissingPriceApplicationRequestParamException.class)
    public ResponseEntity<ErrorPriceResponse> handleMissingPriceParam(MissingPriceApplicationRequestParamException ex, ServerHttpRequest request) {
        String message = ex.getMessage() != null ? ex.getMessage()
                : "Missing required parameter for price application.";
        return buildErrorResponse(HttpStatus.BAD_REQUEST, message, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorPriceResponse> handleAll(Exception ex, ServerHttpRequest request) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.", request);
    }

    private ResponseEntity<ErrorPriceResponse> buildErrorResponse(HttpStatus status, String message, ServerHttpRequest request) {
        var error = new ErrorPriceResponse(
            status.value(),
            status.getReasonPhrase(),
            message,
            request.getPath().value(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(status).body(error);
    }
}
