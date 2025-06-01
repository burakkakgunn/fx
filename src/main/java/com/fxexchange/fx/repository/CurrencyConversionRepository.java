package com.fxexchange.fx.repository;

import com.fxexchange.fx.dao.CurrencyConversion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CurrencyConversionRepository extends JpaRepository<CurrencyConversion, String>, JpaSpecificationExecutor<CurrencyConversion> {

}