package org.telebotv0.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@RestController
@RequestMapping("/telegram")
@RequiredArgsConstructor
public class DownloadController {

    private final Bot bot;

    @GetMapping(path = {"/download","/download/{fileName}"}, produces = "application/octet-stream")
    public ResponseEntity<byte[]> downloadFile(@RequestParam("file") String filePath) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("application/octet-stream"));

        try (InputStream fileAsStream = bot.downloadFileAsStream(filePath)) {
            byte[] bytes = fileAsStream.readAllBytes();

            return new ResponseEntity<>(
                    bytes
                    , headers
                    , HttpStatus.OK);
        } catch (TelegramApiException | IOException e) {
            log.error("File download as Stream error: {},\nStack: {}", e.getMessage(), e.getStackTrace());
            if (e instanceof TelegramApiException) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }
}
