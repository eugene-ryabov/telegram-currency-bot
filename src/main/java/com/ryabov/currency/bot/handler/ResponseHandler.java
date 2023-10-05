package com.ryabov.currency.bot.handler;

import com.ryabov.currency.bot.Constants;
import com.ryabov.currency.bot.service.UserCurrencyService;
import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;
import java.util.Set;

public class ResponseHandler {
    private final SilentSender sender;
    private final Set<Long> chats;

    public ResponseHandler(UserCurrencyService userCurrencyService,
                           SilentSender sender,
                           DBContext db) {
        this.sender = sender;
        chats = db.getSet("HashSet");
    }

    public Set<Long> getChats() {
        return chats;
    }

    public void replyToStart(Long chatId) {
        sender.execute(createMsg(chatId, Constants.START_TEXT));
        chats.add(chatId);
    }

    public void stopChat(long chatId) {
        chats.remove(chatId);

        sender.execute(createMsg(chatId, "Thank you!"));
    }

    public void replyText(long chatId, String message) {
        sender.execute(createMsg(chatId, message));
    }

    private SendMessage createMsg(Long chatId, String value) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(value);
        return sendMessage;
    }
}
