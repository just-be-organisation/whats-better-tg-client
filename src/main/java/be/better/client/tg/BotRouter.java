package be.better.client.tg;

import be.better.client.tg.message.CallbackMessageHandlerManager;
import be.better.client.tg.message.MessageHandlerManager;
import be.better.client.tg.message.builder.SendMessageBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;

@Slf4j
public class BotRouter extends TelegramLongPollingBot {

    @Value("${bot.telegram.name}")
    private String name;

    @Value("${bot.telegram.token}")
    private String token;

    @Autowired
    private MessageHandlerManager msgManager;

    @Autowired
    private CallbackMessageHandlerManager callbackManager;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            try {
                BotApiMethod<Message> msg = msgManager.manage(update.getMessage());
                if (msg != null) {
                    execute(msg);
                }
            } catch (TelegramApiException e) {
                log.error(e.getMessage(), e);
                //TODO: send to admin
                SendMessage m = SendMessage.builder()
                    .chatId(String.valueOf(update.getMessage().getChatId()))
                    .text(e.getMessage())
                    .build();
                try {
                    execute(m);
                } catch (TelegramApiException ex) {
                    log.error(ex.getMessage(), ex);
                }
            }
        } else if (update.hasCallbackQuery()){
            CallbackQuery callbackQuery = update.getCallbackQuery();
            BotApiMethod<Serializable> m = callbackManager.manageCallback(
                callbackQuery.getMessage(),
                callbackQuery.getData()
            );
            if (m == null) {
                return;
            }
            try {
                execute(m);
            } catch (TelegramApiException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    public void sendNew(Long chatId, String text) {
        try {
            execute(new SendMessageBuilder(chatId, text).build());
        } catch (TelegramApiException e) {
            log.error("bot:message:failed:{}:{}", chatId, text);
        }
    }

    public void sendNew(Long chatId, String text, ReplyKeyboardMarkup keyboard) {
        try {
            execute(new SendMessageBuilder(chatId, text).replyMarkup(keyboard).build());
        } catch (TelegramApiException e) {
            log.error("bot:message:failed:{}:{}", chatId, text);
        }
    }

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
