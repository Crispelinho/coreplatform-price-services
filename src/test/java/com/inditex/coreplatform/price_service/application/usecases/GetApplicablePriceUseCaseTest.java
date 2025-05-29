package com.inditex.coreplatform.price_service.application.usecases;

import com.inditex.coreplatform.price_service.domain.models.Price;
import com.inditex.coreplatform.price_service.domain.ports.IPriceService;
import com.inditex.coreplatform.price_service.application.usecases.queries.GetApplicablePriceQuery;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.time.LocalDateTime;
import static org.mockito.Mockito.*;

class GetApplicablePriceUseCaseTest {

    private IPriceService priceService;
    private GetApplicablePriceUseCase useCase;

    @BeforeEach
    void setUp() {
        priceService = Mockito.mock(IPriceService.class);
        useCase = new GetApplicablePriceUseCase(priceService);
    }

    @Test
    void execute_shouldReturnPrice_whenServiceReturnsPrice() {
        Integer productId = 1;
        Integer brandId = 2;
        LocalDateTime applicationDate = LocalDateTime.now();
        Price price = new Price(); // Assuming default constructor

        GetApplicablePriceQuery query = mock(GetApplicablePriceQuery.class);
        when(query.productId()).thenReturn(productId);
        when(query.brandId()).thenReturn(brandId);
        when(query.applicationDate()).thenReturn(applicationDate);

        when(priceService.getPriceByProductAndBrandIdAndApplicationDate(productId, brandId, applicationDate))
                .thenReturn(Mono.just(price));

        Mono<Price> result = useCase.execute(query);

        StepVerifier.create(result)
                .expectNext(price)
                .verifyComplete();

        verify(priceService, times(1))
                .getPriceByProductAndBrandIdAndApplicationDate(productId, brandId, applicationDate);
    }

    @Test
    void execute_shouldReturnEmpty_whenServiceReturnsEmpty() {
        Integer productId = 1;
        Integer brandId = 2;
        LocalDateTime applicationDate = LocalDateTime.now();

        GetApplicablePriceQuery query = mock(GetApplicablePriceQuery.class);
        when(query.productId()).thenReturn(productId);
        when(query.brandId()).thenReturn(brandId);
        when(query.applicationDate()).thenReturn(applicationDate);

        when(priceService.getPriceByProductAndBrandIdAndApplicationDate(productId, brandId, applicationDate))
                .thenReturn(Mono.empty());

        Mono<Price> result = useCase.execute(query);

        StepVerifier.create(result)
                .verifyComplete();

        verify(priceService, times(1))
                .getPriceByProductAndBrandIdAndApplicationDate(productId, brandId, applicationDate);
    }
}