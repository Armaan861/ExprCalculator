public abstract class Node {
    public abstract String toString();
}

class NumNode extends Node {
    public final double value;

    public NumNode(double value) {
        this.value = value;
    }

    public String toString() {
        return (value == (long) value) ? String.valueOf((long) value) : String.valueOf(value);
    }
}

class BinOpNode extends Node {
    public final Node left;
    public final char op;
    public final Node right;

    public BinOpNode(Node left, char op, Node right) {
        this.left = left;
        this.op = op;
        this.right = right;
    }

    public String toString() {
        return "(" + left + " " + op + " " + right + ")";
    }
}