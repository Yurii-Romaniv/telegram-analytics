package com.example.telegram_analytics;

import com.example.telegram_analytics.entities.AnalyticsDTO;
import com.example.telegram_analytics.entities.ParsedMessage;
import com.example.telegram_analytics.entities.TimePointData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

public class AnalyticsCollect {

    final static int TIME_POINTS_NUMBER = 16;

    private final int maxIgnoreTime;

    private final List<ParsedMessage> parsedMessages;

    private final List<AnalyticsDTO> analyticsDTOs = new ArrayList<>();

    public AnalyticsCollect(List<ParsedMessage> parsedMessages, Integer maxIgnoreTime) {
        this.parsedMessages = parsedMessages;
        this.maxIgnoreTime = maxIgnoreTime * 60 * 60;
    }

    private Stack<Integer> getTimePoints() {
        int firstMessageTime = parsedMessages.get(0).getUnixTime();
        int lastMessageTime = parsedMessages.get(parsedMessages.size() - 1).getUnixTime();
        int timePeriod = lastMessageTime - firstMessageTime;
        Stack<Integer> timePoints = new Stack<>();

        for (int i = TIME_POINTS_NUMBER; i >= 0; i--) {
            timePoints.push(firstMessageTime + timePeriod * i / TIME_POINTS_NUMBER);
        }

        return timePoints;
    }

    public List<AnalyticsDTO> getAnalytics() {

        int[] lastMessageTime = {parsedMessages.get(0).getUnixTime()};
        String[] lastUserId = {parsedMessages.get(0).getAuthorId()};
        Stack<Integer> timePoints = getTimePoints();
        int firstPoint = timePoints.pop();

        parsedMessages.forEach(message -> {

            AnalyticsDTO analyticsDTO = analyticsDTOs.stream().filter(dto -> Objects.equals(dto.id, message.getAuthorId())).findFirst().orElse(null);

            if (analyticsDTO == null) {
                analyticsDTO = new AnalyticsDTO(message.getAuthorId(), message.getAuthor());
                analyticsDTOs.add(analyticsDTO);
            }

            if (!Objects.equals(lastUserId[0], analyticsDTO.id)) {
                int reactionTime = message.getUnixTime() - lastMessageTime[0];
                if (reactionTime < maxIgnoreTime) {
                    analyticsDTO.tempReactionTime += reactionTime;
                } else {
                    analyticsDTO.numberOfStartedConverses++;
                }
                lastMessageTime[0] = message.getUnixTime();
            }

            analyticsDTO.tempNumberOfMessages++;
            analyticsDTO.numberOfSymbols += message.getText().length();
            lastUserId[0] = analyticsDTO.id;

            if (message.getUnixTime() >= timePoints.peek()) {
                analyticsDTOs.forEach(dto -> {
                    TimePointData timePointData = new TimePointData(timePoints.peek(), dto.tempReactionTime, dto.tempNumberOfMessages);
                    dto.timePointDataList.add(timePointData);
                    dto.tempReactionTime = dto.tempNumberOfMessages = 0;
                });
                timePoints.pop();
            }

        });

        analyticsDTOs.forEach(dto -> dto.timePointDataList.add(0, new TimePointData(firstPoint, 0, 0)));

        return analyticsDTOs;
    }
}
