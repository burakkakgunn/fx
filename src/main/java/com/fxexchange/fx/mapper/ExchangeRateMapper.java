package com.fxexchange.fx.mapper;

import com.fxexchange.fx.dao.ExchangeRate;
import com.fxexchange.fx.dto.ExchangeRateDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ExchangeRateMapper {
    ExchangeRate toEntity(ExchangeRateDto exchangeRateDto);

    ExchangeRateDto toDto(ExchangeRate exchangeRate);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ExchangeRate partialUpdate(ExchangeRateDto exchangeRateDto, @MappingTarget ExchangeRate exchangeRate);
}