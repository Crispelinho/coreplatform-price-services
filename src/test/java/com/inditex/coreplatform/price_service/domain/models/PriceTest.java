package com.inditex.coreplatform.price_service.domain.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

class PriceTest {

    @Test
    void testIsApplicableAt_withinRange_returnsTrue() {
        LocalDateTime start = LocalDateTime.of(2023, 10, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 10, 10, 23, 59);
        LocalDateTime applicationDate = LocalDateTime.of(2023, 10, 5, 12, 0);

        Price price = Price.builder()
                .brandId(1)
                .startDate(start)
                .endDate(end)
                .rateId(1)
                .productId(100)
                .priority(0)
                .value(99.99)
                .currency("EUR")
                .build();

        assertTrue(price.isApplicableAt(applicationDate));
    }

    @Test
    void testIsApplicableAt_beforeStart_returnsFalse() {
        LocalDateTime start = LocalDateTime.of(2023, 10, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 10, 10, 23, 59);
        LocalDateTime applicationDate = LocalDateTime.of(2023, 9, 30, 23, 59);

        Price price = Price.builder()
                .startDate(start)
                .endDate(end)
                .build();

        assertFalse(price.isApplicableAt(applicationDate));
    }

    @Test
    void testIsApplicableAt_afterEnd_returnsFalse() {
        LocalDateTime start = LocalDateTime.of(2023, 10, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 10, 10, 23, 59);
        LocalDateTime applicationDate = LocalDateTime.of(2023, 10, 11, 0, 0);

        Price price = Price.builder()
                .startDate(start)
                .endDate(end)
                .build();

        assertFalse(price.isApplicableAt(applicationDate));
    }

    @Test
    void testIsApplicableAt_onStartDate_returnsTrue() {
        LocalDateTime start = LocalDateTime.of(2023, 10, 1, 0, 0);

        Price price = Price.builder()
                .startDate(start)
                .endDate(start.plusDays(1))
                .build();

        assertTrue(price.isApplicableAt(start));
    }

    @Test
    void testIsApplicableAt_onEndDate_returnsTrue() {
        LocalDateTime start = LocalDateTime.of(2023, 10, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 10, 10, 23, 59);

        Price price = Price.builder()
                .startDate(start)
                .endDate(end)
                .build();

        assertTrue(price.isApplicableAt(end));
    }

    @Test
    void testLombokGeneratedMethods() {
        Price price1 = new Price(1, LocalDateTime.now(), LocalDateTime.now().plusDays(1), 2, 3, 1, 10.0, "USD");
        Price price2 = new Price();
        price2.setBrandId(1);
        price2.setStartDate(price1.getStartDate());
        price2.setEndDate(price1.getEndDate());
        price2.setRateId(2);
        price2.setProductId(3);
        price2.setPriority(1);
        price2.setValue(10.0);
        price2.setCurrency("USD");

        assertEquals(price1, price2);
        assertEquals(price1.hashCode(), price2.hashCode());
        assertEquals(price1.toString(), price2.toString());
    }
}