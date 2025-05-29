package com.inditex.coreplatform.price_service.application.usecases;

import com.inditex.coreplatform.price_service.domain.models.Price;
import com.inditex.coreplatform.price_service.domain.ports.IPriceService;
import com.inditex.coreplatform.price_service.application.usecases.queries.GetApplicablePriceQuery;

import reactor.core.publisher.Mono;

/**
 * Use case for retrieving the applicable price based on product ID, brand ID, and application date.
 */

public class GetApplicablePriceUseCase {
    private final IPriceService priceService;

    public GetApplicablePriceUseCase(IPriceService priceService) {
        this.priceService = priceService;
    }

    public Mono<Price> execute(GetApplicablePriceQuery query) {
        return priceService.getPriceByProductAndBrandIdAndApplicationDate(
            query.productId(),
            query.brandId(),
            query.applicationDate()
        );
    }
}
