package org.telebotv0.controller.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class HasMessageHasEntities implements Command {

    private static final String CAPTION = "Update has Message. Message has *Entities*\n";

    @Override
    public boolean isApplicable(Update update) {
        return update.hasMessage() && update.getMessage().hasEntities();
    }

    @Override
    public String process(Update update) {
        if (!isApplicable(update)) {
            return "Вызов не к месту.";
        }

        List<MessageEntity> entities = update.getMessage().getEntities();

        StringBuilder info = new StringBuilder(CAPTION);
        info.append(" - List of MessageEntity:\n").append(entities);
        return info.toString();
    }
}
