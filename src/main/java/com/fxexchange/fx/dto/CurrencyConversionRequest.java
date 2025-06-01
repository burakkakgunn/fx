package com.fxexchange.fx.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyConversionRequest {

    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than zero.")
    private BigDecimal amount;

    @NotBlank(message = "Source currency must not be blank.")
    @Size(min = 3, max = 3, message = "Source currency must be a 3-letter code.")
    private String sourceCurrency;

    @NotBlank(message = "Target currency must not be blank.")
    @Size(min = 3, max = 3, message = "Target currency must be a 3-letter code.")
    private String targetCurrency;

}
