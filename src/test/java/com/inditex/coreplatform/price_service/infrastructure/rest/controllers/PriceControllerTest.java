package com.inditex.coreplatform.price_service.infrastructure.rest.controllers;

import com.inditex.coreplatform.price_service.application.usecases.GetApplicablePriceUseCase;
import com.inditex.coreplatform.price_service.application.usecases.GetPricesUseCase;
import com.inditex.coreplatform.price_service.application.usecases.queries.GetApplicablePriceQuery;
import com.inditex.coreplatform.price_service.domain.models.Price;
import com.inditex.coreplatform.price_service.infrastructure.mappers.PriceMapper;
import com.inditex.coreplatform.price_service.infrastructure.rest.controllers.dtos.PriceResponse;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PriceControllerTest {

    private GetApplicablePriceUseCase getApplicablePriceUseCase;
    private GetPricesUseCase getPricesUseCase;
    private PriceMapper priceMapper;
    private PriceController priceController;

    @BeforeEach
    void setUp() {
        getApplicablePriceUseCase = mock(GetApplicablePriceUseCase.class);
        getPricesUseCase = mock(GetPricesUseCase.class);
        priceMapper = mock(PriceMapper.class);
        priceController = new PriceController(getApplicablePriceUseCase, getPricesUseCase, priceMapper);
    }

    @Test
    void getPrices_shouldReturnOkWithPrices_whenPricesExist() {
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);

        Price price1 = Price.builder()
                .brandId(1)
                .startDate(startDate)
                .endDate(endDate)
                .rateId(1)
                .productId(100)
                .priority(1)
                .value(50.0)
                .currency("EUR")
                .build();

        Price price2 = Price.builder()
                .brandId(2)
                .startDate(startDate)
                .endDate(endDate)
                .rateId(2)
                .productId(200)
                .priority(2)
                .value(80.0)
                .currency("EUR")
                .build();

        PriceResponse priceResponse1 = PriceResponse.builder()
                .productId(100)
                .brandId(1)
                .rateId(1)
                .startDate(startDate)
                .endDate(endDate)
                .price(50.0)
                .build();

        PriceResponse priceResponse2 = PriceResponse.builder()
                .productId(200)
                .brandId(2)
                .rateId(2)
                .startDate(startDate)
                .endDate(endDate)
                .price(80.0)
                .build();

        when(getPricesUseCase.execute()).thenReturn(Flux.just(price1, price2));
        when(priceMapper.toResponse(price1)).thenReturn(priceResponse1);
        when(priceMapper.toResponse(price2)).thenReturn(priceResponse2);

        Mono<ResponseEntity<Flux<PriceResponse>>> resultMono = priceController.getPrices();

        StepVerifier.create(resultMono)
                .assertNext(response -> {
                    assertEquals(200, response.getStatusCode().value());
                    assertNotNull(response.getBody());
                        
                    StepVerifier.create(response.getBody().collectList())
                            .assertNext(list -> {
                                assertEquals(2, list.size());
                                assertTrue(list.contains(priceResponse1));
                                assertTrue(list.contains(priceResponse2));
                            })
                            .verifyComplete();
                })
                .verifyComplete();
    }

    @Test
    void getPrices_shouldReturnNotFound_whenNoPricesExist() {
        when(getPricesUseCase.execute()).thenReturn(Flux.empty());

        Mono<ResponseEntity<Flux<PriceResponse>>> resultMono = priceController.getPrices();

        StepVerifier.create(resultMono)
                .assertNext(response -> {
                    assertEquals(404, response.getStatusCode().value());
                    assertNull(response.getBody());
                })
                .verifyComplete();
    }

    @Test
    void getApplicablePrice_shouldReturnOkWithPrice_whenPriceExists() {
        Integer productId = 1;
        Integer brandId = 2;
        LocalDateTime applicationDate = LocalDateTime.now();

        Price price = Price.builder()
                .brandId(brandId)
                .startDate(applicationDate.minusDays(1))
                .endDate(applicationDate.plusDays(1))
                .rateId(1)
                .productId(productId)
                .priority(1)
                .value(100.0)
                .currency("EUR")
                .build();

        PriceResponse priceResponse = PriceResponse.builder()
                .productId(productId)
                .brandId(brandId)
                .rateId(1)
                .startDate(applicationDate.minusDays(1))
                .endDate(applicationDate.plusDays(1))
                .price(100.0)
                .build();

        when(getApplicablePriceUseCase.execute(any(GetApplicablePriceQuery.class)))
                .thenReturn(Mono.just(price));
        when(priceMapper.toResponse(price)).thenReturn(priceResponse);

        Mono<ResponseEntity<PriceResponse>> resultMono = priceController.getApplicablePrice(productId, brandId, applicationDate);

        StepVerifier.create(resultMono)
                .assertNext(response -> {
                    assertEquals(200, response.getStatusCode().value());
                    assertEquals(priceResponse, response.getBody());
                })
                .verifyComplete();

        ArgumentCaptor<GetApplicablePriceQuery> captor = ArgumentCaptor.forClass(GetApplicablePriceQuery.class);
        verify(getApplicablePriceUseCase).execute(captor.capture());
        GetApplicablePriceQuery query = captor.getValue();
        assertEquals(productId, query.productId());
        assertEquals(brandId, query.brandId());
        assertEquals(applicationDate, query.applicationDate());
    }

    @Test
    void getApplicablePrice_shouldReturnNotFound_whenNoPriceExists() {
        Integer productId = 1;
        Integer brandId = 2;
        LocalDateTime applicationDate = LocalDateTime.now();

        when(getApplicablePriceUseCase.execute(any(GetApplicablePriceQuery.class)))
                .thenReturn(Mono.empty());

        Mono<ResponseEntity<PriceResponse>> resultMono = priceController.getApplicablePrice(productId, brandId, applicationDate);

        StepVerifier.create(resultMono)
                .assertNext(response -> {
                    assertEquals(404, response.getStatusCode().value());
                    assertNull(response.getBody());
                })
                .verifyComplete();
    }
}
