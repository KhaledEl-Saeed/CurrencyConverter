package com.example.CurrencyConverter.service.serviceImpl;

import com.example.CurrencyConverter.Response.CurrencyResponse;
import com.example.CurrencyConverter.WebClient.CurrencyApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class CurrencyServiceImplTest {

    @Mock
    private CurrencyApiClient apiClient;

    @InjectMocks
    private CurrencyServiceImpl currencyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFetchCurrencies_Success() {

        Map<String, String> mockCurrencies = Map.of("USD", "United States Dollar", "EUR", "Euro");
        when(apiClient.getCurrencies()).thenReturn(mockCurrencies);

        CurrencyResponse response = currencyService.fetchCurrencies();

        assertNotNull(response);
        assertEquals("success", response.getStatus());
        assertEquals(2, response.getCurrencies().size());
        assertEquals("United States Dollar", response.getCurrencies().get("USD"));

        verify(apiClient, times(2)).getCurrencies(); // Because it's called twice in the method
    }

    @Test
    void testFetchCurrencies_Failure_NullResponse() {

        when(apiClient.getCurrencies()).thenReturn(null);

        CurrencyResponse response = currencyService.fetchCurrencies();

        assertNotNull(response);
        assertEquals("fail", response.getStatus());
        assertNull(response.getCurrencies());

        verify(apiClient, times(1)).getCurrencies();
    }

    @Test
    void testFetchCurrencies_EmptyMap() {

        when(apiClient.getCurrencies()).thenReturn(Map.of());

        CurrencyResponse response = currencyService.fetchCurrencies();

        assertNotNull(response);
        assertEquals("success", response.getStatus());
        assertNotNull(response.getCurrencies());
        assertTrue(response.getCurrencies().isEmpty());

        verify(apiClient, times(2)).getCurrencies();
    }

    @Test
    void testFetchCurrencies_ThrowsException() {

        when(apiClient.getCurrencies()).thenThrow(new RuntimeException("API failure"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            currencyService.fetchCurrencies();
        });

        assertEquals("API failure", thrown.getMessage());
        verify(apiClient, times(1)).getCurrencies(); // only one call before the exception
    }


}
