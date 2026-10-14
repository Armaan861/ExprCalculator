public class Token {
    public enum Type { NUM, PLUS, MINUS, MUL, DIV, LPAREN, RPAREN, EOF }

    public final Type type;
    public final String value;

    public Token(Type type, String value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return type == Type.NUM ? "NUM(" + value + ")" : type.toString();
    }
}