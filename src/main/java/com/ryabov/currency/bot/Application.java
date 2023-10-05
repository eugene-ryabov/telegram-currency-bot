package com.ryabov.currency.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		var ctx = SpringApplication.run(Application.class, args);
		try {
			TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
			botsApi.registerBot(ctx.getBean("currencyBot", AbilityBot.class));
		} catch (TelegramApiException e) {
			throw new RuntimeException(e);
		}
	}

}
