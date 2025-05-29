package com.inditex.coreplatform.price_service.application.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.inditex.coreplatform.price_service.domain.models.Price;
import com.inditex.coreplatform.price_service.domain.ports.IPriceRepository;
import com.inditex.coreplatform.price_service.domain.ports.IPriceService;
import com.inditex.coreplatform.price_service.application.exceptions.MissingPriceApplicationRequestParamException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Reactive service for handling price-related operations.
 * This service interacts with the price repository to fetch prices based on
 * product ID, brand ID, and application date.
 */

@Service
public class ReactivePriceService implements IPriceService {
    private final IPriceRepository priceRepository;

    public ReactivePriceService(IPriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    public Mono<Price> getPriceByProductAndBrandIdAndApplicationDate(Integer productId, Integer brandId,
            LocalDateTime applicationDate) {

        List<String> missingParams = new ArrayList<>();

        if (productId == null) {
            missingParams.add("productId");
        }
        if (brandId == null) {
            missingParams.add("brandId");
        }
        if (applicationDate == null) {
            missingParams.add("applicationDate");
        }

        if (!missingParams.isEmpty()) {
            String joinedParams = String.join(", ", missingParams);
            String errorMessage = "Missing required request parameter" + (missingParams.size() > 1 ? "s" : "") + ": "
                    + joinedParams;
            return Mono.error(new MissingPriceApplicationRequestParamException(errorMessage));
        }

        return priceRepository
                .findTopByProductIdAndBrandIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(
                        productId, brandId, applicationDate, applicationDate)
                .filter(price -> price.isApplicableAt(applicationDate));
    }

    public Flux<Price> getAllPrices() {
        return priceRepository.findAll();
    }
}
