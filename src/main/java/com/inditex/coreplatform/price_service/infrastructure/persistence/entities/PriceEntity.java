package com.inditex.coreplatform.price_service.infrastructure.persistence.entities;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table("prices")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PriceEntity {
    @Id
    @Column("id")
    private Long id;

    @Column("brand_id")
    private Integer brandId;

    @Column("start_date")
    private LocalDateTime startDate;

    @Column("end_date")
    private LocalDateTime endDate;

    @Column("price_list")
    private Integer priceList;

    @Column("product_id")
    private Integer productId;

    @Column("priority")
    private Integer priority;

    @Column("price")
    private Double price;

    @Column("curr")
    private String curr;
}
