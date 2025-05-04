package com.example.CurrencyConverter.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@EqualsAndHashCode
@Table(name = "currency_convertor")
@Schema(description = "Entity representing stored exchange rate data for a specific currency at a specific time")
public class CurrencyConvertor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier for the currency conversion record", example = "1")
    private Long id;

    @Schema(description = "Timestamp when the exchange rates were fetched", example = "1714828800")
    private Long timestamp;

    @Schema(description = "The base currency code", example = "USD")
    private String currency;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "Serialized JSON string representing exchange rates", example = "{\"EGP\": 30.90, \"EUR\": 0.91}")
    private String rates;

}

