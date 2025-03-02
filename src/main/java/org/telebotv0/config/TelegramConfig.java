package org.telebotv0.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telebotv0.controller.Bot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Getter
@Configuration
public class TelegramConfig {

    @Value("${secrets.telegram.name:NOT_FOUND}")
    private String botName;

    @Value("${secrets.telegram.ownerId:NOT_FOUND}")
    private String ownerId;

    @Value("${secrets.telegram.token:NOT_FOUND}")
    private String token;

    @Value("${config.telegram.send_duplicate_to_owner:false}")
    private boolean sendDuplicateToOwner;

    @Bean
    public TelegramBotsApi telegramBotsApi(Bot bot) throws TelegramApiException {
        TelegramBotsApi tba = new TelegramBotsApi(DefaultBotSession.class);
        tba.registerBot(bot);
        return tba;
    }
}
