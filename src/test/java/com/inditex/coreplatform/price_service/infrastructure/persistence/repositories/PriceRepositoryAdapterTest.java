package com.inditex.coreplatform.price_service.infrastructure.persistence.repositories;

import com.inditex.coreplatform.price_service.domain.models.Price;
import com.inditex.coreplatform.price_service.infrastructure.mappers.PriceMapper;
import com.inditex.coreplatform.price_service.infrastructure.persistence.PriceRepositoryAdapter;
import com.inditex.coreplatform.price_service.infrastructure.persistence.entities.PriceEntity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

class PriceRepositoryAdapterTest {

    private IReactivePriceRepository reactivePriceRepository;
    private PriceMapper priceMapper;
    private PriceRepositoryAdapter priceRepositoryApapter;

    @BeforeEach
    void setUp() {
        reactivePriceRepository = mock(IReactivePriceRepository.class);
        priceMapper = mock(PriceMapper.class);
        priceRepositoryApapter = new PriceRepositoryAdapter(reactivePriceRepository, priceMapper);
    }

    @Test
    void testFindTopByProductIdAndBrandIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc() {
        Integer productId = 1;
        Integer brandId = 2;
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);

        PriceEntity entity = new PriceEntity();
        Price domainPrice = new Price();

        when(reactivePriceRepository
                .findTopByProductIdAndBrandIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(
                        productId, brandId, startDate, endDate))
                .thenReturn(Mono.just(entity));
        when(priceMapper.toDomain(entity)).thenReturn(domainPrice);

        Mono<Price> result = priceRepositoryApapter
                .findTopByProductIdAndBrandIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(
                        productId, brandId, startDate, endDate);

        StepVerifier.create(result)
                .expectNext(domainPrice)
                .verifyComplete();

        verify(reactivePriceRepository).findTopByProductIdAndBrandIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(
                productId, brandId, startDate, endDate);
        verify(priceMapper).toDomain(entity);
    }

    @Test
    void testFindAll() {
        PriceEntity entity1 = new PriceEntity();
        entity1.setProductId(1); // Diferenciar entidades
        PriceEntity entity2 = new PriceEntity();
        entity2.setProductId(2);
        Price domainPrice1 = new Price();
        Price domainPrice2 = new Price();

        when(reactivePriceRepository.findAll()).thenReturn(Flux.just(entity1, entity2));
        when(priceMapper.toDomain(entity1)).thenReturn(domainPrice1);
        when(priceMapper.toDomain(entity2)).thenReturn(domainPrice2);

        Flux<Price> result = priceRepositoryApapter.findAll();

        StepVerifier.create(result)
                .expectNext(domainPrice1)
                .expectNext(domainPrice2)
                .verifyComplete();

        verify(reactivePriceRepository).findAll();
        verify(priceMapper, times(1)).toDomain(entity1);
        verify(priceMapper, times(1)).toDomain(entity2);
    }
}