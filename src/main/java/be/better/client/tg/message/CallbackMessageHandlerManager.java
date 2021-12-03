package be.better.client.tg.message;

import be.better.client.tg.cmd.CallbackCommand;
import be.better.client.tg.cmd.CommandType;
import be.better.client.tg.message.handler.CallbackMessageHandler;
import be.better.client.tg.message.handler.EntityCallbackMessageHandler;
import be.better.client.tg.message.handler.UtilMessageHandler;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class CallbackMessageHandlerManager {

    private final Map<CommandType, Function<EntityType, CallbackMessageHandler>> functionContainer;

    private final Map<CommandType, UtilMessageHandler> utilHandlers;

    public CallbackMessageHandlerManager(
        List<EntityCallbackMessageHandler> entityMessageHandlers,
        List<UtilMessageHandler> utilMessageHandler
    ) {
        ImmutableMap.Builder<CommandType, Function<EntityType, CallbackMessageHandler>> functionBuilder =
            ImmutableMap.builder();
        entityMessageHandlers.stream()
            .collect(Collectors.groupingBy(
                EntityCallbackMessageHandler::commandType,
                Collectors.toMap(EntityCallbackMessageHandler::entityType, Function.identity())
            )).forEach((type, handler) -> functionBuilder.put(type, handler::get));
        this.functionContainer = functionBuilder.build();

        this.utilHandlers = utilMessageHandler.stream()
            .collect(Collectors.toMap(UtilMessageHandler::commandType, Function.identity()));
    }

    public BotApiMethod<Serializable> manageCallback(Message message, String data) {
        StringBuilder txt = new StringBuilder();
        CallbackMessageHandler.HandleResult handleResult = null;
        for (String cc : data.split(";")) {
            handleResult = handleCallback(message.getChatId(), cc);
            if (handleResult != null && handleResult.getText() != null) {
                txt.append("\n").append(handleResult.getText());
            }
        }
        if (handleResult == null) {
            return null;
        }
        EditMessageText msg = EditMessageText.builder()
            .chatId(String.valueOf(message.getChatId()))
            .messageId(message.getMessageId())
            .text(txt.toString())
            .disableWebPagePreview(!handleResult.isPreviewPage())
            .replyMarkup(handleResult.getInline())
            .build();
        msg.enableHtml(true);
        return msg;
    }

    private CallbackMessageHandler.HandleResult handleCallback(long chatId, String data) {
        CallbackCommand cc = CallbackCommand.fromText(data);
        cc.setChatId(chatId);
        if (CommandType.UTIL.contains(cc.getCommandType())) {
            return utilHandlers.get(cc.getCommandType()).handle(cc);
        }
        return functionContainer.get(cc.getCommandType()).apply(cc.getEntityType()).handle(cc);
    }

}
