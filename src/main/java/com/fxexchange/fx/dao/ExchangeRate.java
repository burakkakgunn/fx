package com.fxexchange.fx.dao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "exchange_rate")
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false, length = 3)
    private String fromCurrency;

    @Column(nullable = false, length = 3)
    private String toCurrency;

    @Column(nullable = false, precision = 19, scale = 6)
    private BigDecimal rate;

    @Column(nullable = false)
    private LocalDateTime retrievedAt;

    @Column(nullable = false)
    private LocalDateTime createDate;

    @Column(nullable = false, length = 50)
    private String createdBy;

}