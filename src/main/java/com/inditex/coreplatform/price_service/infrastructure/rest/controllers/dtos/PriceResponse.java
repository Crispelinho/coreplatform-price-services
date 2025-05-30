package com.inditex.coreplatform.price_service.infrastructure.rest.controllers.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriceResponse {
    private Integer productId;
    private Integer brandId;
    private Integer rateId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Double price;
}
