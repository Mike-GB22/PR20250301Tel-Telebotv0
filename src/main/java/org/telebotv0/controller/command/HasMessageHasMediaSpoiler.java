package org.telebotv0.controller.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
public class HasMessageHasMediaSpoiler implements Command {
    @Override
    public boolean isApplicable(Update update) {
        if (update.hasMessage()) {
            try {
                return update.getMessage().getHasMediaSpoiler();
            } catch (Exception e) {
                log.warn("update.getMessage().getHasMediaSpoiler() is null");
            }
        }
        return false;

    }

    @Override
    public String process(Update update) {
        if (!isApplicable(update)) {
            return "Вызов не к месту.";
        }

        return "Update has Message. Message has MediaSpoiler";
    }
}
