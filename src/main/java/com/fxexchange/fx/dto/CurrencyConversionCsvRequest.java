package com.fxexchange.fx.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyConversionCsvRequest {
    private BigDecimal amount;
    private String sourceCurrency;
    private String targetCurrency;
}
