package org.telebotv0.controller;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telebotv0.config.TelegramConfig;
import org.telebotv0.controller.command.Command;
import org.telebotv0.service.SendService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class Bot extends TelegramLongPollingBot {

    private final TelegramConfig telegramConfig;
    private final SendService sendService;
    private final List<Command> commands;

    @Getter
    private final Set<Long> clientIds = new HashSet<>();

    public Bot(TelegramConfig telegramConfig, List<Command> commands, SendService sendService) {
        super(telegramConfig.getToken());
        this.telegramConfig = telegramConfig;
        this.sendService = sendService;
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

        answerOpt.ifPresent(answer -> sendService.sendAnswerAndDuplicateToOwner(update, answer));

        registerClientId(update);
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

    private void registerClientId(Update update) {
        Optional<Long> clientId = Optional.of(update.getMessage().getChatId());
        clientId.ifPresent(clientIds::add);
    }
}
