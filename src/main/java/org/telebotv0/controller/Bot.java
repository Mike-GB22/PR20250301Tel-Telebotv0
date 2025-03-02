package org.telebotv0.controller;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telebotv0.config.TelegramConfig;
import org.telebotv0.controller.command.Command;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class Bot extends TelegramLongPollingBot {

    private final TelegramConfig telegramConfig;
    private final List<Command> commands;

    public Bot(TelegramConfig telegramConfig, List<Command> commands) {
        super(telegramConfig.getToken());
        this.telegramConfig = telegramConfig;
        this.commands = commands;
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.info("\n(i) -> Update was received: {}\n", update);

        Optional<String> answerOpt = Optional.of(
                commands.stream()
                .filter(c -> c.isApplicable(update))
                .map(c -> c.process(update))
                .collect(Collectors.joining("\n")));

        answerOpt.ifPresent(answer -> sendAnswerAndDuplicateToOwner(update, answer));
    }

    @Override
    public String getBotUsername() {
        return telegramConfig.getBotName();
    }

    public String getOwnerId() {
        return telegramConfig.getOwnerId();
    }

    @PostConstruct
    public void printInfo() {
        log.info("\n(i) -> Telegram bot name is: {},\n(i) -> Owner ID is: {}\n", getBotUsername(), getOwnerId());
    }

    private void sendAnswerAndDuplicateToOwner(Update update, String answer) {
        if (answer.isBlank()) {
            log.info("\n(i) -> Answer will NOT send, because the message is empty. {}\n", answer);
            return;
        }
        log.info("\n(i) -> Answer will send: {}\n", answer);

        sendAnswer(getOwnerId(), answer);
        sendAnswer(update.getMessage().getChatId().toString(), answer);
    }

    private void sendAnswer(String chatId, String answer) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setText(answer);
        sendMessage.setChatId(chatId);
        try {
            this.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("SendAnswer -> SendMessage -> Bot.execute(): {},\nStack: {}", e.getMessage(), e.getStackTrace());
        }
    }
}
