package com.example.CurrencyConverter.repository;

import com.example.CurrencyConverter.entity.CurrencyConvertor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyConvertorRepository extends JpaRepository<CurrencyConvertor, Long> {
}
