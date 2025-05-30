package com.inditex.coreplatform.price_service.infrastructure.rest.controllers;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inditex.coreplatform.price_service.application.usecases.GetApplicablePriceUseCase;
import com.inditex.coreplatform.price_service.application.usecases.GetPricesUseCase;
import com.inditex.coreplatform.price_service.application.usecases.queries.GetApplicablePriceQuery;
import com.inditex.coreplatform.price_service.infrastructure.mappers.PriceMapper;
import com.inditex.coreplatform.price_service.infrastructure.rest.controllers.dtos.PriceResponse;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Validated
@RequestMapping("/api/prices")
public class PriceController {

    private final GetApplicablePriceUseCase getApplicablePriceUseCase;
    private final GetPricesUseCase getPricesUseCase;
    private final PriceMapper priceMapper;

    public PriceController(GetApplicablePriceUseCase getApplicablePriceUseCase,
            GetPricesUseCase getPricesUseCase,
            PriceMapper priceMapper) {
        this.getApplicablePriceUseCase = getApplicablePriceUseCase;
        this.getPricesUseCase = getPricesUseCase;
        this.priceMapper = priceMapper;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Flux<PriceResponse>>> getPrices() {
        Flux<PriceResponse> pricesResponse = getPricesUseCase.execute()
                .map(priceMapper::toResponse);

        return pricesResponse.hasElements()
                .flatMap(has -> Mono.just(
                        Boolean.TRUE.equals(has)
                                ? ResponseEntity.ok(pricesResponse)
                                : ResponseEntity.notFound().build()));
    }

    @GetMapping(value = "/applicable", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PriceResponse>> getApplicablePrice(
            @RequestParam("productId") @NotNull @Positive(message = "must be a positive integer") Integer productId,
            @RequestParam("brandId") @NotNull @Positive(message = "must be a positive integer") Integer brandId,
            @RequestParam("applicationDate") @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime applicationDate) {

        GetApplicablePriceQuery query = new GetApplicablePriceQuery(productId, brandId, applicationDate);

        return getApplicablePriceUseCase.execute(query)
                .map(priceMapper::toResponse)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
}
