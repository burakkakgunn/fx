package com.fxexchange.fx.external.model;

import java.math.BigDecimal;
import java.util.Map;

public record ApiLayerResponse (boolean success,
                                String source,
                                Map<String, BigDecimal> quotes){
}
