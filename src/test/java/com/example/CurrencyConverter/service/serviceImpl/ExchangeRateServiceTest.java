package com.example.CurrencyConverter.service.serviceImpl;

import com.example.CurrencyConverter.WebClient.CurrencyApiClient;
import com.example.CurrencyConverter.entity.CurrencyConvertor;
import com.example.CurrencyConverter.repository.CurrencyConvertorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExchangeRateServiceTest {

    @Mock
    private CurrencyConvertorRepository repository;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private CurrencyApiClient currencyApiClient;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private ExchangeRateService exchangeRateService;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void testGetCachedRates_ReturnsCachedValue() {
        when(valueOperations.get("latest_rates_USD")).thenReturn("{\"EUR\":0.9}");

        String result = exchangeRateService.getCachedRates("USD");

        assertEquals("{\"EUR\":0.9}", result);
    }

    @Test
    void testGetCachedRates_ReturnsNullIfNotFound() {
        when(valueOperations.get("latest_rates_EUR")).thenReturn(null);

        String result = exchangeRateService.getCachedRates("EUR");

        assertNull(result);
    }

    @Test
    void testGetRatesBasedOnCurrency_ParsesJsonCorrectly() throws Exception {
        String json = "{\"EUR\":0.9,\"JPY\":110.0}";
        when(valueOperations.get("latest_rates_USD")).thenReturn(json);

        Map<String, BigDecimal> result = exchangeRateService.getRatesBasedOnCurrency("USD");

        assertEquals(2, result.size());
        assertEquals(new BigDecimal("0.9"), result.get("EUR"));
        assertEquals(new BigDecimal("110.0"), result.get("JPY"));
    }

    @Test
    void testGetRatesBasedOnCurrency_ThrowsIfCacheMissing() {
        when(valueOperations.get("latest_rates_GBP")).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            exchangeRateService.getRatesBasedOnCurrency("GBP");
        });

        assertTrue(ex.getMessage().contains("Failed to get rates for base:"));
    }

    @Test
    void testUpdateAllCurrencyRates_SavesToDbAndCache() throws Exception {
        String mockJson = """
                {
                  "timestamp": 1714821234,
                  "base": "USD",
                  "rates": {
                    "USD": 1.0,
                    "EUR": 0.9,
                    "JPY": 110.0
                  }
                }
                """;

        when(currencyApiClient.getLatestRates("USD")).thenReturn(Mono.just(mockJson));

        exchangeRateService.updateAllCurrencyRates();

        verify(repository, atLeast(1)).save(any(CurrencyConvertor.class));
        verify(valueOperations, atLeast(1)).set(startsWith("latest_rates_"), anyString());
    }

    @Test
    void testUpdateAllCurrencyRates_ThrowsOnApiFailure() {
        when(currencyApiClient.getLatestRates("USD")).thenThrow(new RuntimeException("API failed"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            exchangeRateService.updateAllCurrencyRates();
        });

        assertTrue(ex.getMessage().contains("Failed to update all exchange rates"));
    }
}

