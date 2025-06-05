package com.fxexchange.fx.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CurrencyConversionContextTest {

    private CurrencyConversionStrategy mockStrategy;
    private CurrencyConversionContext context;

    @BeforeEach
    void setUp() {
        mockStrategy = mock(CurrencyConversionStrategy.class);

        Map<String, CurrencyConversionStrategy> strategies = new HashMap<>();
        strategies.put("default", mockStrategy);

        context = new CurrencyConversionContext(strategies);
    }

    @Test
    void testSetAndUseStrategySuccessfully() {
        BigDecimal inputAmount = BigDecimal.valueOf(100);
        BigDecimal expectedAmount = BigDecimal.valueOf(110);

        when(mockStrategy.convert(inputAmount, "USD", "EUR")).thenReturn(expectedAmount);

        context.setStrategy("default");
        BigDecimal result = context.convert(inputAmount, "USD", "EUR");

        assertEquals(expectedAmount, result);
        verify(mockStrategy).convert(inputAmount, "USD", "EUR");
    }

    @Test
    void testSetStrategyThrowsExceptionForUnknownKey() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            context.setStrategy("nonexistent");
        });

        assertEquals("Unknown strategy: nonexistent", exception.getMessage());
    }

    @Test
    void testConvertThrowsExceptionIfStrategyNotSet() {
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            context.convert(BigDecimal.TEN, "USD", "EUR");
        });

        assertEquals("Conversion strategy not set", exception.getMessage());
    }
}
