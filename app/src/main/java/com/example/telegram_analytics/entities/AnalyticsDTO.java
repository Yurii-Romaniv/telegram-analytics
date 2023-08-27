package com.example.telegram_analytics.entities;

public class AnalyticsDTO {
    public String id;
    public String name;
    public int reactionTime;
    public int numberOfStartedConverses;
    public int numberOfMessages;
    public long numberOfSymbols;

    public AnalyticsDTO(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
