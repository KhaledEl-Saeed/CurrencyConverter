package com.example.CurrencyConverter.service.serviceImpl;

import com.example.CurrencyConverter.WebClient.CurrencyApiClient;
import com.example.CurrencyConverter.entity.CurrencyConvertor;
import com.example.CurrencyConverter.repository.CurrencyConvertorRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
@Service
public class ExchangeRateService {

    private final CurrencyConvertorRepository repository;
    private final RedisTemplate<String, String> redisTemplate;
    private final CurrencyApiClient currencyApiClient;

    private static final String REDIS_KEY_PREFIX = "latest_rates_";

    public ExchangeRateService(CurrencyConvertorRepository repository,
                               RedisTemplate<String, String> redisTemplate,
                               CurrencyApiClient currencyApiClient) {
        this.repository = repository;
        this.redisTemplate = redisTemplate;
        this.currencyApiClient = currencyApiClient;
    }

    @PostConstruct
    public void initializeRatesOnStartup() {
        updateAllCurrencyRates();
    }


    @Scheduled(fixedRate = 3600000)
    public void updateAllCurrencyRates() {
        String base = "USD";

        try {
            String json = currencyApiClient.getLatestRates(base).block();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);

            long timestamp = root.get("timestamp").asLong();
            String baseCurrency = root.get("base").asText();
            JsonNode ratesNode = root.get("rates");

            String baseRatesJson = ratesNode.toString();
            saveRates(timestamp, baseCurrency, baseRatesJson);
            redisTemplate.opsForValue().set("latest_rates_" + baseCurrency, baseRatesJson);

            Iterator<Map.Entry<String, JsonNode>> fields = ratesNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                String newBaseCurrency = entry.getKey();
                BigDecimal newBaseRate = entry.getValue().decimalValue();

                if (newBaseRate.compareTo(BigDecimal.ZERO) == 0 || "USD".equalsIgnoreCase(newBaseCurrency)) {
                    continue;
                }

                BigDecimal inverse = BigDecimal.ONE.divide(newBaseRate, 10, RoundingMode.HALF_UP);
                Map<String, BigDecimal> convertedRates = new HashMap<>();

                for (Iterator<Map.Entry<String, JsonNode>> it = ratesNode.fields(); it.hasNext(); ) {
                    Map.Entry<String, JsonNode> rateEntry = it.next();
                    String currency = rateEntry.getKey();
                    BigDecimal rateInUSD = rateEntry.getValue().decimalValue();
                    BigDecimal rateInNewBase = rateInUSD.multiply(inverse).setScale(6, RoundingMode.HALF_UP);
                    convertedRates.put(currency, rateInNewBase);
                }

                convertedRates.put(newBaseCurrency, BigDecimal.ONE);

                String jsonRates = mapper.writeValueAsString(convertedRates);
                saveRates(timestamp, newBaseCurrency, jsonRates);

                redisTemplate.opsForValue().set("latest_rates_" + newBaseCurrency, jsonRates);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to update all exchange rates", e);
        }
    }

    public String getCachedRates(String base) {
        return redisTemplate.opsForValue().get(REDIS_KEY_PREFIX + base.toUpperCase());
    }

    public Map<String, BigDecimal> getRatesBasedOnCurrency(String baseCurrency) {
        try {
            String ratesJson = getCachedRates(baseCurrency);
            if (ratesJson == null) {
                throw new RuntimeException("Rates not found in cache for base: " + baseCurrency);
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode ratesNode = mapper.readTree(ratesJson);

            Map<String, BigDecimal> result = new HashMap<>();
            Iterator<Map.Entry<String, JsonNode>> fields = ratesNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                result.put(entry.getKey(), entry.getValue().decimalValue());
            }

            return result;

        } catch (Exception e) {
            throw new RuntimeException("Failed to get rates for base: " + baseCurrency, e);
        }
    }

    private void saveRates(long timestamp, String currency, String ratesJson) {
        CurrencyConvertor entity = new CurrencyConvertor();
        entity.setTimestamp(timestamp);
        entity.setCurrency(currency);
        entity.setRates(ratesJson);
        repository.save(entity);
    }
}



