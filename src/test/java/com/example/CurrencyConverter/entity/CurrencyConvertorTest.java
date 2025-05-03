package com.example.CurrencyConverter.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CurrencyConvertorTest {

    @Test
    public void testSetAndGetId() {
        CurrencyConvertor cc = new CurrencyConvertor();
        cc.setId(1L);
        assertThat(cc.getId()).isEqualTo(1L);
    }

    @Test
    public void testSetAndGetTimestamp() {
        CurrencyConvertor cc = new CurrencyConvertor();
        cc.setTimestamp(1714741280L);
        assertThat(cc.getTimestamp()).isEqualTo(1714741280L);
    }

    @Test
    public void testSetAndGetCurrency() {
        CurrencyConvertor cc = new CurrencyConvertor();
        cc.setCurrency("USD");
        assertThat(cc.getCurrency()).isEqualTo("USD");
    }

    @Test
    public void testSetAndGetRates() {
        CurrencyConvertor cc = new CurrencyConvertor();
        String ratesJson = "{\"EUR\": 0.92, \"GBP\": 0.78}";
        cc.setRates(ratesJson);
        assertThat(cc.getRates()).isEqualTo(ratesJson);
    }

    @Test
    public void testAllFields() {
        CurrencyConvertor cc = new CurrencyConvertor();
        cc.setId(10L);
        cc.setTimestamp(1714741280L);
        cc.setCurrency("USD");
        cc.setRates("{\"JPY\": 153.2}");

        assertThat(cc.getId()).isEqualTo(10L);
        assertThat(cc.getTimestamp()).isEqualTo(1714741280L);
        assertThat(cc.getCurrency()).isEqualTo("USD");
        assertThat(cc.getRates()).isEqualTo("{\"JPY\": 153.2}");
    }
}

