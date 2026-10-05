package model;

public class Student extends User {
    private final String name;
    private final String email;

    public Student(String id, String name, String password, String email) {
        super(id, password);
        this.name = name;
        this.email = email;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
}
