package com.inditex.coreplatform.price_service.application.usecases;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import com.inditex.coreplatform.price_service.domain.models.Price;
import com.inditex.coreplatform.price_service.domain.ports.IPriceService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import static org.mockito.Mockito.*;

class GetPricesUseCaseTest {

    private IPriceService priceService;
    private GetPricesUseCase getPricesUseCase;

    @BeforeEach
    void setUp() {
        priceService = mock(IPriceService.class);
        getPricesUseCase = new GetPricesUseCase(priceService);
    }

    @Test
    void execute_shouldReturnAllPrices() {
        Price price1 = new Price();
        Price price2 = new Price();
        when(priceService.getAllPrices()).thenReturn(Flux.just(price1, price2));

        StepVerifier.create(getPricesUseCase.execute())
                .expectNext(price1)
                .expectNext(price2)
                .verifyComplete();

        verify(priceService, times(1)).getAllPrices();
    }

    @Test
    void execute_shouldReturnEmptyWhenNoPrices() {
        when(priceService.getAllPrices()).thenReturn(Flux.empty());

        StepVerifier.create(getPricesUseCase.execute())
                .verifyComplete();

        verify(priceService, times(1)).getAllPrices();
    }
}