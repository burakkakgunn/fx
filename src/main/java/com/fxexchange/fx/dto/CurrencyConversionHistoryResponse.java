package com.fxexchange.fx.dto;

import com.fxexchange.fx.dao.CurrencyConversion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyConversionHistoryResponse {
    private Page<CurrencyConversion> currencyConversions;

}
