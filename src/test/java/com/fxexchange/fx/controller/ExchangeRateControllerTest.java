package com.fxexchange.fx.controller;
import com.fxexchange.fx.dto.ExchangeRateDto;
import com.fxexchange.fx.service.ExchangeRateService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExchangeRateController.class)
class ExchangeRateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExchangeRateService exchangeRateService;

    @Test
    @DisplayName("Should return exchange rate for valid 'from' and 'to' parameters")
    void testGetExchangeRate() throws Exception {
        // given
        ExchangeRateDto mockResponse = ExchangeRateDto.builder().toCurrency("USD").fromCurrency("EUR").build();
        Mockito.when(exchangeRateService.getExchangeRate(anyString(), anyString())).thenReturn(mockResponse);

        // when & then
        mockMvc.perform(get("/api/exchange-rate")
                        .param("from", "usd")
                        .param("to", "eur")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fromCurrency").value("EUR"))
                .andExpect(jsonPath("$.toCurrency").value("USD"));
    }
}