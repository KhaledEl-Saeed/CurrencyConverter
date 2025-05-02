package com.example.CurrencyConverter.WebClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class CurrencyApiClient {
    private static final Logger logger = LoggerFactory.getLogger(CurrencyApiClient.class);


    private final WebClient.Builder webClientBuilder;

    @Value("${currency.api.url}")
    private String apiUrl;

    @Value("${currency.api.currency-path}")
    private String currencyPath;

    @Value("${currency.api.exchange-path}")
    private String exchangePath;

    @Value("${currency.api.app-id}")
    private String appId;

    public CurrencyApiClient(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Map<String, String> getCurrencies() {

        return webClientBuilder
                .baseUrl(apiUrl)  // 👈 This tells WebClient where to connect
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/currencies.json")
                        .queryParam("prettyprint", "false")
                        .queryParam("show_alternative", "false")
                        .queryParam("show_inactive", "false")
                        .queryParam("app_id", appId)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .block();

    }

    public Mono<String> getLatestRates() {
        return webClientBuilder
                .baseUrl(apiUrl)
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(exchangePath)
                        .queryParam("app_id", appId)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }


}
