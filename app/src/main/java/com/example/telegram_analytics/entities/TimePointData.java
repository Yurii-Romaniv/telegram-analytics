package com.example.telegram_analytics.entities;

public class TimePointData {
    public TimePointData(int unixTime, int totalReactionTime, int numberOfMessages) {
        this.unixTimeInMs = unixTime * 1000L;
        this.numberOfMessages = numberOfMessages;
        this.reactionTime = totalReactionTime / ((numberOfMessages != 0) ? numberOfMessages : 1);
    }

    private final long unixTimeInMs;
    private final int reactionTime;
    private final int numberOfMessages;

    public long getUnixTimeInMs() {
        return unixTimeInMs;
    }

    public int getReactionTime() {
        return reactionTime;
    }

    public int getNumberOfMessages() {
        return numberOfMessages;
    }
}