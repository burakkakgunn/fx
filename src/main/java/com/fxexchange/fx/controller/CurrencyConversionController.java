package com.fxexchange.fx.controller;

import com.fxexchange.fx.dto.ConversionHistoryRequest;
import com.fxexchange.fx.dto.CurrencyConversionDto;
import com.fxexchange.fx.dto.CurrencyConversionRequest;
import com.fxexchange.fx.dto.CurrencyConversionResponse;
import com.fxexchange.fx.service.CurrencyConversionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/currency")
@AllArgsConstructor
@Tag(name = "Currency Conversion", description = "Endpoints for currency conversion operations. Supports real-time single conversions and bulk processing via CSV upload.Exchange rates are updated. ")
public class CurrencyConversionController {

    private final CurrencyConversionService conversionService;

    @PostMapping("/convert")
    @Operation(summary = "Convert currency", description = "Converts a given amount from a source currency to a target currency.")
    public ResponseEntity<CurrencyConversionResponse> convertCurrency(@RequestBody @Valid CurrencyConversionRequest request) {
        CurrencyConversionResponse response = conversionService.convert(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/conversion/history")
    @Operation(summary = "Get conversion by criteria", description = "Returns details of a specific currency conversion by criteria parameters.")
    public ResponseEntity<Page<CurrencyConversionDto>> getConversionHistory(@RequestBody ConversionHistoryRequest conversionHistoryRequest) {
        Page<CurrencyConversionDto> history = conversionService.getConversionHistory(conversionHistoryRequest);
        return ResponseEntity.ok(history);
    }

    @PostMapping(value = "/conversions/bulk", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Convert currency with csv.", description ="Processes bulk currency conversions via CSV upload.Required CSV columns: sourceCurrency, targetCurrency (ISO 3-letter codes), amount (positive decimal).Max file size: 2MB, supports .csv/.txt formats. Example row: USD,EUR,100.00")
    public ResponseEntity<List<CurrencyConversionResponse>> handleCsvUpload(@RequestPart("file") MultipartFile file) {
        List<CurrencyConversionResponse> responseList = conversionService.processCsvFile(file);
        return ResponseEntity.ok(responseList);
    }

}
