import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int pos = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    private Token current() {
        return tokens.get(pos);
    }

    public Node parse() {
        Node tree = parseExpr();
        if (current().type != Token.Type.EOF) {
            throw new RuntimeException("Syntax error: unexpected " + current());
        }
        return tree;
    }

    // expr → term (('+' | '-') term)*
    private Node parseExpr() {
        Node left = parseTerm();
        while (current().type == Token.Type.PLUS || current().type == Token.Type.MINUS) {
            char op = current().value.charAt(0);
            pos++;
            Node right = parseTerm();
            left = new BinOpNode(left, op, right);
        }
        return left;
    }

    // term → factor (('*' | '/') factor)*
    private Node parseTerm() {
        Node left = parseFactor();
        while (current().type == Token.Type.MUL || current().type == Token.Type.DIV) {
            char op = current().value.charAt(0);
            pos++;
            Node right = parseFactor();
            left = new BinOpNode(left, op, right);
        }
        return left;
    }

    // factor → NUMBER | '(' expr ')'
    private Node parseFactor() {
        Token t = current();
        if (t.type == Token.Type.NUM) {
            pos++;
            return new NumNode(Double.parseDouble(t.value));
        }
        if (t.type == Token.Type.LPAREN) {
            pos++;
            Node inside = parseExpr();
            if (current().type != Token.Type.RPAREN) {
                throw new RuntimeException("Syntax error: missing ')'");
            }
            pos++;
            return inside;
        }
        throw new RuntimeException("Syntax error: unexpected " + t);
    }
}