package app;

import model.Exam;
import model.Question;
import model.Result;
import model.Student;
import service.ExamService;
import service.ResultService;
import util.ExamTimer;
import util.FileExporter;
import util.InputValidator;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class StudentApp {
    private final Scanner scanner;
    private final Student student;
    private final ExamService examService;
    private final ResultService resultService;

    public StudentApp(Scanner scanner, Student student) {
        this.scanner = scanner;
        this.student = student;
        this.examService = new ExamService();
        this.resultService = new ResultService();
    }

    public void run() {
        while (true) {
            System.out.println("\n========================================");
            System.out.println("          STUDENT DASHBOARD");
            System.out.println("========================================");
            System.out.println("Welcome, " + student.getName());
            System.out.println("1. View Available Exams");
            System.out.println("2. Take Exam");
            System.out.println("3. View My Results");
            System.out.println("4. View Exam History");
            System.out.println("5. Logout");

            int choice = readInt("Enter choice: ");

            try {
                switch (choice) {
                    case 1 -> displayExams();
                    case 2 -> takeExam();
                    case 3, 4 -> displayResults();
                    case 5 -> { return; }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            }
        }
    }

    private void displayExams() throws SQLException {
        List<Exam> exams = examService.getActiveExams();
        if (exams.isEmpty()) {
            System.out.println("No active exams available.");
            return;
        }

        System.out.printf("%n%-8s %-30s %-12s %-10s%n",
                "ID", "Exam", "Duration", "Marks");
        System.out.println("-------------------------------------------------------------");
        for (Exam e : exams) {
            System.out.printf("%-8d %-30s %-12s %-10d%n",
                    e.getId(), e.getName(),
                    e.getDurationMinutes() + " min", e.getTotalMarks());
        }
    }

    private void takeExam() throws SQLException {
        List<Exam> exams = examService.getActiveExams();
        if (exams.isEmpty()) {
            System.out.println("No active exams.");
            return;
        }

        displayExams();
        int examId = readInt("Enter Exam ID (0 to cancel): ");
        if (examId == 0) return;

        Exam exam = examService.getExam(examId);
        if (exam == null || !exam.isActive()) {
            System.out.println("Invalid exam.");
            return;
        }

        List<Question> questions = examService.getQuestions(examId);
        if (questions.isEmpty()) {
            System.out.println("This exam has no questions.");
            return;
        }

        System.out.println("\nQuestions loaded: " + questions.size());
        System.out.println("Duration: " + exam.getDurationMinutes() + " minutes");
        System.out.println("Press ENTER to start...");
        scanner.nextLine();

        int attemptId = resultService.createAttempt(student.getId(), examId);
        ExamTimer timer = new ExamTimer(exam.getDurationMinutes() * 60L);
        timer.start();

        int score = 0;
        int attempted = 0;
        int correct = 0;
        int wrong = 0;

        try {
            for (int i = 0; i < questions.size(); i++) {
                if (timer.isExpired()) break;

                Question q = questions.get(i);

                System.out.println("\n\n========================================");
                System.out.println("Question " + (i + 1) + " / " + questions.size());
                System.out.println("========================================");
                System.out.println(q.getText());
                System.out.println("A. " + q.getOptionA());
                System.out.println("B. " + q.getOptionB());
                System.out.println("C. " + q.getOptionC());
                System.out.println("D. " + q.getOptionD());

                String answer;
                while (true) {
                    System.out.print("Enter answer (A/B/C/D): ");
                    answer = scanner.nextLine().trim();

                    if (InputValidator.isOption(answer)) break;
                    System.out.println("Invalid answer. Enter A, B, C or D.");
                }

                char selected = InputValidator.option(answer);
                boolean isCorrect = selected == q.getCorrectOption();

                attempted++;
                if (isCorrect) {
                    correct++;
                    score += q.getMarks();
                } else {
                    wrong++;
                }

                resultService.saveAnswer(attemptId, q.getId(), selected, isCorrect);
            }
        } finally {
            timer.stopTimer();

            String status = timer.isExpired() ? "TIME_EXPIRED" : "SUBMITTED";
            resultService.finalizeAttempt(
                    attemptId, score, attempted, correct, wrong, status);

            System.out.println("\n\nExam submitted.");
            System.out.println("Score: " + score + "/" + exam.getTotalMarks());
            System.out.println("Correct: " + correct);
            System.out.println("Wrong: " + wrong);
            System.out.println("Attempted: " + attempted);
        }

        displayLatestResult();
    }

    private void displayLatestResult() throws SQLException {
        List<Result> results = resultService.getStudentResults(student.getId());
        if (!results.isEmpty()) {
            Result r = results.get(0);
            double percentage = r.getTotalMarks() == 0 ? 0 :
                    (r.getScore() * 100.0 / r.getTotalMarks());

            System.out.printf("Percentage: %.2f%%%n", percentage);

            try {
                System.out.println("Saving result report...");
                System.out.println("Report: " + FileExporter.exportResult(r));
            } catch (IOException e) {
                System.out.println("Could not export report: " + e.getMessage());
            }
        }
    }

    private void displayResults() throws SQLException {
        List<Result> results = resultService.getStudentResults(student.getId());

        if (results.isEmpty()) {
            System.out.println("No exam history found.");
            return;
        }

        System.out.printf("%n%-8s %-25s %-10s %-12s %-15s%n",
                "Attempt", "Exam", "Score", "Percentage", "Status");
        System.out.println("---------------------------------------------------------------------");

        for (Result r : results) {
            double percentage = r.getTotalMarks() == 0 ? 0 :
                    r.getScore() * 100.0 / r.getTotalMarks();

            System.out.printf("%-8d %-25s %-10s %-11.2f%% %-15s%n",
                    r.getAttemptId(), r.getExamName(),
                    r.getScore() + "/" + r.getTotalMarks(),
                    percentage, r.getStatus());
        }
    }

    private int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
