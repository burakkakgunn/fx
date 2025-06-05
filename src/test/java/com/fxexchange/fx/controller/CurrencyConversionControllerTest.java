package com.fxexchange.fx.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fxexchange.fx.dto.CurrencyConversionDto;
import com.fxexchange.fx.dto.CurrencyConversionRequest;
import com.fxexchange.fx.dto.CurrencyConversionResponse;
import com.fxexchange.fx.service.CurrencyConversionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CurrencyConversionController.class)
class CurrencyConversionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyConversionService conversionService;

    ObjectMapper objectMapper = new ObjectMapper();


    @Test
    void convertCurrency_shouldReturnConversionResponse() throws Exception {
        CurrencyConversionResponse mockResponse = new CurrencyConversionResponse();
        mockResponse.setConvertedAmount(BigDecimal.valueOf(150));
        Mockito.when(conversionService.convert(any())).thenReturn(mockResponse);

        CurrencyConversionRequest request = new CurrencyConversionRequest();
        request.setAmount(BigDecimal.valueOf(100));
        request.setSourceCurrency("USD");
        request.setTargetCurrency("EUR");

        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/currency/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.convertedAmount").value(150));

    }

    @Test
    void getConversionHistory_shouldReturnPagedResults() throws Exception {
        CurrencyConversionDto dto = CurrencyConversionDto.builder().sourceCurrency("USD").build();
        Page<CurrencyConversionDto> mockPage = new PageImpl<>(List.of(dto), PageRequest.of(0, 10), 1);
        Mockito.when(conversionService.getConversionHistory(any())).thenReturn(mockPage);

        String requestBody = "{}";

        mockMvc.perform(post("/api/currency/conversion/history")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].sourceCurrency").value("USD"));
    }

    @Test
    void handleCsvUpload_shouldReturnConversionList() throws Exception {
        CurrencyConversionResponse response = new CurrencyConversionResponse();
        response.setConvertedAmount(BigDecimal.TEN);
        Mockito.when(conversionService.processCsvFile(any())).thenReturn(List.of(response));

        MockMultipartFile file = new MockMultipartFile(
                "file", "test.csv", "text/csv", "USD,EUR,100.00".getBytes()
        );

        mockMvc.perform(multipart("/api/currency/conversions/bulk")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].convertedAmount").value(10));
    }
}
