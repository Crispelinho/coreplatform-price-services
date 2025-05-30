package com.inditex.coreplatform.price_service.application.exceptions;

public class MissingPriceApplicationRequestParamException extends RuntimeException {
    public MissingPriceApplicationRequestParamException(String paramName) {
        super("Missing required request parameter: " + paramName);
    }
}
