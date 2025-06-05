package com.fxexchange.fx.service;

import com.fxexchange.fx.dao.ExchangeRate;
import com.fxexchange.fx.dto.ExchangeRateDto;
import com.fxexchange.fx.external.client.ApiLayerExchangeRateClient;
import com.fxexchange.fx.external.model.ApiLayerResponse;
import com.fxexchange.fx.mapper.ExchangeRateMapper;
import com.fxexchange.fx.repository.ExchangeRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class ExchangeRateServiceTest {

    @Mock
    private ApiLayerExchangeRateClient apiLayerExchangeRateClient;

    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    @Mock
    private ExchangeRateMapper exchangeRateMapper;

    @InjectMocks
    private ExchangeRateService exchangeRateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetExchangeRate() {
        // Arrange
        String from = "USD";
        String to = "EUR";
        BigDecimal expectedRate = BigDecimal.valueOf(1.09);

        ApiLayerResponse mockResponse = mock(ApiLayerResponse.class);
        when(mockResponse.quotes()).thenReturn(Map.of("USDEUR", expectedRate));

        when(apiLayerExchangeRateClient.getExchangeRate(from, to)).thenReturn(mockResponse);

        ExchangeRateDto dtoToSave = new ExchangeRateDto();
        dtoToSave.setFromCurrency(from);
        dtoToSave.setToCurrency(to);
        dtoToSave.setRate(expectedRate);
        dtoToSave.setCreatedBy("BURAK");
        dtoToSave.setCreateDate(LocalDateTime.now());
        dtoToSave.setRetrievedAt(LocalDateTime.now());

        ExchangeRate entity = new ExchangeRate(); // Assume ExchangeRate is your JPA entity class
        when(exchangeRateMapper.toEntity(any())).thenReturn(entity);
        when(exchangeRateRepository.save(entity)).thenReturn(entity);
        when(exchangeRateMapper.toDto(entity)).thenReturn(dtoToSave);

        // Act
        ExchangeRateDto result = exchangeRateService.getExchangeRate(from, to);

        // Assert
        assertNotNull(result);
        assertEquals(from, result.getFromCurrency());
        assertEquals(to, result.getToCurrency());
        assertEquals(expectedRate, result.getRate());
        assertEquals("BURAK", result.getCreatedBy());

        verify(apiLayerExchangeRateClient).getExchangeRate(from, to);
        verify(exchangeRateRepository).save(any());
    }

    @Test
    void testSaveExchangeRate() {
        ExchangeRateDto dto = new ExchangeRateDto();
        dto.setFromCurrency("USD");
        dto.setToCurrency("TRY");
        dto.setRate(BigDecimal.valueOf(31.3));

        ExchangeRate entity = new ExchangeRate();
        when(exchangeRateMapper.toEntity(dto)).thenReturn(entity);
        when(exchangeRateRepository.save(entity)).thenReturn(entity);
        when(exchangeRateMapper.toDto(entity)).thenReturn(dto);

        ExchangeRateDto result = exchangeRateService.saveExchangeRate(dto);

        assertNotNull(result);
        assertEquals("USD", result.getFromCurrency());
        assertEquals("TRY", result.getToCurrency());
        assertEquals(BigDecimal.valueOf(31.3), result.getRate());
    }
}