package com.example.CurrencyConverter.controller;


import com.example.CurrencyConverter.Response.CurrencyResponse;
import com.example.CurrencyConverter.service.CurrencyService;
import com.example.CurrencyConverter.service.serviceImpl.ExchangeRateService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.*;

@WebMvcTest(CurrencyController.class)
class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CurrencyService currencyService;

    @MockitoBean
    private ExchangeRateService exchangeRateService;

    @Test
    @DisplayName("Given currencyService returns response, When calling /list, Then return 200 with data")
    void getCurrencies_ShouldReturnCurrencyResponse() throws Exception {
        // Given
        CurrencyResponse mockResponse = new CurrencyResponse();
        mockResponse.setStatus("success");

        Mockito.when(currencyService.fetchCurrencies()).thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(get("/api/v1/currencies/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("success")));
    }

    @Test
    @DisplayName("Given exchangeRateService returns rates, When calling /rates with base=EGP, Then return 200 and JSON")
    void getRatesBasedOnBase_ShouldReturnRatesMap() throws Exception {
        // Given
        Map<String, BigDecimal> rates = Map.of("USD", BigDecimal.valueOf(0.032), "EUR", BigDecimal.valueOf(0.03));
        Mockito.when(exchangeRateService.getRatesBasedOnCurrency("EGP")).thenReturn(rates);

        // When & Then
        mockMvc.perform(get("/api/v1/currencies/rates").param("base", "EGP"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.USD", is(0.032)))
                .andExpect(jsonPath("$.EUR", is(0.03)));
    }

    @Test
    @DisplayName("Given no base param, When calling /rates, Then default to USD and return 200")
    void getRates_ShouldUseDefaultBase() throws Exception {
        // Given
        Map<String, BigDecimal> rates = Map.of("EGP", BigDecimal.valueOf(30.95));
        Mockito.when(exchangeRateService.getRatesBasedOnCurrency("USD")).thenReturn(rates);

        // When & Then
        mockMvc.perform(get("/api/v1/currencies/rates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.EGP", is(30.95)));
    }
}

