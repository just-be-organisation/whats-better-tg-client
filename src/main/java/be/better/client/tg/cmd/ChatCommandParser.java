package be.better.client.tg.cmd;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.User;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.Arrays;

@Component
public class ChatCommandParser {

    public static ChatCommand parse(String text, long chatId, User from) {
        Tuple2<String, String> keys = parseKeys(text);
        return new ChatCommand(keys.getT1(), keys.getT2(), chatId, from);
    }

    private static Tuple2<String, String> parseKeys(String text) {
        switch (text.charAt(0)) {
            case '#':
                return Tuples.of("#", text.substring(1));
            case '/':
                String[] keys = text.split("[ _]");
                if (keys.length > 1) {
                    return Tuples.of(keys[0], String.join("", Arrays.copyOfRange(keys, 1, keys.length)));
                }
                return Tuples.of(keys[0], "");
            default:
            return Tuples.of(text, text);
        }
    }
}
