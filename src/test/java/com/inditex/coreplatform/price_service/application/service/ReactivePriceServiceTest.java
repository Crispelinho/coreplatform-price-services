package com.inditex.coreplatform.price_service.application.service;

import com.inditex.coreplatform.price_service.application.exceptions.MissingPriceApplicationRequestParamException;
import com.inditex.coreplatform.price_service.domain.models.Price;
import com.inditex.coreplatform.price_service.domain.ports.IPriceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

class ReactivePriceServiceTest {

        private IPriceRepository priceRepository;
        private ReactivePriceService reactivePriceService;

        @BeforeEach
        void setUp() {
                priceRepository = Mockito.mock(IPriceRepository.class);
                reactivePriceService = new ReactivePriceService(priceRepository);
        }

        @Test
        void testGetPriceByProductAndBrandIdAndApplicationDate_ReturnsPrice() {
                Integer productId = 1;
                Integer brandId = 2;
                LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);

                Price price = new Price();
                price.setProductId(productId);
                price.setBrandId(brandId);
                price.setStartDate(applicationDate.minusDays(1)); // o una fecha anterior
                price.setEndDate(applicationDate.plusDays(1)); // o una fecha posterior
                price.setRateId(1);
                price.setPriority(1);
                price.setValue(35.5);
                price.setCurrency("EUR");

                Mockito.when(priceRepository
                                .findTopByProductIdAndBrandIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(
                                                productId, brandId, applicationDate, applicationDate))
                                .thenReturn(Mono.just(price));

                Mono<Price> result = reactivePriceService
                                .getPriceByProductAndBrandIdAndApplicationDate(productId, brandId, applicationDate);

                StepVerifier.create(result)
                                .expectNextMatches(p -> p.getProductId().equals(productId) &&
                                                p.getBrandId().equals(brandId) &&
                                                p.getValue().equals(35.5))
                                .verifyComplete();
        }

        @Test
        void testGetPriceByProductAndBrandIdAndApplicationDate_ReturnsEmpty() {
                Integer productId = 1;
                Integer brandId = 2;
                LocalDateTime applicationDate = LocalDateTime.now();

                Mockito.when(priceRepository
                                .findTopByProductIdAndBrandIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(
                                                productId, brandId, applicationDate, applicationDate))
                                .thenReturn(Mono.empty());

                Mono<Price> result = reactivePriceService.getPriceByProductAndBrandIdAndApplicationDate(productId,
                                brandId, applicationDate);

                StepVerifier.create(result)
                                .verifyComplete();
        }

        @Test
        void testGetAllPrices_ReturnsPrices() {
                Price price1 = new Price();
                Price price2 = new Price();

                Mockito.when(priceRepository.findAll()).thenReturn(Flux.just(price1, price2));

                Flux<Price> result = reactivePriceService.getAllPrices();

                StepVerifier.create(result)
                                .expectNext(price1)
                                .expectNext(price2)
                                .verifyComplete();
        }

        @Test
        void testGetAllPrices_ReturnsEmpty() {
                Mockito.when(priceRepository.findAll()).thenReturn(Flux.empty());

                Flux<Price> result = reactivePriceService.getAllPrices();

                StepVerifier.create(result)
                                .verifyComplete();
        }

        @Test
        void testGetPriceByProductAndBrandIdAndApplicationDate_MissingProductId() {
                Integer productId = null;
                Integer brandId = 2;
                LocalDateTime applicationDate = LocalDateTime.now();

                Mono<Price> result = reactivePriceService
                        .getPriceByProductAndBrandIdAndApplicationDate(productId, brandId, applicationDate);

                StepVerifier.create(result)
                        .expectErrorSatisfies(throwable -> {
                                assert throwable instanceof MissingPriceApplicationRequestParamException;
                                assert throwable.getMessage().contains("productId");
                        })
                        .verify();
        }

        @Test
        void testGetPriceByProductAndBrandIdAndApplicationDate_MissingBrandId() {
                Integer productId = 1;
                Integer brandId = null;
                LocalDateTime applicationDate = LocalDateTime.now();

                Mono<Price> result = reactivePriceService
                        .getPriceByProductAndBrandIdAndApplicationDate(productId, brandId, applicationDate);

                StepVerifier.create(result)
                        .expectErrorSatisfies(throwable -> {
                                assert throwable instanceof MissingPriceApplicationRequestParamException;
                                assert throwable.getMessage().contains("brandId");
                        })
                        .verify();
        }

        @Test
        void testGetPriceByProductAndBrandIdAndApplicationDate_MissingApplicationDate() {
                Integer productId = 1;
                Integer brandId = 2;
                LocalDateTime applicationDate = null;

                Mono<Price> result = reactivePriceService
                        .getPriceByProductAndBrandIdAndApplicationDate(productId, brandId, applicationDate);

                StepVerifier.create(result)
                        .expectErrorSatisfies(throwable -> {
                                assert throwable instanceof MissingPriceApplicationRequestParamException;
                                assert throwable.getMessage().contains("applicationDate");
                        })
                        .verify();
        }

        @Test
        void testGetPriceByProductAndBrandIdAndApplicationDate_MultipleMissingParams() {
                Integer productId = null;
                Integer brandId = null;
                LocalDateTime applicationDate = null;

                Mono<Price> result = reactivePriceService
                        .getPriceByProductAndBrandIdAndApplicationDate(productId, brandId, applicationDate);

                StepVerifier.create(result)
                        .expectErrorSatisfies(throwable -> {
                                assert throwable instanceof MissingPriceApplicationRequestParamException;
                                assert throwable.getMessage().contains("productId");
                                assert throwable.getMessage().contains("brandId");
                                assert throwable.getMessage().contains("applicationDate");
                                assert throwable.getMessage().contains("parameters");
                        })
                        .verify();
        }

        @Test
        void testGetPriceByProductAndBrandIdAndApplicationDate_FiltersNonApplicablePrice() {
                Integer productId = 1;
                Integer brandId = 2;
                LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);

                Price price = Mockito.mock(Price.class);
                Mockito.when(price.isApplicableAt(applicationDate)).thenReturn(false);

                Mockito.when(priceRepository
                        .findTopByProductIdAndBrandIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(
                                productId, brandId, applicationDate, applicationDate))
                        .thenReturn(Mono.just(price));

                Mono<Price> result = reactivePriceService
                        .getPriceByProductAndBrandIdAndApplicationDate(productId, brandId, applicationDate);

                StepVerifier.create(result)
                        .verifyComplete();
        }
}