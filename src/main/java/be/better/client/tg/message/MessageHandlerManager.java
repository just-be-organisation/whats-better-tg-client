package be.better.client.tg.message;

import bepicky.bot.core.cmd.ChatCommand;
import bepicky.bot.core.cmd.ChatCommandParser;
import bepicky.bot.core.cmd.CommandTranslator;
import bepicky.bot.core.message.handler.IDefaultMessageHandler;
import bepicky.bot.core.message.handler.MessageHandler;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Slf4j
public class MessageHandlerManager {

    private final Map<String, MessageHandler> commonMessageHandlers;

    private final IDefaultMessageHandler defaultMessageHandler;

    private final CommandTranslator txt2Cmd;

    public MessageHandlerManager(
        List<MessageHandler> commonMessageHandlers,
        IDefaultMessageHandler defaultMessageHandler,
        CommandTranslator txt2Cmd
    ) {
        this.commonMessageHandlers = commonMessageHandlers.stream()
            .collect(ImmutableMap.toImmutableMap(MessageHandler::trigger, Function.identity()));
        this.defaultMessageHandler = defaultMessageHandler;
        this.txt2Cmd = txt2Cmd;
    }

    public BotApiMethod<Message> manage(Message message) {
        ChatCommand cc = ChatCommandParser.parse(message.getText(), message.getChatId(), message.getFrom());
        return getCommonHandler(cc.getTrigger()).handle(cc);
    }

    private MessageHandler getCommonHandler(String text) {
        MessageHandler commonMessageHandler = commonMessageHandlers.get(text);
        if (commonMessageHandler != null) {
            return commonMessageHandler;
        }
        String cmd = txt2Cmd.translate(text);
        return cmd != null ? commonMessageHandlers.get(cmd) : defaultMessageHandler;
    }

}
