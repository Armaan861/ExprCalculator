import java.util.List;

public class Main {
    public static void main(String[] args) {
        String[] tests = { "2+3*4", "(2+3)*4-5", "10/4", "5/0", "2+*3", "(2+3" };
        Evaluator evaluator = new Evaluator();
        DatabaseHelper db = new DatabaseHelper();

        for (String t : tests) {
            String tokenText = "";
            try {
                List<Token> tokens = new Lexer(t).tokenize();
                tokenText = tokens.toString();
                Node tree = new Parser(tokens).parse();
                double result = evaluator.evaluate(tree);
                System.out.println(t + " = " + result);
                db.save(t, tokenText, String.valueOf(result), "SUCCESS");
            } catch (RuntimeException e) {
                System.out.println(t + " -> Error: " + e.getMessage());
                db.save(t, tokenText, e.getMessage(), "ERROR");
            }
        }

        System.out.println("\n--- History from database ---");
        for (String[] row : db.getHistory()) {
            System.out.println(String.join(" | ", row));
        }
    }
}