package org.telebotv0.controller.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telebotv0.controller.Bot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Voice;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class HasMessageHasVoice implements Command {

    private static final String CAPTION = "*Update has Message. Message has Voice*";
    private Bot bot;

    @Override
    public boolean isApplicable(Update update) {
        return update.hasMessage() && update.getMessage().hasVoice();
    }

    @Override
    public String process(Update update) {
        if (!isApplicable(update)) {
            return "Вызов не к месту.";
        }

        StringBuilder info = new StringBuilder(CAPTION);
        Voice recievedVoice = update.getMessage().getVoice();

        System.out.println("recievedVoice: " + recievedVoice);
        String fileId = recievedVoice.getFileId();
        System.out.println("fileId: " + fileId);
        GetFile getVoiceFileRequest = new GetFile();
        getVoiceFileRequest.setFileId(fileId);

        java.io.File audioFile;
        try {
            File voiceTeleFile = bot.execute(getVoiceFileRequest);
            System.out.println("File: " + voiceTeleFile);
            //System.out.println("FileURL: " + voiceTeleFile.getFileUrl(token));
            System.out.println("FilePath: " + voiceTeleFile.getFilePath());

            audioFile = bot.downloadFile(voiceTeleFile.getFilePath());
            System.out.println("audioFile: " + audioFile);
            System.out.println("audioFile.getAbsolutePath(): " + audioFile.getAbsolutePath());
            System.out.println("audioFile.getAbsoluteFile(): " + audioFile.getAbsoluteFile());
            System.out.println("audioFile.toURI(): " + audioFile.toURI());
            System.out.println("audioFile.getCanonicalPath(): " + audioFile.getCanonicalPath());
            System.out.println("audioFile.getName(): " + audioFile.getName());

        } catch (TelegramApiException e) {
            log.error("Voice. Get and Download file. \n{}\nStack: {}", e.getMessage(), e.getStackTrace());
        } catch (IOException e) {
            log.error("Voice. IOException. \n{}\nStack: {}", e.getMessage(), e.getStackTrace());
        }

        return info.toString();
    }
}
