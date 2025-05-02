package com.example.CurrencyConverter.service.serviceImpl;

import com.example.CurrencyConverter.Response.CurrencyResponse;
import com.example.CurrencyConverter.WebClient.CurrencyApiClient;
import com.example.CurrencyConverter.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyApiClient apiClient;

    public CurrencyServiceImpl(CurrencyApiClient apiClient) {
        this.apiClient = apiClient;
    }
    @Override
    public CurrencyResponse fetchCurrencies() {
        CurrencyResponse response = new CurrencyResponse();
        response.setSuccess(true);
        response.setCurrencies(apiClient.getCurrencies());
        return response;
    }
}
