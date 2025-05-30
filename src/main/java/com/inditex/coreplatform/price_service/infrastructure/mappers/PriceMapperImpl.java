package com.inditex.coreplatform.price_service.infrastructure.mappers;

import org.springframework.stereotype.Component;

import com.inditex.coreplatform.price_service.domain.models.Price;
import com.inditex.coreplatform.price_service.infrastructure.persistence.entities.PriceEntity;
import com.inditex.coreplatform.price_service.infrastructure.rest.controllers.dtos.PriceResponse;

@Component
public class PriceMapperImpl implements PriceMapper {

    @Override
    public Price toDomain(PriceEntity entity) {
        if (entity == null) {
            return null;
        }

        return Price.builder()
                .brandId(entity.getBrandId())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .rateId(entity.getPriceList())
                .productId(entity.getProductId())
                .priority(entity.getPriority())
                .value(entity.getPrice())
                .currency(entity.getCurr())
                .build();
    }

    @Override
    public PriceEntity toEntity(Price domain) {
        if (domain == null) {
            return null;
        }

        PriceEntity entity = new PriceEntity();
        entity.setBrandId(domain.getBrandId());
        entity.setStartDate(domain.getStartDate());
        entity.setEndDate(domain.getEndDate());
        entity.setPriceList(domain.getRateId());
        entity.setProductId(domain.getProductId());
        entity.setPriority(domain.getPriority());
        entity.setPrice(domain.getValue());
        entity.setCurr(domain.getCurrency());
        return entity;
    }

    @Override
    public PriceResponse toResponse(Price domain) {
        if (domain == null) {
            return null;
        }

        return PriceResponse.builder()
                .brandId(domain.getBrandId())
                .startDate(domain.getStartDate())
                .endDate(domain.getEndDate())
                .rateId(domain.getRateId())
                .productId(domain.getProductId())
                .price(domain.getValue())
                .build();
    }
}
