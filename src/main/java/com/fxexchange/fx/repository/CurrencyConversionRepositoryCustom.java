package com.fxexchange.fx.repository;

import com.fxexchange.fx.dao.CurrencyConversion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface CurrencyConversionRepositoryCustom {
    Page<CurrencyConversion> findByCriteria(String transactionId, LocalDate transactionDate, Pageable pageable);

}
