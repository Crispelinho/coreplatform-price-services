package com.inditex.coreplatform.price_service.application.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;


class MissingPriceApplicationRequestParamExceptionTest {

    @Test
    void testExceptionMessageContainsParamName() {
        String paramName = "startDate";
        MissingPriceApplicationRequestParamException exception =
                new MissingPriceApplicationRequestParamException(paramName);

        assertEquals("Missing required request parameter: startDate", exception.getMessage());
    }

    @Test
    void testExceptionIsInstanceOfRuntimeException() {
        MissingPriceApplicationRequestParamException exception =
                new MissingPriceApplicationRequestParamException("brandId");

        assertTrue(exception instanceof RuntimeException);
    }
}