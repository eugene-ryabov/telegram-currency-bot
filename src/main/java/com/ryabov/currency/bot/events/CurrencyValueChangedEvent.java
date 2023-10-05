package com.ryabov.currency.bot.events;

import org.springframework.context.ApplicationEvent;

public class CurrencyValueChangedEvent extends ApplicationEvent {
    private Long chatId;
    private String currency;
    private Double from;
    private Double to;

    public CurrencyValueChangedEvent(Object source, Long chatId, String currency, Double from, Double to) {
        super(source);
        this.chatId = chatId;
        this.currency = currency;
        this.from = from;
        this.to = to;
    }

    public Long getChatId() {
        return chatId;
    }

    public String getCurrency() {
        return currency;
    }

    public Double getFrom() {
        return from;
    }

    public Double getTo() {
        return to;
    }
}
