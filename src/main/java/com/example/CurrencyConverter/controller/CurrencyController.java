package com.example.CurrencyConverter.controller;

import com.example.CurrencyConverter.Response.CurrencyResponse;
import com.example.CurrencyConverter.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/currencies")
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/list")
    public CurrencyResponse getCurrencies() {
        return currencyService.fetchCurrencies();
    }
}
