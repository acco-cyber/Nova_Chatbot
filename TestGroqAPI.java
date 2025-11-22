package ai;

public class TestGroqAPI {
    public static void main(String[] args) {
        try {
            // Test synchronous completion
            System.out.println("Testing synchronous completion...");
            String result = GroqAPIClient.generateCompletion("What is the capital of France?");
            System.out.println("Response: " + result);

            // Test async completion
            System.out.println("\nTesting asynchronous completion...");
            GroqAPIClient.generateCompletionAsync("What is the capital of Italy?",
                    new GroqAPIClient.CompletionCallback() {
                        @Override
                        public void onSuccess(String result) {
                            System.out.println("Async Response: " + result);
                        }

                        @Override
                        public void onError(Exception e) {
                            System.err.println("Async Error: " + e.getMessage());
                        }
                    });

            // Wait a bit for async completion
            Thread.sleep(5000);

            // Cleanup
            GroqAPIClient.shutdown();

        } catch (Exception e) {
            System.err.println("Test Error: " + e.getMessage());
        }
    }
}