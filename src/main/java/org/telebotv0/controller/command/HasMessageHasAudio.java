package org.telebotv0.controller.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class HasMessageHasAudio implements Command {
    @Override
    public boolean isApplicable(Update update) {
        return update.hasMessage() && update.getMessage().hasAudio();
    }

    @Override
    public String process(Update update) {
        if (!isApplicable(update)) {
            return "Вызов не к месту.";
        }

        return "Update has Message. Message has Audio";
    }
}
