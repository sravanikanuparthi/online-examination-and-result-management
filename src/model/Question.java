package model;

public class Question {
    private final int id;
    private final int examId;
    private final String text;
    private final String optionA;
    private final String optionB;
    private final String optionC;
    private final String optionD;
    private final char correctOption;
    private final int marks;

    public Question(int id, int examId, String text, String optionA, String optionB,
                    String optionC, String optionD, char correctOption, int marks) {
        this.id = id;
        this.examId = examId;
        this.text = text;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctOption = Character.toUpperCase(correctOption);
        this.marks = marks;
    }

    public int getId() { return id; }
    public int getExamId() { return examId; }
    public String getText() { return text; }
    public String getOptionA() { return optionA; }
    public String getOptionB() { return optionB; }
    public String getOptionC() { return optionC; }
    public String getOptionD() { return optionD; }
    public char getCorrectOption() { return correctOption; }
    public int getMarks() { return marks; }
}
