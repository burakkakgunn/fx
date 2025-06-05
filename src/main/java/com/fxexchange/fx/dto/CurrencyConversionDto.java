package com.fxexchange.fx.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.fxexchange.fx.dao.CurrencyConversion}
 */
@AllArgsConstructor
@Getter
@Builder
public class CurrencyConversionDto implements Serializable {
    private final Long id;
    private final String transactionId;
    private final BigDecimal amount;
    private final String sourceCurrency;
    private final String targetCurrency;
    private final BigDecimal convertedAmount;
    private final LocalDateTime transactionDate;
}