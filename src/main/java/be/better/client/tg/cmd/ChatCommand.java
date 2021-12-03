package be.better.client.tg.cmd;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.User;

@AllArgsConstructor
@Getter
public class ChatCommand {

    private final String trigger;
    private final String key;
    private final Long chatId;
    private final User from;

}
