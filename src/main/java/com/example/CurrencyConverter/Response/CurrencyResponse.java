package com.example.CurrencyConverter.Response;

import lombok.Data;

import java.util.Map;



@Data
public class CurrencyResponse {
    private String status;
    private Map<String, String> currencies;


}
