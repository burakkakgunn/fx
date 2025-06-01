package com.fxexchange.fx.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "currency_conversion")
public class CurrencyConversion {

    @Id
    @Column(name = "transaction_id", nullable = false, unique = true, length = 36)
    private String transactionId;

    @Column(name = "amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(name = "source_currency", nullable = false, length = 3)
    private String sourceCurrency;

    @Column(name = "target_currency", nullable = false, length = 3)
    private String targetCurrency;

    @Column(name = "converted_amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal convertedAmount;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;
}
