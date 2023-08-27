package com.example.telegram_analytics;

import com.example.telegram_analytics.entities.AnalyticsDTO;
import com.example.telegram_analytics.entities.ParsedMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AnalyticsCollect {

    private final int maxIgnoreTime;

    private final List<ParsedMessage> parsedMessages;

    private final List<AnalyticsDTO> analyticsDTOs = new ArrayList<>();

    public AnalyticsCollect(List<ParsedMessage> parsedMessages, Integer maxIgnoreTime) {
        this.parsedMessages = parsedMessages;
        this.maxIgnoreTime = maxIgnoreTime * 60 * 60;
    }

    public List<AnalyticsDTO> getAnalytics() {

        int[] lastMessageTime = {parsedMessages.get(0).getUnixTime()};
        String[] lastUserId = {parsedMessages.get(0).getAuthorId()};

        parsedMessages.forEach(message -> {

            AnalyticsDTO analyticsDTO = analyticsDTOs.stream().filter(dto -> Objects.equals(dto.id, message.getAuthorId())).findFirst().orElse(null);

            if (analyticsDTO == null) {
                analyticsDTO = new AnalyticsDTO(message.getAuthorId(), message.getAuthor());
                analyticsDTOs.add(analyticsDTO);
            }

            if (!Objects.equals(lastUserId[0], analyticsDTO.id)) {
                int reactionTime = message.getUnixTime() - lastMessageTime[0];
                if (reactionTime < maxIgnoreTime) {
                    analyticsDTO.reactionTime += reactionTime;
                }
                lastMessageTime[0] = message.getUnixTime();
            }

            analyticsDTO.numberOfMessages += 1;
            lastUserId[0] = analyticsDTO.id;

        });

        analyticsDTOs.forEach(dto -> dto.reactionTime /= dto.numberOfMessages);

        return analyticsDTOs;
    }
}
