package com.fxexchange.fx.controller;

import com.fxexchange.fx.dto.ExchangeRateDto;
import com.fxexchange.fx.service.ExchangeRateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/exchange-rate")
public class ExchangeRateController {

    private final ExchangeRateService service;

    public ExchangeRateController(ExchangeRateService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ExchangeRateDto> getExchangeRate(@RequestParam String from,
                                                           @RequestParam String to) {
        return ResponseEntity.ok(service.getExchangeRate(from.toUpperCase(), to.toUpperCase()));
    }

}
