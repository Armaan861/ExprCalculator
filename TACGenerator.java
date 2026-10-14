import java.util.*;

public class TACGenerator {
    private int tempCount = 0;
    private final List<String> code = new ArrayList<>();

    public List<String> generate(Node root) {
        tempCount = 0;
        code.clear();
        String result = walk(root);
        // if expression is just a number (e.g. "5"), still show a line
        if (code.isEmpty()) {
            code.add("t1 = " + result);
        }
        return new ArrayList<>(code);
    }

    private String walk(Node node) {
        if (node instanceof NumNode) {
            return node.toString();
        }

        BinOpNode bin = (BinOpNode) node;
        String left = walk(bin.left);
        String right = walk(bin.right);

        tempCount++;
        String temp = "t" + tempCount;
        code.add(temp + " = " + left + " " + bin.op + " " + right);
        return temp;
    }
}