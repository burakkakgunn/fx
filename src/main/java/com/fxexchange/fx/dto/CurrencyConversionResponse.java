package com.fxexchange.fx.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyConversionResponse {
    private Long id;
    private String transactionId;
    private BigDecimal amount;
    private String sourceCurrency;
    private String targetCurrency;
    private BigDecimal convertedAmount;
    private LocalDateTime transactionDate;
}
