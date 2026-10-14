public class Evaluator {

    public double evaluate(Node node) {
        if (node instanceof NumNode) {
            return ((NumNode) node).value;
        }

        BinOpNode bin = (BinOpNode) node;
        double left = evaluate(bin.left);
        double right = evaluate(bin.right);

        switch (bin.op) {
            case '+': return left + right;
            case '-': return left - right;
            case '*': return left * right;
            case '/':
                if (right == 0) {
                    throw new RuntimeException("Math error: division by zero");
                }
                return left / right;
            default:
                throw new RuntimeException("Unknown operator " + bin.op);
        }
    }
}