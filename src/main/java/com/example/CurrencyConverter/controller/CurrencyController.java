package com.example.CurrencyConverter.controller;

import com.example.CurrencyConverter.Response.CurrencyResponse;
import com.example.CurrencyConverter.service.CurrencyService;
import com.example.CurrencyConverter.service.serviceImpl.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/currencies")
public class CurrencyController {

    private final CurrencyService currencyService;
    private final ExchangeRateService exchangeRateService;

    public CurrencyController(CurrencyService currencyService, ExchangeRateService exchangeRateService) {
        this.currencyService = currencyService;
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping("/list")
    public CurrencyResponse getCurrencies() {
        return currencyService.fetchCurrencies();
    }


    // Endpoint: /api/rates?base=EGP
    @GetMapping(value = "/rates")
    public Map<String, BigDecimal> getRatesBasedOnBase(@RequestParam(defaultValue = "USD") String base) {
        return exchangeRateService.getRatesBasedOnCurrency(base);
    }
}
