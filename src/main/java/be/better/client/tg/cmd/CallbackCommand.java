package be.better.client.tg.cmd;

import be.better.client.tg.message.EntityType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CallbackCommand {

    public static final String DELIMITER = ":";

    private CommandType commandType;
    private EntityType entityType;
    private Integer page;
    private Object id;
    private long chatId;
    private Integer messageId;

    public static CallbackCommand of(CommandType type) {
        CallbackCommand c = new CallbackCommand();
        c.setCommandType(type);
        return c;
    }

    public static CallbackCommand of(CommandType ct, EntityType e, Integer page) {
        CallbackCommand c = of(ct);
        c.setEntityType(e);
        c.setPage(page);
        return c;
    }

    public static CallbackCommand of(CommandType ct, EntityType e, Object id) {
        CallbackCommand c = of(ct);
        c.setEntityType(e);
        c.setId(id);
        return c;
    }

    public static CallbackCommand of(CommandType ct, EntityType e, Integer page, Object id) {
        CallbackCommand c = of(ct, e, page);
        c.setId(id);
        return c;
    }

    public Object getId() {
        if (id instanceof String) {
            try {
                return Long.parseLong((String) id);
            } catch (NumberFormatException e) {
                return id;
            }
        }
        return id;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(commandType.getKey());
        if (entityType != null) {
            b.append(DELIMITER).append(entityType.getKey());
            b.append(DELIMITER).append(page == null ? 0 : page);
            b.append(DELIMITER).append(id == null ? 0 : id);
        }
        return b.toString();
    }

    public static CallbackCommand fromText(String cc) {
        String[] parts = cc.split(DELIMITER);
        CommandType ct = CommandType.of(parts[0]);
        if (parts.length > 1) {
            EntityType et = EntityType.of(parts[1]);
            int page = Integer.parseInt(parts[2]);
            Object id = parts[3];
            return of(ct, et, page, id);
        }
        return of(ct);
    }
}
