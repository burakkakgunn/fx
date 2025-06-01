package com.fxexchange.fx.controller;

import com.fxexchange.fx.dao.CurrencyConversion;
import com.fxexchange.fx.dto.ConversionHistoryRequest;
import com.fxexchange.fx.dto.CurrencyConversionRequest;
import com.fxexchange.fx.dto.CurrencyConversionResponse;
import com.fxexchange.fx.service.CurrencyConversionService;
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
public class CurrencyConversionController {

    private final CurrencyConversionService conversionService;

    @PostMapping("/convert")
    public ResponseEntity<CurrencyConversionResponse> convertCurrency(@RequestBody @Valid CurrencyConversionRequest request) {
        CurrencyConversionResponse response = conversionService.convert(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/conversion/history")
    public ResponseEntity<Page<CurrencyConversion>> getConversionHistory(@RequestBody ConversionHistoryRequest conversionHistoryRequest) {
        Page<CurrencyConversion> history = conversionService.getConversionHistory(conversionHistoryRequest);
        return ResponseEntity.ok(history);
    }

    @PostMapping(value = "/conversions/bulk", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<CurrencyConversionResponse>> handleCsvUpload(@RequestPart("file") MultipartFile file) {
        List<CurrencyConversionResponse> responseList = conversionService.processCsvFile(file);
        return ResponseEntity.ok(responseList);
    }

}
