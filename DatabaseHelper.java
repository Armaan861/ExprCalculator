import java.sql.*;
import java.util.*;

public class DatabaseHelper {
    private static final String URL =
            "jdbc:sqlserver://localhost:1433;databaseName=ExprCalcDB;"
                    + "encrypt=true;trustServerCertificate=true";
    private static final String USER = "calcuser";
    private static final String PASSWORD = "your_password";

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void save(String expression, String tokens, String result, String status) {
        String sql = "INSERT INTO history(expression, tokens, result, status) VALUES(?,?,?,?)";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, expression);
            ps.setString(2, tokens);
            ps.setString(3, result);
            ps.setString(4, status);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage());
        }
    }

    public List<String[]> getHistory() {
        List<String[]> rows = new ArrayList<>();
        String sql = "SELECT id, expression, result, status, created_at FROM history ORDER BY id DESC";
        try (Connection conn = connect();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                rows.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("expression"),
                        rs.getString("result"),
                        rs.getString("status"),
                        rs.getString("created_at")
                });
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage());
        }
        return rows;
    }
}