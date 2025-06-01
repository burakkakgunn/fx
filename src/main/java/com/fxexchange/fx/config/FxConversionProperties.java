package com.fxexchange.fx.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "fx")
@Getter
@Setter
public class FxConversionProperties {
    private String strategy;
}
