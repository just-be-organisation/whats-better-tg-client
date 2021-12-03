package be.better.client.tg.cmd;

import be.better.client.tg.message.EntityType;
import org.springframework.stereotype.Component;

import static be.better.client.tg.cmd.CommandType.*;


@Component
public class CommandManager {


    public String choice(EntityType entity) {
        return toCmd(CallbackCommand.of(CHOICE, entity, 0L));
    }

    public String pick(EntityType entity, String name) {
        return toCmd(CallbackCommand.of(PICK, entity, name));
    }

    public String pick(EntityType entity, long id) {
        return toCmd(CallbackCommand.of(PICK, entity, id));
    }

    public String pickAll(EntityType entity, long id) {
        return toCmd(CallbackCommand.of(PICK_ALL, entity, id));
    }

    public String remove(EntityType entity, String name) {
        return toCmd(CallbackCommand.of(REMOVE, entity, name));
    }

    public String remove(EntityType entity, long id) {
        return toCmd(CallbackCommand.of(REMOVE, entity, id));
    }

    public String removeAll(EntityType entity, long id) {
        return toCmd(CallbackCommand.of(REMOVE_ALL, entity, id));
    }

    public String list(EntityType entity) {
        return list(entity, 1);
    }

    public String list(EntityType entity, int page) {
        return toCmd(CallbackCommand.of(LIST, entity, page));
    }

    public String sublist(EntityType entity, long parent) {
        return sublist(entity, parent, 1);
    }

    public String sublist(EntityType entity, long parent, int page) {
        return toCmd(CallbackCommand.of(CommandType.SUBLIST, entity, page, parent));
    }

    public String generic(CommandType c, EntityType e, int page, Object id) {
        return toCmd(CallbackCommand.of(c, e, page, id));
    }

    public String util(CommandType c) {
        return toCmd(CallbackCommand.of(c));
    }

    public String update(EntityType e) {
        return toCmd(CallbackCommand.of(CommandType.UPDATE, e, 1));
    }

    public String status() {
        return toCmd(CallbackCommand.of(CommandType.STATUS_READER));
    }

    public String goNext() {
        return toCmd(CallbackCommand.of(GO_NEXT));
    }

    public String goPrevious() {
        return toCmd(CallbackCommand.of(GO_PREVIOUS));
    }

    private String toCmd(CallbackCommand cc) {
        return cc.toString();
    }

}
