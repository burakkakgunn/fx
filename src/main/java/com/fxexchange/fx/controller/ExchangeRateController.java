package com.fxexchange.fx.controller;

import com.fxexchange.fx.dto.ExchangeRateDto;
import com.fxexchange.fx.service.ExchangeRateService;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/exchange-rate")
@Tag(
        name = "Exchange Rates",
        description = """
        Real-time and historical foreign exchange rate data.
        """,
        externalDocs = @ExternalDocumentation(
                description = "Rate calculation end point",
                url = "https://apilayer.net/api/live?access_key=e3e348a749b28b2cffc9af8b89fe8afa&currencies=EUR,GBP,CAD,PLN&source=USD&format=1"
        )
)
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
