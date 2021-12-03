package be.better.client.tg.message.handler;


import be.better.client.tg.message.EntityType;

public interface EntityCallbackMessageHandler extends CallbackMessageHandler {

    EntityType entityType();
}
