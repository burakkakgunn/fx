package com.fxexchange.fx.service;

import com.fxexchange.fx.external.client.ApiLayerExchangeRateClient;
import com.fxexchange.fx.external.model.ApiLayerResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component("apiLayerStrategy")
public class ApiLayerConversionStrategy implements CurrencyConversionStrategy{

    private final ApiLayerExchangeRateClient apiLayerExchangeRateClient;

    public ApiLayerConversionStrategy(ApiLayerExchangeRateClient apiLayerExchangeRateClient) {
        this.apiLayerExchangeRateClient = apiLayerExchangeRateClient;
    }

    @Override
    public BigDecimal convert(BigDecimal amount, String sourceCurrency, String targetCurrency) {
        ApiLayerResponse apiLayerResponse = apiLayerExchangeRateClient.getExchangeRate(sourceCurrency, targetCurrency);
        BigDecimal rate = apiLayerResponse.quotes().get(sourceCurrency + targetCurrency);
        return amount.multiply(rate);
    }
}
