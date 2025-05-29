package com.inditex.coreplatform.price_service.domain.ports;

import java.time.LocalDateTime;

import com.inditex.coreplatform.price_service.domain.models.Price;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IPriceService {
    /**
     * Retrieves a price by product ID, brand ID, and start date.
     *
     * @param productId the product ID
     * @param brandId   the brand ID
     * @param startDate the start date
     * @return a Mono containing the price
     */
    Mono<Price> getPriceByProductAndBrandIdAndApplicationDate(
            Integer productId,
            Integer brandId,
            LocalDateTime applicationDate);

    Flux<Price> getAllPrices();

}
