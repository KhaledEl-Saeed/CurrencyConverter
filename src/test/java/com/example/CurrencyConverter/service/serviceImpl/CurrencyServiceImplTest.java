package com.example.CurrencyConverter.service.serviceImpl;

import com.example.CurrencyConverter.Response.CurrencyResponse;
import com.example.CurrencyConverter.WebClient.CurrencyApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        // Given
        Map<String, String> mockCurrencies = Map.of("USD", "United States Dollar", "EUR", "Euro");
        when(apiClient.getCurrencies()).thenReturn(mockCurrencies);

        // When
        CurrencyResponse response = currencyService.fetchCurrencies();

        // Then
        assertNotNull(response);
        assertEquals("success", response.getStatus());
        assertEquals(2, response.getCurrencies().size());
        assertEquals("United States Dollar", response.getCurrencies().get("USD"));

        verify(apiClient, times(2)).getCurrencies(); // Because it's called twice in the method
    }

    @Test
    void testFetchCurrencies_Failure_NullResponse() {
        // Given
        when(apiClient.getCurrencies()).thenReturn(null);

        // When
        CurrencyResponse response = currencyService.fetchCurrencies();

        // Then
        assertNotNull(response);
        assertEquals("fail", response.getStatus());
        assertNull(response.getCurrencies());

        verify(apiClient, times(1)).getCurrencies();
    }

    @Test
    void testFetchCurrencies_EmptyMap() {
        // Given
        when(apiClient.getCurrencies()).thenReturn(Map.of());

        // When
        CurrencyResponse response = currencyService.fetchCurrencies();

        // Then
        assertNotNull(response);
        assertEquals("success", response.getStatus());
        assertNotNull(response.getCurrencies());
        assertTrue(response.getCurrencies().isEmpty());

        verify(apiClient, times(2)).getCurrencies();
    }

    @Test
    void testFetchCurrencies_ThrowsException() {
        // Given
        when(apiClient.getCurrencies()).thenThrow(new RuntimeException("API failure"));

        // When & Then
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            currencyService.fetchCurrencies();
        });

        assertEquals("API failure", thrown.getMessage());
        verify(apiClient, times(1)).getCurrencies(); // only one call before the exception
    }


}
