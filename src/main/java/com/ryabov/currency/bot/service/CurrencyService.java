package com.ryabov.currency.bot.service;

import com.ryabov.currency.bot.dto.CurrencyDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class CurrencyService {
    private RestTemplate restTemplate;

    public CurrencyService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<CurrencyDTO> requestCurrencies() {
        ResponseEntity<List<CurrencyDTO>> response =
                restTemplate.exchange(
                        "https://api.mexc.com/api/v3/ticker/price",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {}
                );

        return response.getBody();
    }
}
