package com.fxexchange.fx.service;

import com.fxexchange.fx.dto.ExchangeRateDto;
import com.fxexchange.fx.external.client.ApiLayerExchangeRateClient;
import com.fxexchange.fx.external.model.ApiLayerResponse;
import com.fxexchange.fx.mapper.ExchangeRateMapper;
import com.fxexchange.fx.repository.ExchangeRateRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class ExchangeRateService {

    private final ApiLayerExchangeRateClient apiLayerExchangeRateClient;

    private final ExchangeRateRepository exchangeRateRepository;
    private final ExchangeRateMapper exchangeRateMapper;

    public ExchangeRateService(ApiLayerExchangeRateClient apiLayerExchangeRateClient, ExchangeRateRepository exchangeRateRepository,
                               ExchangeRateMapper exchangeRateMapper) {
        this.apiLayerExchangeRateClient = apiLayerExchangeRateClient;
        this.exchangeRateRepository = exchangeRateRepository;
        this.exchangeRateMapper = exchangeRateMapper;
    }

    public ExchangeRateDto getExchangeRate(String from, String to) {
        ApiLayerResponse apiLayerResponse = apiLayerExchangeRateClient.getExchangeRate(from, to);
        ExchangeRateDto dto = new ExchangeRateDto();
        dto.setFromCurrency(from);
        dto.setToCurrency(to);
        dto.setRate(apiLayerResponse.quotes().get(from + to));
        dto.setCreateDate(LocalDateTime.now());
        dto.setRetrievedAt(LocalDateTime.now());
        dto.setCreatedBy("BURAK");
        return saveExchangeRate(dto);
    }

    public ExchangeRateDto saveExchangeRate(ExchangeRateDto exchangeRateDto) {
        return exchangeRateMapper.toDto(exchangeRateRepository.save(exchangeRateMapper.toEntity(exchangeRateDto)));
    }
}
