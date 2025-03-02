package org.telebotv0.controller.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class HasMessage implements Command {
    private static final String CAPTION = "*Update has Message.*";

    @Override
    public boolean isApplicable(Update update) {
        return update.hasMessage();
    }

    @Override
    public String process(Update update) {
        if (!isApplicable(update)) {
            return "Вызов не к месту.";
        }

        Message msg = update.getMessage();
        StringBuilder info = new StringBuilder(CAPTION)
                .append("\n - msg.getChatId(): ").append(msg.getChatId())
                .append("\n - msg.getMessageId(): ").append(msg.getMessageId())
                .append("\n - msg.getMessageThreadId(): ").append(msg.getMessageThreadId())
                .append("\n - msg.getMediaGroupId(): ").append(msg.getMediaGroupId())
                .append("\n - msg.getForwardFromMessageId(): ").append(msg.getForwardFromMessageId())
                .append("\n - msg.getMigrateFromChatId(): ").append(msg.getMigrateFromChatId())
                .append("\n - msg.getMigrateToChatId(): ").append(msg.getMigrateToChatId())
                .append("\n");

        return info.toString();
    }
}
