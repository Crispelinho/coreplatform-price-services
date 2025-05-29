package com.inditex.coreplatform.price_service.infrastructure.persistence.repositories;

import java.time.LocalDateTime;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.inditex.coreplatform.price_service.infrastructure.persistence.entities.PriceEntity;

import reactor.core.publisher.Mono;

public interface IReactivePriceRepository extends ReactiveCrudRepository<PriceEntity, Long> {

    /**
     * Finds a price by product ID, brand ID, start date and end date.
     *
     * @param productId the product ID
     * @param brandId   the brand ID
     * @param startDate the start date
     * @param endDate   the end date
     * @return the price
     */

    // @Query("SELECT * FROM prices WHERE product_id = :productId AND brand_id =
    // :brandId AND start_date <= :startDate AND end_date >= :startDate ORDER BY
    // priority DESC LIMIT 1")
    Mono<PriceEntity> findTopByProductIdAndBrandIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(
            Integer productId,
            Integer brandId,
            LocalDateTime startDate,
            LocalDateTime endDate);
}
