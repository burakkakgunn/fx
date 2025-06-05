package com.fxexchange.fx.external.client;

import com.fxexchange.fx.external.model.ApiLayerResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;


@SpringBootTest(properties = "apilayer.access-key=test_key")
class ApiLayerExchangeRateClientIntegrationTest {

    private ApiLayerExchangeRateClient apiLayerExchangeRateClient;

    private MockRestServiceServer mockServer;

    @Value("${apilayer.access-key}")
    private String accessKey;

    @BeforeEach
    void setup() {
        // ApiLayerExchangeRateClient oluştur, accessKey ile
        apiLayerExchangeRateClient = new ApiLayerExchangeRateClient(accessKey);

        // RestTemplate içindeki private field 'restTemplate' referansını al
        RestTemplate restTemplate = (RestTemplate) ReflectionTestUtils.getField(apiLayerExchangeRateClient, "restTemplate");

        // MockRestServiceServer oluştur
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void testGetExchangeRate_Success() {
        String source = "USD";
        String target = "EUR";

        String jsonResponse = """
                {
                  "success": true,
                  "quotes": {
                    "USDEUR": 0.85
                  }
                }
                """;

        URI uri = UriComponentsBuilder
                .fromHttpUrl("https://apilayer.net/api/live")
                .queryParam("access_key", accessKey)
                .queryParam("source", source)
                .queryParam("currencies", target)
                .queryParam("format", 1)
                .build()
                .toUri();

        mockServer.expect(once(), requestTo(uri))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        ApiLayerResponse response = apiLayerExchangeRateClient.getExchangeRate(source, target);

        assertNotNull(response);
        assertTrue(response.success());
        assertEquals(0.85, response.quotes().get("USDEUR").doubleValue());

        mockServer.verify();
    }

    @Test
    void testGetExchangeRate_Failure_ThrowsException() {
        String source = "USD";
        String target = "EUR";

        String jsonResponse = """
                {
                  "success": false,
                  "quotes": {}
                }
                """;

        URI uri = UriComponentsBuilder
                .fromHttpUrl("https://apilayer.net/api/live")
                .queryParam("access_key", accessKey)
                .queryParam("source", source)
                .queryParam("currencies", target)
                .queryParam("format", 1)
                .build()
                .toUri();

        mockServer.expect(once(), requestTo(uri))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> apiLayerExchangeRateClient.getExchangeRate(source, target));

        assertTrue(thrown.getMessage().contains("Rate not found"));

        mockServer.verify();
    }
}
