package com.fxexchange.fx.dto;


import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateDto implements Serializable {
    private Long id;
    private String fromCurrency;
    private String toCurrency;
    private BigDecimal rate;
    private LocalDateTime retrievedAt;
    private LocalDateTime createDate;
    private String createdBy;
}
