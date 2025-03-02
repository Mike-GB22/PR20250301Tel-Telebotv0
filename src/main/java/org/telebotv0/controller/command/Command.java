package org.telebotv0.controller.command;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface Command {
    boolean isApplicable(Update update);

    String process(Update update);

}