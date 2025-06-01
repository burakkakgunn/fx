package com.fxexchange.fx.external.client;

import com.fxexchange.fx.external.model.ApiLayerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Slf4j
public class ApiLayerExchangeRateClient {
    private final WebClient webClient;
    private final String accessKey;

    public ApiLayerExchangeRateClient(WebClient.Builder webClientBuilder,
                                      @Value("${apilayer.access-key}") String accessKey) {
        this.webClient = webClientBuilder
                .baseUrl("https://apilayer.net/api")
                .build();
        this.accessKey = accessKey;
    }
    @Cacheable(value = "exchangeRates", key = "#source + '_' + #target")
    public ApiLayerResponse getExchangeRate(String source, String target) {
        ApiLayerResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/live")
                        .queryParam("access_key", accessKey)
                        .queryParam("source", source)
                        .queryParam("currencies", target)
                        .queryParam("format", 1)
                        .build())
                .retrieve()
                .bodyToMono(ApiLayerResponse.class)
                .block();
        log.info("exchangeRateServiceCalled");
        if (response == null || !response.success() || !response.quotes().containsKey(source + target)) {
            throw new IllegalArgumentException("Rate not found for: " + source + " to " + target);
        }

        return response;
    }
}
