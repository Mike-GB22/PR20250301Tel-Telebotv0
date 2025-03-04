package org.telebotv0.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telebotv0.config.TelegramConfig;

@Slf4j
@Service
@RequiredArgsConstructor
public class DownloadService {

    private final TelegramConfig telegramConfig;

    public String getDownloadUrl(String filePath) {
        if (null == filePath) {
            log.warn("getDownloadUrl(String filePath), filePath is null");
            return "filePath is null";
        }

        String tailUrl = String.format("%s?file=%s", getFileNameToSave(filePath), filePath);

        String downloadPattern = telegramConfig.getFileDownloadPattern();
        if (!downloadPattern.contains("%s")) {
            return tailUrl;
        }
        return String.format(downloadPattern, tailUrl);
    }

    public String getDownloadUrlText(String filePath) {
        String url = getDownloadUrl(filePath);
        String fieNameToSave = getFileNameToSave(filePath);
        //String pattern = "<a href = '%s'>%s</a>";
        String pattern = "%s";

        return String.format(pattern, url, fieNameToSave);
    }

    private String getFileNameToSave(String filePath) {
        //String[] pathElements = filePath.split("/");
        //return pathElements[pathElements.length - 1];

        return filePath.replace('/', '_');
    }
}
