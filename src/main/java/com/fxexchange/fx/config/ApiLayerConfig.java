package com.fxexchange.fx.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "apilayer")
@Data
public class ApiLayerConfig {
    private String accessKey;
}
