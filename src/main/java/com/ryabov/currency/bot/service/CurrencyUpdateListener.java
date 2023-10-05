package com.ryabov.currency.bot.service;

import com.ryabov.currency.bot.CurrencyBot;
import com.ryabov.currency.bot.events.CurrencyValueChangedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;

@Component
public class CurrencyUpdateListener implements ApplicationListener<CurrencyValueChangedEvent> {
    private final CurrencyBot currencyBot;
    private final DecimalFormat df;

    public CurrencyUpdateListener(CurrencyBot currencyBot) {
        this.currencyBot = currencyBot;

        df = new DecimalFormat("#");
        df.setMaximumFractionDigits(12);

    }

    @Override
    public void onApplicationEvent(CurrencyValueChangedEvent event) {
        currencyBot.sendNotification(event.getChatId(), String.format(
                "Currency '%s' changed from %s to %s",
                event.getCurrency(),
                df.format(event.getFrom()),
                df.format(event.getTo())
        ));
    }
}
