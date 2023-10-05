package com.ryabov.currency.bot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CurrencyDTO(String symbol, Double price) {}
