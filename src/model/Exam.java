package model;

public class Exam {
    private final int id;
    private String name;
    private int durationMinutes;
    private int totalMarks;
    private boolean active;

    public Exam(int id, String name, int durationMinutes, int totalMarks, boolean active) {
        this.id = id;
        this.name = name;
        this.durationMinutes = durationMinutes;
        this.totalMarks = totalMarks;
        this.active = active;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getDurationMinutes() { return durationMinutes; }
    public int getTotalMarks() { return totalMarks; }
    public boolean isActive() { return active; }

    public void setName(String name) { this.name = name; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }
    public void setTotalMarks(int totalMarks) { this.totalMarks = totalMarks; }
    public void setActive(boolean active) { this.active = active; }
}
