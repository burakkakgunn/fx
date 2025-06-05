package com.fxexchange.fx.external.client;

import com.fxexchange.fx.external.model.ApiLayerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
@Slf4j
public class ApiLayerExchangeRateClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String accessKey;

    public ApiLayerExchangeRateClient(@Value("${apilayer.access-key}") String accessKey) {
        this.accessKey = accessKey;
    }

    @Cacheable(value = "exchangeRates", key = "#source + '_' + #target")
    public ApiLayerResponse getExchangeRate(String source, String target) {
        URI uri = UriComponentsBuilder
                .fromHttpUrl("https://apilayer.net/api/live")
                .queryParam("access_key", accessKey)
                .queryParam("source", source)
                .queryParam("currencies", target)
                .queryParam("format", 1)
                .build()
                .toUri();

        log.info("Calling exchange rate service for {} to {}", source, target);
        ApiLayerResponse response = restTemplate.getForObject(uri, ApiLayerResponse.class);

        if (response == null || !response.success() || !response.quotes().containsKey(source + target)) {
            throw new IllegalArgumentException("Rate not found for: " + source + " to " + target);
        }

        return response;
    }
}
