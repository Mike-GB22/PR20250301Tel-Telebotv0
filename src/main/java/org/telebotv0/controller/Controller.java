package org.telebotv0.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.telebotv0.service.SendService;

@RestController
@RequiredArgsConstructor
public class Controller {

    private final Bot bot;
    private final SendService sendService;

    @GetMapping("/")
    public ResponseEntity<Void> index() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/telegram");
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping({"/telegram", "/telegram/"})
    public String indexTelegram() {
        return "<a href=/telegram/info>info</a><br>" +
                "<a href=/telegram/message>send message</a><br>";
    }

    @GetMapping("/telegram/info")
    public String getInfo(){
        String clientEntityPattern = "id: <a href=/telegram/message/%s>%s</a><br>%n";
        StringBuilder html = new StringBuilder();
        for (Long clientId : bot.getClientIds()) {
            html.append(String.format(clientEntityPattern, clientId, clientId));
        }
        html.append("<br><a href=../>index</a><br>");
    return html.toString();
    }


    @GetMapping("/telegram/message")
    public String messageForm() {
        return messageFormWithId(0L);
    }

    @GetMapping("/telegram/message/{clientId}")
    public String messageFormWithId(@PathVariable(required = false) Long clientId){
        StringBuilder html = new StringBuilder();
        html.append(String.format("<form action='/telegram/message' method=POST>%n" +
                "<input type=text name='client_id' value='%s'><br>%n" +
                "<input type=text name='message' value=''><br>%n" +
                "<input type=submit value='send'></form>%n"
                , clientId));
        html.append("<br><a href=../>index</a><br>");
        return html.toString();
    }

    @PostMapping("/telegram/message")
    public String messageSend(@RequestParam("client_id") String clientId, @RequestParam String message){
        String html = String.format("Message was sent on id: %s<br>%n%s<br>%n" +
                "<meta http-equiv='refresh' content='5,/telegram/message/%s'>"
                , clientId, message, clientId);

        sendService.sendAnswerAndDuplicateToOwner(clientId, message);
        return html;
    }

}
