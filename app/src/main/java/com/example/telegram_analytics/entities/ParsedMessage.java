package com.example.telegram_analytics.entities;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ParsedMessage {
    private int id;

    private String type;

    @JsonAlias("date_unixtime")
    private int unixTime;

    @JsonProperty("from")
    @JsonAlias("actor")
    private String author;

    @JsonProperty("from_id")
    @JsonAlias("actor_id")
    private String authorId;

    private String text;

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getAuthor() {
        return author;
    }

    public int getUnixTime() {
        return unixTime;
    }

    public void setText(Object text) {
        try {
            this.text = (String) text;
        } catch (Exception ignored) {
            this.text = "";
        }
    }
}