package com.example.telegram_analytics;

import android.content.Context;
import android.net.Uri;

import com.example.telegram_analytics.entities.ParsedChat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.Objects;

public class DataExtractor {

    public static ParsedChat getParsedChat(Context context, Uri uri) throws IOException {
        return parseJson(readJsonFromUri(context, uri));
    }

    private static String readJsonFromUri(Context context, Uri uri) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (
                InputStream inputStream = context.getContentResolver().openInputStream(uri);
                BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        return stringBuilder.toString();
    }


    private static ParsedChat parseJson(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        return objectMapper.readValue(json, ParsedChat.class);
    }
}
