package com.novaswing;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * AI Service that connects to OpenAI API for intelligent responses
 */
public class AIService {
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String MODEL = "gpt-3.5-turbo";

    private final OkHttpClient client;
    private final ObjectMapper objectMapper;
    private final String apiKey;

    public AIService(String apiKey) {
        this.apiKey = apiKey;
        this.objectMapper = new ObjectMapper();
        this.client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    /**
     * Get AI response from OpenAI API
     * 
     * @param userInput The user's input message
     * @param emotion   The emotional context
     * @return AI-generated response
     */
    public String getAIResponse(String userInput, String emotion) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            return "API key not configured. Please set your OpenAI API key.";
        }

        try {
            String requestBody = createRequestBody(userInput, emotion);
            Request request = createRequest(requestBody);

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    return handleErrorResponse(response);
                }

                return parseAIResponse(response.body().string());
            }
        } catch (IOException e) {
            return "Sorry, I'm having trouble connecting to the AI service. Error: " + e.getMessage();
        } catch (Exception e) {
            return "An unexpected error occurred: " + e.getMessage();
        }
    }

    private String createRequestBody(String userInput, String emotion) throws Exception {
        String systemPrompt = String.format(
                "You are Nova, a friendly AI assistant. The user is feeling %s. " +
                        "Respond in a warm, empathetic way that matches their emotional state. " +
                        "Keep responses conversational and helpful, but not too long.",
                emotion);

        String requestJson = String.format("""
                {
                    "model": "%s",
                    "messages": [
                        {"role": "system", "content": "%s"},
                        {"role": "user", "content": "%s"}
                    ],
                    "max_tokens": 150,
                    "temperature": 0.7
                }
                """, MODEL, systemPrompt, userInput.replace("\"", "\\\""));

        return requestJson;
    }

    private Request createRequest(String requestBody) {
        RequestBody body = RequestBody.create(
                requestBody,
                MediaType.get("application/json; charset=utf-8"));

        return new Request.Builder()
                .url(OPENAI_API_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .build();
    }

    private String parseAIResponse(String responseBody) throws Exception {
        JsonNode root = objectMapper.readTree(responseBody);
        JsonNode choices = root.get("choices");

        if (choices != null && choices.isArray() && choices.size() > 0) {
            JsonNode message = choices.get(0).get("message");
            if (message != null && message.has("content")) {
                return message.get("content").asText().trim();
            }
        }

        return "Sorry, I couldn't process the AI response.";
    }

    private String handleErrorResponse(Response response) throws IOException {
        String errorBody = response.body() != null ? response.body().string() : "No error details";

        if (response.code() == 401) {
            return "Invalid API key. Please check your OpenAI API key.";
        } else if (response.code() == 429) {
            return "Rate limit exceeded. Please try again later.";
        } else if (response.code() == 500) {
            return "OpenAI service is temporarily unavailable.";
        } else {
            return String.format("API Error %d: %s", response.code(), errorBody);
        }
    }

    /**
     * Test the API connection
     * 
     * @return true if connection is successful
     */
    public boolean testConnection() {
        try {
            String response = getAIResponse("Hello", "happy");
            return !response.contains("Error") && !response.contains("trouble");
        } catch (Exception e) {
            return false;
        }
    }
}
