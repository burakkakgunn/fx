package com.fxexchange.fx.service;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Component
public class CurrencyConversionContext {
    private final Map<String, CurrencyConversionStrategy> strategies;
    private CurrencyConversionStrategy selectedStrategy;

    public CurrencyConversionContext(Map<String, CurrencyConversionStrategy> strategies) {
        this.strategies = strategies;
    }

    public void setStrategy(String strategyKey) {
        CurrencyConversionStrategy strategy = strategies.get(strategyKey);
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown strategy: " + strategyKey);
        }
        this.selectedStrategy = strategy;
    }

    public BigDecimal convert(BigDecimal amount, String source, String target) {
        if (selectedStrategy == null) {
            throw new IllegalStateException("Conversion strategy not set");
        }
        return selectedStrategy.convert(amount, source, target);
    }

}
