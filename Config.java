package com.novaswing;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Configuration management for API keys and settings
 */
public class Config {
    private static final String CONFIG_FILE = "config.properties";
    private static final String DEFAULT_API_KEY = "your-openai-api-key-here";

    private Properties properties;

    public Config() {
        this.properties = new Properties();
        loadConfiguration();
    }

    private void loadConfiguration() {
        try {
            // Try to load from config file first
            try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
                properties.load(fis);
            } catch (IOException e) {
                // If config file doesn't exist, use environment variables or defaults
                loadFromEnvironment();
            }
        } catch (Exception e) {
            System.err.println("Warning: Could not load configuration. Using defaults.");
            loadFromEnvironment();
        }
    }

    private void loadFromEnvironment() {
        // Try to get API key from environment variable
        String apiKey = System.getenv("OPENAI_API_KEY");
        if (apiKey == null || apiKey.trim().isEmpty()) {
            // Try system property
            apiKey = System.getProperty("openai.api.key");
        }

        if (apiKey == null || apiKey.trim().isEmpty()) {
            apiKey = DEFAULT_API_KEY;
        }

        properties.setProperty("openai.api.key", apiKey);
    }

    public String getOpenAIApiKey() {
        return properties.getProperty("openai.api.key", DEFAULT_API_KEY);
    }

    public boolean isApiKeyConfigured() {
        String apiKey = getOpenAIApiKey();
        return apiKey != null && !apiKey.trim().isEmpty() && !apiKey.equals(DEFAULT_API_KEY);
    }

    public void setOpenAIApiKey(String apiKey) {
        properties.setProperty("openai.api.key", apiKey);
    }
}
