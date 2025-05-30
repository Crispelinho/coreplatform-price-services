package com.inditex.coreplatform.price_service.application.usecases.queries;

import java.time.LocalDateTime;

public record GetApplicablePriceQuery(
    Integer productId,
    Integer brandId,
    LocalDateTime applicationDate
) {}