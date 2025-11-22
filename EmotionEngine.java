import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class EmotionEngine {
    private final Map<String, Double> emotionalState;
    private final Map<Emotion, Function<String, String>> emotionalResponses;

    public EmotionEngine() {
        this.emotionalState = new ConcurrentHashMap<>();
        this.emotionalState.put("happiness", 0.5);
        this.emotionalState.put("empathy", 0.5);
        this.emotionalState.put("curiosity", 0.5);

        this.emotionalResponses = new ConcurrentHashMap<>();
        setupEmotionalResponses();
    }

    private void setupEmotionalResponses() {
        emotionalResponses.put(Emotion.HAPPY, response -> addEmoji(response, "😊"));
        emotionalResponses.put(Emotion.CURIOUS, response -> addEmoji(response, "🤔"));
        emotionalResponses.put(Emotion.EMPATHETIC, response -> addEmoji(response, "💗"));
        emotionalResponses.put(Emotion.THOUGHTFUL, response -> addEmoji(response, "✨"));
    }

    public void updateEmotionalState(String userInput) {
        // Analyze user input and update emotional state
        if (containsPositiveWords(userInput)) {
            adjustEmotion("happiness", 0.1);
        }
        if (containsNegativeWords(userInput)) {
            adjustEmotion("happiness", -0.1);
        }
        if (containsQuestions(userInput)) {
            adjustEmotion("curiosity", 0.1);
        }
        if (containsEmotionalContent(userInput)) {
            adjustEmotion("empathy", 0.1);
        }
    }

    public String modifyResponse(String response) {
        Emotion dominantEmotion = getDominantEmotion();
        Function<String, String> emotionalModifier = emotionalResponses.get(dominantEmotion);
        return emotionalModifier != null ? emotionalModifier.apply(response) : response;
    }

    private String addEmoji(String response, String emoji) {
        return emoji + " " + response;
    }

    private void adjustEmotion(String emotion, double delta) {
        double currentValue = emotionalState.getOrDefault(emotion, 0.5);
        emotionalState.put(emotion, Math.min(1.0, Math.max(0.0, currentValue + delta)));
    }

    private Emotion getDominantEmotion() {
        double happiness = emotionalState.get("happiness");
        double empathy = emotionalState.get("empathy");
        double curiosity = emotionalState.get("curiosity");

        if (happiness > 0.7)
            return Emotion.HAPPY;
        if (empathy > 0.7)
            return Emotion.EMPATHETIC;
        if (curiosity > 0.7)
            return Emotion.CURIOUS;
        return Emotion.THOUGHTFUL;
    }

    private boolean containsPositiveWords(String input) {
        String lowerInput = input.toLowerCase();
        return lowerInput.matches(".*(happy|great|awesome|excellent|good|love|thank|thanks).*");
    }

    private boolean containsNegativeWords(String input) {
        String lowerInput = input.toLowerCase();
        return lowerInput.matches(".*(sad|bad|terrible|awful|worst|hate|angry|upset).*");
    }

    private boolean containsQuestions(String input) {
        return input.contains("?") || input.toLowerCase().matches(".*(what|why|how|when|where|who).*");
    }

    private boolean containsEmotionalContent(String input) {
        String lowerInput = input.toLowerCase();
        return lowerInput.matches(".*(feel|feeling|emotion|care|worry|concern|love|hate|happy|sad|angry).*");
    }
}

enum Emotion {
    HAPPY, CURIOUS, EMPATHETIC, THOUGHTFUL
}