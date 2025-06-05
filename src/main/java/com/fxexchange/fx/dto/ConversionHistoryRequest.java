package com.fxexchange.fx.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversionHistoryRequest {

    private String transactionId;

    private LocalDate transactionDate;
    @Builder.Default
    private int page = 0;
    @Builder.Default
    private int size = 10;
}
