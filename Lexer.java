import java.util.*;

public class Lexer {
    private final String text;
    private int pos = 0;

    public Lexer(String text) {
        this.text = text;
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        while (pos < text.length()) {
            char c = text.charAt(pos);
            if (Character.isWhitespace(c)) {
                pos++;
            } else if (Character.isDigit(c) || c == '.') {
                int start = pos;
                while (pos < text.length() &&
                        (Character.isDigit(text.charAt(pos)) || text.charAt(pos) == '.')) {
                    pos++;
                }
                tokens.add(new Token(Token.Type.NUM, text.substring(start, pos)));
            } else {
                switch (c) {
                    case '+': tokens.add(new Token(Token.Type.PLUS, "+")); break;
                    case '-': tokens.add(new Token(Token.Type.MINUS, "-")); break;
                    case '*': tokens.add(new Token(Token.Type.MUL, "*")); break;
                    case '/': tokens.add(new Token(Token.Type.DIV, "/")); break;
                    case '(': tokens.add(new Token(Token.Type.LPAREN, "(")); break;
                    case ')': tokens.add(new Token(Token.Type.RPAREN, ")")); break;
                    default:
                        throw new RuntimeException(
                                "Invalid character '" + c + "' at position " + pos);
                }
                pos++;
            }
        }
        tokens.add(new Token(Token.Type.EOF, ""));
        return tokens;
    }
}