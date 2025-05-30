package com.inditex.coreplatform.price_service.infrastructure.mappers;

import com.inditex.coreplatform.price_service.domain.models.Price;
import com.inditex.coreplatform.price_service.infrastructure.persistence.entities.PriceEntity;
import com.inditex.coreplatform.price_service.infrastructure.rest.controllers.dtos.PriceResponse;

public interface PriceMapper {

    Price toDomain(PriceEntity entity);
    
    PriceEntity toEntity(Price domain);

    PriceResponse toResponse(Price domain);
}
