package com.fxexchange.fx.service;

import java.math.BigDecimal;

public interface CurrencyConversionStrategy {
    BigDecimal convert(BigDecimal amount, String sourceCurrency, String targetCurrency);

}
