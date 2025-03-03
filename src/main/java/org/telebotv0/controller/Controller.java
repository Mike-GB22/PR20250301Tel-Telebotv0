package org.telebotv0.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/telegram")
@RestController
@RequiredArgsConstructor
public class Controller {

    private final Bot bot;

    @GetMapping("/")
    public String index() {
        return "Hi!!!";
    }

    @GetMapping("/info")
    public String getInfo(){
        StringBuilder html = new StringBuilder();
        html.append(String.format("Clients: %s%n", bot.getClientIds()));

        return html.toString();
    }
}
