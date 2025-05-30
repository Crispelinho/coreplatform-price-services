package com.inditex.coreplatform.price_service.infrastructure.mappers;

import com.inditex.coreplatform.price_service.domain.models.Price;
import com.inditex.coreplatform.price_service.infrastructure.persistence.entities.PriceEntity;
import com.inditex.coreplatform.price_service.infrastructure.rest.controllers.dtos.PriceResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;

class PriceMapperImplTest {

    private PriceMapperImpl priceMapper;

    @BeforeEach
    void setUp() {
        priceMapper = new PriceMapperImpl();
    }

    @Test
    void testToDomain_NullEntity() {
        assertNull(priceMapper.toDomain(null));
    }

    @Test
    void testToDomain_ValidEntity() {
        PriceEntity entity = new PriceEntity();
        entity.setBrandId(1);
        entity.setStartDate(LocalDateTime.of(2023, 1, 1, 0, 0));
        entity.setEndDate(LocalDateTime.of(2023, 12, 31, 23, 59));
        entity.setPriceList(2);
        entity.setProductId(3);
        entity.setPriority(4);
        entity.setPrice(99.99);
        entity.setCurr("EUR");

        Price price = priceMapper.toDomain(entity);

        assertNotNull(price);
        assertEquals(entity.getBrandId(), price.getBrandId());
        assertEquals(entity.getStartDate(), price.getStartDate());
        assertEquals(entity.getEndDate(), price.getEndDate());
        assertEquals(entity.getPriceList(), price.getRateId());
        assertEquals(entity.getProductId(), price.getProductId());
        assertEquals(entity.getPriority(), price.getPriority());
        assertEquals(entity.getPrice(), price.getValue());
        assertEquals(entity.getCurr(), price.getCurrency());
    }

    @Test
    void testToEntity_NullDomain() {
        assertNull(priceMapper.toEntity(null));
    }

    @Test
    void testToEntity_ValidDomain() {
        Price price = Price.builder()
                .brandId(1)
                .startDate(LocalDateTime.of(2023, 1, 1, 0, 0))
                .endDate(LocalDateTime.of(2023, 12, 31, 23, 59))
                .rateId(2)
                .productId(3)
                .priority(4)
                .value(99.99)
                .currency("EUR")
                .build();

        PriceEntity entity = priceMapper.toEntity(price);

        assertNotNull(entity);
        assertEquals(price.getBrandId(), entity.getBrandId());
        assertEquals(price.getStartDate(), entity.getStartDate());
        assertEquals(price.getEndDate(), entity.getEndDate());
        assertEquals(price.getRateId(), entity.getPriceList());
        assertEquals(price.getProductId(), entity.getProductId());
        assertEquals(price.getPriority(), entity.getPriority());
        assertEquals(price.getValue(), entity.getPrice());
        assertEquals(price.getCurrency(), entity.getCurr());
    }

    @Test
    void testToResponse_NullDomain() {
        assertNull(priceMapper.toResponse(null));
    }

    @Test
    void testToResponse_ValidDomain() {
        Price price = Price.builder()
                .brandId(1)
                .startDate(LocalDateTime.of(2023, 1, 1, 0, 0))
                .endDate(LocalDateTime.of(2023, 12, 31, 23, 59))
                .rateId(2)
                .productId(3)
                .value(99.99)
                .build();

        PriceResponse response = priceMapper.toResponse(price);

        assertNotNull(response);
        assertEquals(price.getBrandId(), response.getBrandId());
        assertEquals(price.getStartDate(), response.getStartDate());
        assertEquals(price.getEndDate(), response.getEndDate());
        assertEquals(price.getRateId(), response.getRateId());
        assertEquals(price.getProductId(), response.getProductId());
        assertEquals(price.getValue(), response.getPrice());
    }
}