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
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CurrencyConversionService {

    private final ExchangeRateService exchangeRateService;
    private final CurrencyConversionContext conversionContext;
    private final CurrencyConversionRepository repository;
    private  final CurrencyConversionRepositoryCustom currencyConversionRepositoryCustom;
    private final CurrencyConversionMapper currencyConversionMapper;

    public CurrencyConversionService(ExchangeRateService exchangeRateService, FxConversionProperties properties, CurrencyConversionContext conversionContext, CurrencyConversionRepository repository, CurrencyConversionRepositoryCustom currencyConversionRepositoryCustom,
                                     CurrencyConversionMapper currencyConversionMapper) {
        this.exchangeRateService = exchangeRateService;
        this.conversionContext = conversionContext;
        this.conversionContext.setStrategy(properties.getStrategy());
        this.repository = repository;
        this.currencyConversionRepositoryCustom = currencyConversionRepositoryCustom;
        this.currencyConversionMapper = currencyConversionMapper;
    }


    public CurrencyConversionResponse convert(CurrencyConversionRequest request) {
        //BigDecimal rate = exchangeRateService.getExchangeRate(request.getSourceCurrency(), request.getTargetCurrency()).getRate();

        BigDecimal rate = conversionContext.convert(request.getAmount(), request.getSourceCurrency(), request.getTargetCurrency());
        BigDecimal convertedAmount = request.getAmount().multiply(rate);

        String transactionId = UUID.randomUUID().toString();
        CurrencyConversion conversion = new CurrencyConversion();
        conversion.setTransactionId(transactionId);
        conversion.setAmount(request.getAmount());
        conversion.setSourceCurrency(request.getSourceCurrency());
        conversion.setTargetCurrency(request.getTargetCurrency());
        conversion.setConvertedAmount(convertedAmount);
        conversion.setTransactionDate(LocalDateTime.now());

        repository.save(conversion);

        return new CurrencyConversionResponse(conversion.getId(),transactionId,request.getAmount(), request.getSourceCurrency(), request.getTargetCurrency(),convertedAmount,conversion.getTransactionDate());
    }

    public Page<CurrencyConversionDto> getConversionHistory(ConversionHistoryRequest conversionHistoryRequest) {
        Page<CurrencyConversion> currencyPage = currencyConversionRepositoryCustom.findByCriteria(conversionHistoryRequest.getTransactionId(), conversionHistoryRequest.getTransactionDate(), PageRequest.of(conversionHistoryRequest.getPage(), conversionHistoryRequest.getSize()));
        return currencyPage.map(currencyConversionMapper::toDto);
    }

    public List<CurrencyConversionResponse> processCsvFile(MultipartFile file) {
        List<CurrencyConversionRequest> conversionRequests = parseCsv(file);
        List<CurrencyConversionResponse> responses = new ArrayList<>();

        for (CurrencyConversionRequest req : conversionRequests) {
            CurrencyConversionResponse response = convert(CurrencyConversionRequest.builder().sourceCurrency(req.getSourceCurrency()).targetCurrency(req.getTargetCurrency()).amount(req.getAmount()).build());
            responses.add(response);
        }

        return responses;
    }

    public List<CurrencyConversionRequest> parseCsv(MultipartFile file) {
        List<CurrencyConversionRequest> conversionRequests = new ArrayList<>();

        try (
                BOMInputStream bomInputStream = new BOMInputStream(file.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(bomInputStream, StandardCharsets.UTF_8));
                CSVParser csvParser = CSVFormat.DEFAULT
                        .withDelimiter(';')
                        .withFirstRecordAsHeader()
                        .withIgnoreHeaderCase()
                        .withTrim()
                        .parse(reader)
        ) {
            for (CSVRecord csvRecord : csvParser) {
                CurrencyConversionRequest request = new CurrencyConversionRequest();
                request.setAmount(new BigDecimal(csvRecord.get("amount")));
                request.setSourceCurrency(csvRecord.get("sourceCurrency"));
                request.setTargetCurrency(csvRecord.get("targetCurrency"));
                conversionRequests.add(request);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse CSV file: " + e.getMessage(), e);
        }

        return conversionRequests;
    }
}
