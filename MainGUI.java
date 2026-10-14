import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MainGUI extends JFrame {
    private final JTextField input = new JTextField(30);
    private final JLabel resultLabel = new JLabel("Result: ");
    private final JTextArea details = new JTextArea();
    private final DefaultTableModel tokenModel =
            new DefaultTableModel(new String[]{"#", "Type", "Value"}, 0);
    private final DefaultTableModel historyModel =
            new DefaultTableModel(new String[]{"ID", "Expression", "Result", "Status", "Time"}, 0);

    private final Evaluator evaluator = new Evaluator();
    private final TACGenerator tac = new TACGenerator();
    private final DatabaseHelper db = new DatabaseHelper();

    public MainGUI() {
        setTitle("Expression Parser and Calculator");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Top: input area
        JButton calcBtn = new JButton("Calculate");
        JButton clearBtn = new JButton("Clear");
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Expression:"));
        top.add(input);
        top.add(calcBtn);
        top.add(clearBtn);
        add(top, BorderLayout.NORTH);

        // Center: tokens table + tree/TAC area
        JTable tokenTable = new JTable(tokenModel) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JScrollPane tokenScroll = new JScrollPane(tokenTable);
        tokenScroll.setBorder(BorderFactory.createTitledBorder("Tokens (Lexer)"));

        details.setEditable(false);
        details.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane detailScroll = new JScrollPane(details);
        detailScroll.setBorder(BorderFactory.createTitledBorder("Parse Tree and Three-Address Code"));

        JPanel center = new JPanel(new GridLayout(1, 2, 10, 10));
        center.add(tokenScroll);
        center.add(detailScroll);
        add(center, BorderLayout.CENTER);

        // Bottom: result + history table
        resultLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        JTable historyTable = new JTable(historyModel) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JScrollPane historyScroll = new JScrollPane(historyTable);
        historyScroll.setBorder(BorderFactory.createTitledBorder("History (SQL Server)"));
        historyScroll.setPreferredSize(new Dimension(900, 190));

        JPanel bottom = new JPanel(new BorderLayout(5, 5));
        bottom.add(resultLabel, BorderLayout.NORTH);
        bottom.add(historyScroll, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        // Events
        calcBtn.addActionListener(e -> calculate());
        input.addActionListener(e -> calculate());   // Enter key
        clearBtn.addActionListener(e -> {
            input.setText("");
            tokenModel.setRowCount(0);
            details.setText("");
            resultLabel.setText("Result: ");
            resultLabel.setForeground(Color.BLACK);
        });

        loadHistory();
    }

    private void calculate() {
        String expr = input.getText().trim();
        if (expr.isEmpty()) {
            resultLabel.setText("Result: please enter an expression");
            resultLabel.setForeground(Color.RED);
            return;
        }

        tokenModel.setRowCount(0);
        details.setText("");
        String tokenText = "";
        String result;
        String status;

        try {
            List<Token> tokens = new Lexer(expr).tokenize();
            tokenText = tokens.toString();
            int i = 1;
            for (Token t : tokens) {
                tokenModel.addRow(new Object[]{i++, t.type, t.value});
            }

            Node tree = new Parser(tokens).parse();
            StringBuilder sb = new StringBuilder();
            sb.append("Tree: ").append(tree).append("\n\n");
            sb.append("Three-Address Code:\n");
            for (String line : tac.generate(tree)) {
                sb.append("  ").append(line).append("\n");
            }
            details.setText(sb.toString());

            double value = evaluator.evaluate(tree);
            result = (value == (long) value) ? String.valueOf((long) value) : String.valueOf(value);
            status = "SUCCESS";
            resultLabel.setText("Result: " + result);
            resultLabel.setForeground(new Color(0, 130, 0));
        } catch (RuntimeException ex) {
            result = ex.getMessage();
            status = "ERROR";
            resultLabel.setText("Error: " + result);
            resultLabel.setForeground(Color.RED);
        }

        try {
            db.save(expr, tokenText, result, status);
            loadHistory();
        } catch (RuntimeException ex) {
            details.append("\n" + ex.getMessage());
        }
    }

    private void loadHistory() {
        historyModel.setRowCount(0);
        try {
            for (String[] row : db.getHistory()) {
                historyModel.addRow(row);
            }
        } catch (RuntimeException ex) {
            details.setText(ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainGUI().setVisible(true));
    }
}