package com.example.CurrencyConverter.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@EqualsAndHashCode
@Table(name = "currency_convertor")
public class CurrencyConvertor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long timestamp;

    private String currency;

    @Column(columnDefinition = "TEXT")
    private String rates;

}

