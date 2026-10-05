package model;

public abstract class User {
    private final String id;
    private final String password;

    protected User(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public String getId() { return id; }
    public String getPassword() { return password; }
}
