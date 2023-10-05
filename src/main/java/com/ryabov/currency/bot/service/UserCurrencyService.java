package com.ryabov.currency.bot.service;

import com.ryabov.currency.bot.dto.CurrencyDTO;
import com.ryabov.currency.bot.entity.UserCurrency;
import com.ryabov.currency.bot.events.CurrencyValueChangedEvent;
import com.ryabov.currency.bot.repository.UserCurrencyRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

@Service
public class UserCurrencyService {
    private final CurrencyService currencyService;
    private final UserCurrencyRepository userCurrencyRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public UserCurrencyService(CurrencyService currencyService, UserCurrencyRepository userCurrencyRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.currencyService = currencyService;
        this.userCurrencyRepository = userCurrencyRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void saveUserCurrencyState(Long chatId) {
        var currentState = getUserState(chatId);

        if (!currentState.isEmpty()) {
            userCurrencyRepository.deleteAll(currentState);
        }

        var now = LocalDateTime.now();
        var values = currencyService.requestCurrencies()
                .stream()
                .map(it -> createUserCurrency(chatId, it.symbol(), it.price(), now))
                .toList();

        userCurrencyRepository.saveAll(values);
    }

    public List<UserCurrency> getUserState(Long chatId) {
        return userCurrencyRepository.findAllByUserId(chatId);
    }

    public void checkUsersState(Set<Long> chats) {
        var currency = currencyService.requestCurrencies()
                .stream()
                .collect(Collectors.toMap(CurrencyDTO::symbol, CurrencyDTO::price));

        chats.forEach(chat -> checkUserState(chat, currency));
    }

    public void checkUserState(Long chatId, Map<String, Double> currencies) {
        getUserState(chatId)
                .forEach(it -> checkCurrencyState(it, currencies.get(it.getName())));
    }

    private void checkCurrencyState(UserCurrency userCurrency, Double currentPrice) {
        if (getPercentage(userCurrency.getValue(), currentPrice) > 5) {
            applicationEventPublisher.publishEvent(new CurrencyValueChangedEvent(
                    this,
                    userCurrency.getUserId(),
                    userCurrency.getName(),
                    userCurrency.getValue(),
                    currentPrice
            ));
        }
    }

    private Double getPercentage(Double a, Double b) {
        return (abs(a - b) * 100) / a;
    }

    private UserCurrency createUserCurrency(
            Long chatId,
            String currencyName,
            Double currencyPrice,
            LocalDateTime localDateTime
    ) {
        var entity = new UserCurrency();
        entity.setUserId(chatId);
        entity.setName(currencyName);
        entity.setValue(currencyPrice);
        entity.setDateTime(Timestamp.valueOf(localDateTime));
        return entity;
    }

    public void removeUserState(long chatId) {
        var currentState = getUserState(chatId);
        userCurrencyRepository.deleteAll(currentState);
    }
}
