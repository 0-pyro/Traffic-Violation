import java.sql.*;

public class DBC {
    static String url = "jdbc:mysql://127.0.0.1:3306/javaproj";
    static String user = "root";
    static String pass = "root";

    static Connection conDB() {
        try {
            return DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
