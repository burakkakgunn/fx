package com.fxexchange.fx.mapper;

import com.fxexchange.fx.dao.CurrencyConversion;
import com.fxexchange.fx.dto.CurrencyConversionDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CurrencyConversionMapper {
    CurrencyConversion toEntity(CurrencyConversionDto currencyConversionDto);

    CurrencyConversionDto toDto(CurrencyConversion currencyConversion);

    List<CurrencyConversionDto> toDtoList(List<CurrencyConversion> entityList);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CurrencyConversion partialUpdate(CurrencyConversionDto currencyConversionDto, @MappingTarget CurrencyConversion currencyConversion);
}