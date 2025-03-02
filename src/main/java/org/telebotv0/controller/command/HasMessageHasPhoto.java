package org.telebotv0.controller.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class HasMessageHasPhoto implements Command {
    private static final String CAPTION = "*Update has Message. Message has Photo.*";

    @Override
    public boolean isApplicable(Update update) {
        return update.hasMessage() && update.getMessage().hasPhoto();
    }

    @Override
    public String process(Update update) {
        if (!isApplicable(update)) {
            return "Вызов не к месту.";
        }

        Message msg = update.getMessage();
        List<PhotoSize> photoSizes = msg.getPhoto();
        StringBuilder info = new StringBuilder(CAPTION)
                .append("\n - photoSize.size(): ").append(photoSizes.size())
                ;
        boolean flagAllFileIdIsTheSame = true;
        String previousFileId = null;
        for (int i = 0; i < photoSizes.size(); i++) {
            PhotoSize photo = photoSizes.get(i);

            String fileId = photo.getFileId();
            if (previousFileId != null && !previousFileId.equals(fileId)) {
                flagAllFileIdIsTheSame = false;
            } else {
                previousFileId = fileId;
            }

            info.append(String.format("\n # photoSize.get(%d): ", i))
                    .append("\nfileId: `").append(fileId).append("`")
                    .append("\nfileUniqueId: ").append(photo.getFileUniqueId())
                    .append("\nwidth x height: ").append(photo.getWidth()).append(" x ").append(photo.getHeight())
                    .append("\nfileSize: ").append(photo.getFileSize())
                    .append("\nfilePath: ").append(photo.getFilePath())
                    .append("\n");;
        }

        info.append(" = All photoSize has the same fileId: ").append(flagAllFileIdIsTheSame);
        if (flagAllFileIdIsTheSame) {
            info.append(previousFileId);
        }
        return info.toString();
    }
}
