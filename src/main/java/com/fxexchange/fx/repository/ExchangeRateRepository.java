package com.fxexchange.fx.repository;

import com.fxexchange.fx.dao.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
}