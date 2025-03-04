package org.telebotv0.controller.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telebotv0.config.TelegramConfig;
import org.telebotv0.controller.Bot;
import org.telebotv0.service.DownloadService;
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

    private static final String CAPTION = "Update has Message. Message has *Voice*";
    private final TelegramConfig telegramConfig;
    private final DownloadService downloadService;


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
        String fileId = recievedVoice.getFileId();
        GetFile getVoiceFileRequest = new GetFile();
        getVoiceFileRequest.setFileId(fileId);

        info.append("\n = receivedVoice: `").append(recievedVoice).append("`")
                .append("\n = fileId: `").append(fileId).append("`");

        java.io.File audioFile;
        try {
            File voiceTeleFile = telegramConfig.getBot().execute(getVoiceFileRequest);
            String filePath = voiceTeleFile.getFilePath();

            info.append("\n ■ org.telegram.telegrambots.meta.api.objects.File: `").append(voiceTeleFile).append("`")
                    //.append("\n = FileURL: `").append(voiceTeleFile.getFileUrl(token)).append("`")
                    .append("\n ● FilePath: `").append(filePath).append("`")
                    .append("\n ● Download file: `").append(downloadService.getDownloadUrlText(filePath)).append("`");

            audioFile =  telegramConfig.getBot().downloadFile(voiceTeleFile.getFilePath());
            info.append("\n\n ■ java.io.File:")
                    .append("\n ● audioFile: `").append(audioFile).append("`")
                    .append("\n ● audioFile.getAbsolutePath(): `").append(audioFile.getAbsolutePath()).append("`")
                    .append("\n ● audioFile.getAbsoluteFile(): `").append(audioFile.getAbsoluteFile()).append("`")
                    .append("\n ● audioFile.toURI(): `").append(audioFile.toURI()).append("`")
                    .append("\n ● audioFile.getCanonicalPath(): `").append(audioFile.getCanonicalPath()).append("`")
                    .append("\n ● audioFile.getName(): `").append(audioFile.getName()).append("`");
        } catch (TelegramApiException e) {
            log.error("Voice. Get and Download file. \n{}\nStack: {}", e.getMessage(), e.getStackTrace());
        } catch (IOException e) {
            log.error("Voice. IOException. \n{}\nStack: {}", e.getMessage(), e.getStackTrace());
        }

        return info.toString();
    }
}
