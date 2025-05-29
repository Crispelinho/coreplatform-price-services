package com.inditex.coreplatform.price_service.application.usecases;

import com.inditex.coreplatform.price_service.domain.models.Price;
import com.inditex.coreplatform.price_service.domain.ports.IPriceService;

import reactor.core.publisher.Flux;

/**
 * Use case for retrieving all prices.
 * This use case interacts with the price service to fetch all available prices.
 */
public class GetPricesUseCase {
    private final IPriceService priceService;

    public GetPricesUseCase(IPriceService priceService) {
        this.priceService = priceService;
    }

    public Flux<Price> execute() {
        return priceService.getAllPrices();
    }

}
