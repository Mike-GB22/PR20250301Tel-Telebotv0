package org.telebotv0.controller.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class HasMessageHasSticker implements Command {
    @Override
    public boolean isApplicable(Update update) {
        return update.hasMessage() && update.getMessage().hasSticker();
    }

    @Override
    public String process(Update update) {
        if (!isApplicable(update)) {
            return "Вызов не к месту.";
        }

        return "Update has Message. Message has Sticker";
    }
}
