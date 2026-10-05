package model;

import java.time.LocalDateTime;

public class Result {
    private final int attemptId;
    private final String studentId;
    private final int examId;
    private final String examName;
    private final int score;
    private final int totalMarks;
    private final int attempted;
    private final int correct;
    private final int wrong;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final String status;

    public Result(int attemptId, String studentId, int examId, String examName,
                  int score, int totalMarks, int attempted, int correct, int wrong,
                  LocalDateTime startTime, LocalDateTime endTime, String status) {
        this.attemptId = attemptId;
        this.studentId = studentId;
        this.examId = examId;
        this.examName = examName;
        this.score = score;
        this.totalMarks = totalMarks;
        this.attempted = attempted;
        this.correct = correct;
        this.wrong = wrong;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public int getAttemptId() { return attemptId; }
    public String getStudentId() { return studentId; }
    public int getExamId() { return examId; }
    public String getExamName() { return examName; }
    public int getScore() { return score; }
    public int getTotalMarks() { return totalMarks; }
    public int getAttempted() { return attempted; }
    public int getCorrect() { return correct; }
    public int getWrong() { return wrong; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public String getStatus() { return status; }
}
