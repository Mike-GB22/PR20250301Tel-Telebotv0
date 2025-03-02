package org.telebotv0.controller.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class IsMediaGroup implements Command {

    private final Map<String, List<Update>> mapUpdatedWithMediaGroupId = new HashMap<>();
    private final Map<String, LocalDateTime> mapUpdatedWithLastUpdateTime = new HashMap<>();

    @Override
    public boolean isApplicable(Update update) {
        return update.hasMessage() && null != update.getMessage().getMediaGroupId();
    }

    @Override
    public String process(Update update) {
        String mediaGroupId = update.getMessage().getMediaGroupId();

        mapUpdatedWithMediaGroupId.computeIfAbsent(mediaGroupId, k -> new ArrayList<>());
        List<Update> updates = mapUpdatedWithMediaGroupId.get(mediaGroupId);
        updates.add(update);

        mapUpdatedWithLastUpdateTime.put(mediaGroupId, LocalDateTime.now());

        String chatId = update.getMessage().getChatId().toString();
        String messages =  String.format("\n\n We have in mediaGroupId [%s] already [%d] Updates", mediaGroupId, updates.size());
        log.info("\n (r) MediaGroupService: {}", messages);
        return messages;
    }
}
