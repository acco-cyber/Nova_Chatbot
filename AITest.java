package com.novaswing;

/**
 * Simple test class to verify AI API integration
 */
public class AITest {

    public static void main(String[] args) {
        System.out.println("🧪 Testing Nova Swing AI Integration");
        System.out.println("====================================");

        // Test configuration
        Config config = new Config();
        System.out.println("📋 Configuration Test:");
        System.out.println("API Key configured: " + config.isApiKeyConfigured());
        System.out.println("API Key (masked): " + maskApiKey(config.getOpenAIApiKey()));

        // Test AI service
        AIService aiService = new AIService(config.getOpenAIApiKey());

        System.out.println("\n🤖 AI Service Test:");
        if (config.isApiKeyConfigured()) {
            System.out.println("Testing connection...");
            boolean connected = aiService.testConnection();
            System.out.println("Connection successful: " + connected);

            if (connected) {
                System.out.println("\nTesting AI response...");
                String response = aiService.getAIResponse("Hello, this is a test!", "happy");
                System.out.println("AI Response: " + response);
            }
        } else {
            System.out.println("⚠️  Skipping AI tests - API key not configured");
            System.out.println("Please set your OpenAI API key to run full tests");
        }

        System.out.println("\n✅ Test completed!");
    }

    private static String maskApiKey(String apiKey) {
        if (apiKey == null || apiKey.length() < 8) {
            return "Not configured";
        }
        return apiKey.substring(0, 4) + "..." + apiKey.substring(apiKey.length() - 4);
    }
}
