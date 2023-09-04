package com.example.telegram_analytics.entities;

import java.util.ArrayList;
import java.util.List;

public class AnalyticsDTO {
    public String id;
    public String name;
    public int tempReactionTime;
    public int numberOfStartedConverses;
    public int tempNumberOfMessages;
    public long numberOfSymbols;
    public List<TimePointData> timePointDataList = new ArrayList<>();

    public AnalyticsDTO(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
