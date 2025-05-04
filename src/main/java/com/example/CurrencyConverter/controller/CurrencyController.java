package com.example.CurrencyConverter.controller;

import com.example.CurrencyConverter.Response.CurrencyResponse;
import com.example.CurrencyConverter.service.CurrencyService;
import com.example.CurrencyConverter.service.serviceImpl.ExchangeRateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/currencies")
@Tag(name = "Currency API", description = "Endpoints for retrieving currency data and exchange rates")
public class CurrencyController {

    private final CurrencyService currencyService;
    private final ExchangeRateService exchangeRateService;

    public CurrencyController(CurrencyService currencyService, ExchangeRateService exchangeRateService) {
        this.currencyService = currencyService;
        this.exchangeRateService = exchangeRateService;
    }

    @Operation(
            summary = "Get list of supported currencies",
            description = "Fetches the list of all available currencies with their names and codes"
    )
    @ApiResponse(
            responseCode = "200",
            description = "List of currencies returned successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CurrencyResponse.class))
    )
    @GetMapping("/list")
    public CurrencyResponse getCurrencies() {
        return currencyService.fetchCurrencies();
    }


    @Operation(
            summary = "Get exchange rates for a base currency",
            description = "Returns exchange rates based on the provided base currency (default is USD)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exchange rates retrieved successfully",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Rates not found in cache for base",
                    content = @Content)
    })
    @GetMapping(value = "/rates")
    public Map<String, BigDecimal> getRatesBasedOnBase(@RequestParam(defaultValue = "USD") String base) {
        return exchangeRateService.getRatesBasedOnCurrency(base);
    }
}
