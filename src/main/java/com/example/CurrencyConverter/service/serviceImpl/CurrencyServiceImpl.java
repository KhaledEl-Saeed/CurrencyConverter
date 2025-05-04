package com.example.CurrencyConverter.service.serviceImpl;

import com.example.CurrencyConverter.Response.CurrencyResponse;
import com.example.CurrencyConverter.WebClient.CurrencyApiClient;
import com.example.CurrencyConverter.service.CurrencyService;
import org.springframework.stereotype.Service;


@Service
public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyApiClient apiClient;

    public CurrencyServiceImpl(CurrencyApiClient apiClient) {
        this.apiClient = apiClient;
    }
    @Override
    public CurrencyResponse fetchCurrencies() {
        CurrencyResponse response = new CurrencyResponse();

        response.setStatus("success");
        if (apiClient.getCurrencies() == null) {
            response.setStatus("fail");
            return response;
        }
        response.setCurrencies(apiClient.getCurrencies());
        return response;
    }
}
