package com.inditex.coreplatform.price_service.infrastructure.rest.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.MissingRequestValueException;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.http.server.RequestPath;

import com.inditex.coreplatform.price_service.application.exceptions.MissingPriceApplicationRequestParamException;
import com.inditex.coreplatform.price_service.infrastructure.rest.controllers.dtos.ErrorPriceResponse;
import com.inditex.coreplatform.price_service.infrastructure.rest.exceptions.GlobalExceptionHandler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    private ServerHttpRequest mockRequest(String path) {
        ServerHttpRequest request = mock(ServerHttpRequest.class);
        RequestPath requestPath = mock(RequestPath.class);

        when(requestPath.value()).thenReturn(path);
        when(request.getPath()).thenReturn(requestPath);

        return request;
    }

    @Test
    void handleWebInputException_withTypeMismatch_returnsDetailedErrorResponse() {
        TypeMismatchException cause = mock(TypeMismatchException.class);
        when(cause.getPropertyName()).thenReturn("productId");
        when(cause.getValue()).thenReturn("abc");
        when(cause.getRequiredType()).thenReturn((Class) Integer.class);

        MethodParameter parameter = mock(MethodParameter.class);
        ServerWebInputException ex = new ServerWebInputException("Invalid parameter", parameter, cause);

        ServerHttpRequest request = mockRequest("/prices");

        ResponseEntity<ErrorPriceResponse> response = handler.handleWebInputException(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorPriceResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(400, body.status());
        assertEquals("Bad Request", body.error());
        assertEquals("/prices", body.path());
        assertEquals("Invalid parameter 'productId': 'abc'. Expected type is Integer.", body.message());
    }

    @Test
    void handleWebInputException_withNullFields_returnsUnknown() {
        TypeMismatchException cause = mock(TypeMismatchException.class);
        when(cause.getPropertyName()).thenReturn(null);
        when(cause.getValue()).thenReturn(null);
        when(cause.getRequiredType()).thenReturn(null);

        MethodParameter parameter = mock(MethodParameter.class);
        ServerWebInputException ex = new ServerWebInputException("Invalid parameter", parameter, cause);

        ServerHttpRequest request = mockRequest("/prices");

        ResponseEntity<ErrorPriceResponse> response = handler.handleWebInputException(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorPriceResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(400, body.status());
        assertEquals("Bad Request", body.error());
        assertEquals("/prices", body.path());
        assertEquals("Invalid parameter 'unknown': 'unknown'. Expected type is unknown.", body.message());
    }

    @Test
    void handleWebInputException_withOtherCause_returnsGenericReason() {
        Throwable otherCause = new IllegalArgumentException("invalid param");
        MethodParameter parameter = mock(MethodParameter.class);
        ServerWebInputException ex = new ServerWebInputException("Invalid parameter", parameter, otherCause);

        ServerHttpRequest request = mockRequest("/prices");

        ResponseEntity<ErrorPriceResponse> response = handler.handleWebInputException(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorPriceResponse body = response.getBody();
        assertNotNull(body);
        assertEquals("Invalid parameter: Invalid parameter", body.message());
    }

    @Test
    void handleMissingParams_returnsBadRequest() {
        MissingRequestValueException ex = mock(MissingRequestValueException.class);
        when(ex.getName()).thenReturn("applicationDate");

        ServerHttpRequest request = mockRequest("/prices");

        ResponseEntity<ErrorPriceResponse> response = handler.handleMissingParams(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorPriceResponse body = response.getBody();
        assertNotNull(body);
        assertEquals("Missing required parameter 'applicationDate'.", body.message());
        assertEquals("/prices", body.path());
    }

    @Test
    void handleConstraintViolation_returnsBadRequestWithMessage() {
        @SuppressWarnings("unchecked")
        ConstraintViolation<Object> violation = mock(ConstraintViolation.class);
        Path mockPath = mock(Path.class);
        when(mockPath.toString()).thenReturn("param");
        when(violation.getPropertyPath()).thenReturn(mockPath);
        when(violation.getMessage()).thenReturn("must not be null");

        Set<ConstraintViolation<?>> violations = Collections.singleton(violation);
        ConstraintViolationException ex = new ConstraintViolationException(violations);

        ServerHttpRequest request = mockRequest("/prices");

        ResponseEntity<ErrorPriceResponse> response = handler.handleConstraintViolation(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorPriceResponse body = response.getBody();
        assertNotNull(body);
        assertTrue(body.message().contains("'param' must not be null"));
    }

    @Test
    void handleConstraintViolation_returnsDefaultMessageIfNoViolations() {
        ConstraintViolationException ex = new ConstraintViolationException(Collections.emptySet());

        ServerHttpRequest request = mockRequest("/prices");

        ResponseEntity<ErrorPriceResponse> response = handler.handleConstraintViolation(ex, request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorPriceResponse body = response.getBody();
        assertNotNull(body);
        assertEquals("Invalid parameter.", body.message());
    }

    @Test
    void handleMissingPriceParam_withMessage_returnsBadRequest() {
        String errorMsg = "productId";
        MissingPriceApplicationRequestParamException ex = new MissingPriceApplicationRequestParamException(errorMsg);

        ServerHttpRequest request = mockRequest("/prices");

        ResponseEntity<ErrorPriceResponse> response = handler.handleMissingPriceParam(ex, request);
        String expectedMessage = "Missing required request parameter: productId";
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorPriceResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(expectedMessage, body.message());
    }

    @Test
    void handleMissingPriceParam_withNullMessage_returnsDefault() {
        MissingPriceApplicationRequestParamException ex = mock(MissingPriceApplicationRequestParamException.class);
        when(ex.getMessage()).thenReturn(null);

        ServerHttpRequest request = mockRequest("/prices");

        ResponseEntity<ErrorPriceResponse> response = handler.handleMissingPriceParam(ex, request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorPriceResponse body = response.getBody();
        assertNotNull(body);
        assertEquals("Missing required parameter for price application.", body.message());
    }

    @Test
    void handleAll_withGenericException_returnsInternalServerError() {
        Exception ex = new Exception("Unexpected error");

        ServerHttpRequest request = mockRequest("/prices");

        ResponseEntity<ErrorPriceResponse> response = handler.handleAll(ex, request);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ErrorPriceResponse body = response.getBody();
        assertNotNull(body);
        assertEquals("An unexpected error occurred.", body.message());
    }
}
