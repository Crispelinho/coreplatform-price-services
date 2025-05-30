package com.inditex.coreplatform.price_service.domain.ports;

import java.time.LocalDateTime;

import com.inditex.coreplatform.price_service.domain.models.Price;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IPriceRepository {
    /**
     * Finds a price by product ID, brand ID, and start date.
     *
     * @param productId the product ID
     * @param brandId   the brand ID
     * @param startDate the start date
     * @return the price
     */
    Mono<Price> findTopByProductIdAndBrandIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(
            Integer productId,
            Integer brandId,
            LocalDateTime startDate,
            LocalDateTime endDate);

    Flux<Price> findAll();
}
