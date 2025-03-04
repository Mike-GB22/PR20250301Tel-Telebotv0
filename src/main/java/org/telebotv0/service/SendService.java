package org.telebotv0.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telebotv0.config.TelegramConfig;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendService {

    private final TelegramConfig telegramConfig;

    public void sendAnswerAndDuplicateToOwner(Update update, String answer) {
        if (answer.isBlank()) {
            log.info("\n(i) -> Answer will NOT send, because the message is empty. {}\n", answer);
            return;
        }
        log.info("\n(i) -> Answer will send: {}\n", answer);

        sendAnswer(telegramConfig.getOwnerId(), answer);
        if (telegramConfig.isSendDuplicateToOwner()) {
            sendAnswer(update.getMessage().getChatId().toString(), answer);
        }
    }

    private void sendAnswer(String chatId, String answer) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setText(answer);
        sendMessage.setChatId(chatId);
        try {
            telegramConfig.getBot().execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("SendAnswer -> SendMessage -> Bot.execute(): {},\nStack: {}", e.getMessage(), e.getStackTrace());
        }
    }
}
