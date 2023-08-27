package com.example.telegram_analytics.entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ParsedChat {
    private String name;
    private List<ParsedMessage> messages;

    public String getName() {
        return name;
    }

    public List<ParsedMessage> getMessages() {
        return messages;
    }

}