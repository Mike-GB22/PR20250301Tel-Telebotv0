package org.telebotv0.config;

import lombok.Getter;
import lombok.Setter;
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

    private Bot bot;

    @Value("${secrets.telegram.name:NOT_FOUND}")
    private String botName;

    @Value("${secrets.telegram.ownerId:NOT_FOUND}")
    private String ownerId;

    @Value("${secrets.telegram.token:NOT_FOUND}")
    private String token;

    @Value("${config.telegram.send_duplicate_to_owner:false}")
    private boolean sendDuplicateToOwner;

    @Value("${config.telegram.file_download_pattern:'http://localhost:8080/download/%s'}")
    private String fileDownloadPattern;

    @Bean
    public TelegramBotsApi telegramBotsApi(Bot bot) throws TelegramApiException {
        TelegramBotsApi tba = new TelegramBotsApi(DefaultBotSession.class);
        tba.registerBot(bot);
        this.bot = bot;
        return tba;
    }
}
