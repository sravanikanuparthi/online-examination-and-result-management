package database;

import config.DBConfig;
import java.sql.*;

public class DatabaseManager {
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC driver not found.", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                DBConfig.URL, DBConfig.USER, DBConfig.PASSWORD);
    }

    public static void close(AutoCloseable resource) {
        if (resource != null) {
            try { resource.close(); }
            catch (Exception ignored) {}
        }
    }
}
