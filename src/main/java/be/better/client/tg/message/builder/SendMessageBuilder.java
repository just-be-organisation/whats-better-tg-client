package be.better.client.tg.message.builder;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

public class SendMessageBuilder {

    private SendMessage msg;

    public SendMessageBuilder(Long chatId, String text) {
        this.msg = SendMessage.builder()
            .chatId(String.valueOf(chatId))
            .text(text)
            .build();
    }

    public SendMessageBuilder replyMarkup(ReplyKeyboard keyboard) {
        this.msg.setReplyMarkup(keyboard);
        return this;
    }

    public SendMessageBuilder enableHtml() {
        this.msg.enableHtml(true);
        return this;
    }

    public SendMessageBuilder enableMarkdown() {
        this.msg.enableMarkdownV2(true);
        return this;
    }

    public SendMessageBuilder disableWebPreview() {
        this.msg.disableWebPagePreview();
        return this;
    }

    public SendMessageBuilder disableNotification() {
        this.msg.disableNotification();
        return this;
    }

    public SendMessage build() {
        return msg;
    }
}
