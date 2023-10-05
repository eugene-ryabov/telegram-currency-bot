package com.ryabov.currency.bot;

import com.ryabov.currency.bot.handler.ResponseHandler;
import com.ryabov.currency.bot.service.UserCurrencyService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.*;

import static org.telegram.abilitybots.api.objects.Locality.USER;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;

@Component
public class CurrencyBot extends AbilityBot {
    private final Configuration.TelegramConfiguration telegramConfiguration;
    private final ResponseHandler responseHandler;
    private final UserCurrencyService userCurrencyService;

    public CurrencyBot(Configuration.TelegramConfiguration telegramConfiguration, UserCurrencyService userCurrencyService) {
        super(telegramConfiguration.getToken(), telegramConfiguration.getName());
        this.telegramConfiguration = telegramConfiguration;
        this.userCurrencyService = userCurrencyService;
        responseHandler = new ResponseHandler(userCurrencyService, silent, db);
    }

    public Ability startBot() {
        return Ability
                .builder()
                .name("start")
                .info("Start the bot")
                .locality(USER)
                .privacy(PUBLIC)
                .action(ctx -> start(ctx.chatId()))
                .build();
    }

    public Ability stopBot() {
        return Ability
                .builder()
                .name("stop")
                .info("Stop the bot")
                .locality(USER)
                .privacy(PUBLIC)
                .action(ctx -> stop(ctx.chatId()))
                .build();
    }

    public Ability restartBot() {
        return Ability
                .builder()
                .name("restart")
                .info("Restart the bot")
                .locality(USER)
                .privacy(PUBLIC)
                .action(ctx -> start(ctx.chatId()))
                .build();
    }

    private void start(long chatId) {
        if (telegramConfiguration.getUserLimit() == responseHandler.getChats().size()) {
            responseHandler.replyText(chatId, "Bot is busy, try again latter");
            return;
        }
        userCurrencyService.saveUserCurrencyState(chatId);
        responseHandler.replyToStart(chatId);
    }

    private void stop(long chatId) {
        userCurrencyService.removeUserState(chatId);
        responseHandler.stopChat(chatId);
    }

    public void sendNotification(Long chatId, String message) {
        responseHandler.replyText(chatId, message);
    }

    @Scheduled(fixedDelayString = "${telegram.delay}")
    public void checkUserState() {
        userCurrencyService.checkUsersState(responseHandler.getChats());
    }

    @Override
    public long creatorId() {
        return 1L;
    }
}
