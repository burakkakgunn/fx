package com.fxexchange.fx.service;

import com.fxexchange.fx.external.client.ApiLayerExchangeRateClient;
import com.fxexchange.fx.external.model.ApiLayerResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ApiLayerConversionStrategyTest {

    private ApiLayerExchangeRateClient exchangeRateClient;
    private ApiLayerConversionStrategy strategy;

    @BeforeEach
    void setUp() {
        exchangeRateClient = mock(ApiLayerExchangeRateClient.class);
        strategy = new ApiLayerConversionStrategy(exchangeRateClient);
    }

    @Test
    void testConvert() {
        // Arrange
        BigDecimal amount = BigDecimal.valueOf(100);
        String source = "USD";
        String target = "EUR";
        BigDecimal rate = BigDecimal.valueOf(1.2);

        ApiLayerResponse response = mock(ApiLayerResponse.class);
        when(response.quotes()).thenReturn(Map.of("USDEUR", rate));
        when(exchangeRateClient.getExchangeRate(source, target)).thenReturn(response);

        // Act
        BigDecimal result = strategy.convert(amount, source, target);

        // Assert
        assertEquals(amount.multiply(rate), result);
        verify(exchangeRateClient).getExchangeRate(source, target);
    }
}
