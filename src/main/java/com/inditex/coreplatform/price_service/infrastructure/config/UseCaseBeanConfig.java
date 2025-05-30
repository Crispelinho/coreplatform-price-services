package com.inditex.coreplatform.price_service.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.inditex.coreplatform.price_service.application.usecases.GetApplicablePriceUseCase;
import com.inditex.coreplatform.price_service.application.usecases.GetPricesUseCase;
import com.inditex.coreplatform.price_service.domain.ports.IPriceService;

@Configuration
public class UseCaseBeanConfig {

    @Bean
    public GetApplicablePriceUseCase getApplicablePriceUseCase(IPriceService priceService) {
        return new GetApplicablePriceUseCase(priceService);
    }

    @Bean
    public GetPricesUseCase getPricesUseCase(IPriceService priceService) {
        return new GetPricesUseCase(priceService);
    }
}
