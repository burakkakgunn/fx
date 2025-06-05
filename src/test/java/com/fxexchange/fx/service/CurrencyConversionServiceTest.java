package com.fxexchange.fx.service;

import com.fxexchange.fx.config.FxConversionProperties;
import com.fxexchange.fx.dao.CurrencyConversion;
import com.fxexchange.fx.dto.ConversionHistoryRequest;
import com.fxexchange.fx.dto.CurrencyConversionDto;
import com.fxexchange.fx.dto.CurrencyConversionRequest;
import com.fxexchange.fx.dto.CurrencyConversionResponse;
import com.fxexchange.fx.mapper.CurrencyConversionMapper;
import com.fxexchange.fx.repository.CurrencyConversionRepository;
import com.fxexchange.fx.repository.CurrencyConversionRepositoryCustom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class CurrencyConversionServiceTest {

    @InjectMocks
    private CurrencyConversionService conversionService;

    @Mock
    private ExchangeRateService exchangeRateService;

    @Mock
    private CurrencyConversionContext conversionContext;

    @Mock
    private CurrencyConversionRepository repository;

    @Mock
    private CurrencyConversionRepositoryCustom currencyConversionRepositoryCustom;

    @Mock
    private CurrencyConversionMapper currencyConversionMapper;

    @Mock
    private FxConversionProperties properties;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(properties.getStrategy()).thenReturn("RATE_BASED");
        conversionService = new CurrencyConversionService(
                exchangeRateService, properties, conversionContext, repository,
                currencyConversionRepositoryCustom, currencyConversionMapper
        );
    }

    @Test
    void testConvert() {
        CurrencyConversionRequest request = CurrencyConversionRequest.builder()
                .amount(BigDecimal.valueOf(100))
                .sourceCurrency("USD")
                .targetCurrency("EUR")
                .build();

        when(conversionContext.convert(BigDecimal.valueOf(100), "USD", "EUR"))
                .thenReturn(BigDecimal.valueOf(1.5));

        CurrencyConversionResponse response = conversionService.convert(request);

        assertNotNull(response);
        assertEquals(BigDecimal.valueOf(150.0), response.getConvertedAmount());
        assertEquals("USD", response.getSourceCurrency());
        assertEquals("EUR", response.getTargetCurrency());
        verify(repository, times(1)).save(any(CurrencyConversion.class));
    }

    @Test
    void testGetConversionHistory() {
        ConversionHistoryRequest historyRequest = new ConversionHistoryRequest();
        historyRequest.setTransactionId("abc-123");
        historyRequest.setTransactionDate(LocalDate.now());
        historyRequest.setPage(0);
        historyRequest.setSize(10);

        Page<CurrencyConversion> mockPage = new PageImpl<>(List.of(new CurrencyConversion()));
        when(currencyConversionRepositoryCustom.findByCriteria(any(), any(), any()))
                .thenReturn(mockPage);
        when(currencyConversionMapper.toDto(any())).thenReturn(CurrencyConversionDto.builder().build());

        Page<CurrencyConversionDto> result = conversionService.getConversionHistory(historyRequest);

        assertEquals(1, result.getContent().size());
    }

    @Test
    void testParseCsv() throws IOException {
        String csvContent = "sourceCurrency;targetCurrency;amount\nUSD;EUR;100.00";
        MockMultipartFile mockFile = new MockMultipartFile("file", "conversions.csv",
                "text/csv", new ByteArrayInputStream(csvContent.getBytes()));

        List<CurrencyConversionRequest> result = conversionService.parseCsv(mockFile);

        assertEquals(1, result.size());
        assertEquals("USD", result.get(0).getSourceCurrency());
        assertEquals("EUR", result.get(0).getTargetCurrency());
        assertEquals(new BigDecimal("100.00"), result.get(0).getAmount());
    }

    @Test
    void testProcessCsvFile() {
        CurrencyConversionRequest csvRequest = CurrencyConversionRequest.builder()
                .sourceCurrency("USD")
                .targetCurrency("EUR")
                .amount(BigDecimal.valueOf(100))
                .build();

        CurrencyConversionResponse mockResponse = new CurrencyConversionResponse();
        mockResponse.setConvertedAmount(BigDecimal.valueOf(150));

        CurrencyConversionService spyService = Mockito.spy(conversionService);
        doReturn(List.of(csvRequest)).when(spyService).parseCsv(any());
        doReturn(mockResponse).when(spyService).convert(any());

        MockMultipartFile mockFile = new MockMultipartFile("file", "conversions.csv",
                "text/csv", "sourceCurrency;targetCurrency;amount\nUSD;EUR;100.00".getBytes());

        List<CurrencyConversionResponse> results = spyService.processCsvFile(mockFile);

        assertEquals(1, results.size());
        assertEquals(BigDecimal.valueOf(150), results.get(0).getConvertedAmount());
    }
}
