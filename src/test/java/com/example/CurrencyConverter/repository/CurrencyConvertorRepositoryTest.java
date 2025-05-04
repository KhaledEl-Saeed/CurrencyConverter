package com.example.CurrencyConverter.repository;

import com.example.CurrencyConverter.entity.CurrencyConvertor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CurrencyConvertorRepositoryTest {

    @Autowired
    private CurrencyConvertorRepository repository;

    @Test
    @DisplayName("Given a new CurrencyConvertor, When saved, Then it should be retrievable by ID")
    void givenCurrencyConvertor_whenSaved_thenFindByIdReturnsIt() {

        CurrencyConvertor cc = new CurrencyConvertor();
        cc.setTimestamp(1714741280L);
        cc.setCurrency("USD");
        cc.setRates("{\"EUR\": 0.92}");

        CurrencyConvertor saved = repository.save(cc);
        Optional<CurrencyConvertor> found = repository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getCurrency()).isEqualTo("USD");
        assertThat(found.get().getRates()).contains("EUR");
    }

    @Test
    @DisplayName("Given a persisted CurrencyConvertor, When deleted, Then it should not be found by ID")
    void givenSavedCurrencyConvertor_whenDeleted_thenItShouldNotExist() {

        CurrencyConvertor cc = new CurrencyConvertor();
        cc.setTimestamp(1714741280L);
        cc.setCurrency("GBP");
        cc.setRates("{\"USD\": 1.25}");

        CurrencyConvertor saved = repository.save(cc);
        Long id = saved.getId();

        repository.deleteById(id);
        Optional<CurrencyConvertor> deleted = repository.findById(id);

        assertThat(deleted).isNotPresent();
    }

    @Test
    @DisplayName("Given multiple CurrencyConvertors, When findAll is called, Then it should return all entries")
    void givenMultipleCurrencyConvertors_whenFindAll_thenReturnsAll() {

        CurrencyConvertor cc1 = new CurrencyConvertor();
        cc1.setTimestamp(1714741280L);
        cc1.setCurrency("JPY");
        cc1.setRates("{\"USD\": 0.0065}");

        CurrencyConvertor cc2 = new CurrencyConvertor();
        cc2.setTimestamp(1714741281L);
        cc2.setCurrency("CAD");
        cc2.setRates("{\"USD\": 0.74}");

        repository.save(cc1);
        repository.save(cc2);

        var all = repository.findAll();

        assertThat(all).hasSizeGreaterThanOrEqualTo(2);
    }
}

