public class TestNova {
    public static void main(String[] args) {
        NovaBot nova = new NovaBot("Nova");
        String[] prompts = {
                "Hello",
                "Tell me something interesting about space.",
                "Tell me a long story about a small robot who wanted to learn poetry."
        };

        for (String p : prompts) {
            System.out.println("You: " + p);
            String r = nova.getResponse(p);
            System.out.println("Nova: " + r);
            System.out.println("-----------------------------------------\n");
        }
    }
}
