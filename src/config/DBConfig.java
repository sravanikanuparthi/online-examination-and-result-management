package config;

public final class DBConfig {
    public static final String URL =
            "jdbc:mysql://localhost:3306/online_exam?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    public static final String USER = "root";
    public static final String PASSWORD = "";

    private DBConfig() {
    }
}