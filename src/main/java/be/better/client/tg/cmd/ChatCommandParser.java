package be.better.client.tg.cmd;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Arrays;
import java.util.Map;

@Component
public class ChatCommandParser {

    public static ChatCommand parse(String text, long chatId, User from) {
        Map.Entry<String, String> keys = parseKeys(text);
        return new ChatCommand(keys.getKey(), keys.getValue(), chatId, from);
    }

    private static Map.Entry<String, String> parseKeys(String text) {
        switch (text.charAt(0)) {
            case '#':
                return Map.entry("#", text.substring(1));
            case '/':
                String[] keys = text.split("[ _]");
                if (keys.length > 1) {
                    return Map.entry(keys[0], String.join("", Arrays.copyOfRange(keys, 1, keys.length)));
                }
                return Map.entry(keys[0], "");
            default:
            return Map.entry(text, text);
        }
    }
}
