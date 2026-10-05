package app;

import model.Exam;
import model.Question;
import model.Result;
import service.ExamService;
import service.ResultService;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class AdminApp {
    private final Scanner scanner;
    private final ExamService examService = new ExamService();
    private final ResultService resultService = new ResultService();

    public AdminApp(Scanner scanner) {
        this.scanner = scanner;
    }

    public void run() {
        while (true) {
            System.out.println("\n========================================");
            System.out.println("              ADMIN MENU");
            System.out.println("========================================");
            System.out.println("1. Create Exam");
            System.out.println("2. View Exams");
            System.out.println("3. Update Exam");
            System.out.println("4. Delete Exam");
            System.out.println("5. Add Question");
            System.out.println("6. View Questions");
            System.out.println("7. Update Question");
            System.out.println("8. Delete Question");
            System.out.println("9. View Student Results");
            System.out.println("10. Logout");

            int choice = readInt("Enter choice: ");

            try {
                switch (choice) {
                    case 1 -> createExam();
                    case 2 -> viewExams();
                    case 3 -> updateExam();
                    case 4 -> deleteExam();
                    case 5 -> addQuestion();
                    case 6 -> viewQuestions();
                    case 7 -> updateQuestion();
                    case 8 -> deleteQuestion();
                    case 9 -> viewResults();
                    case 10 -> { return; }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            }
        }
    }

    private void createExam() throws SQLException {
        int id = readInt("Exam ID: ");
        String name = readLine("Exam name: ");
        int duration = readInt("Duration (minutes): ");
        int marks = readInt("Total marks: ");

        examService.createExam(new Exam(id, name, duration, marks, true));
        System.out.println("Exam created.");
    }

    private void viewExams() throws SQLException {
        List<Exam> exams = examService.getAllExams();

        System.out.printf("%n%-8s %-30s %-12s %-10s %-8s%n",
                "ID", "Exam", "Duration", "Marks", "Active");

        for (Exam e : exams) {
            System.out.printf("%-8d %-30s %-12d %-10d %-8s%n",
                    e.getId(), e.getName(), e.getDurationMinutes(),
                    e.getTotalMarks(), e.isActive());
        }
    }

    private void updateExam() throws SQLException {
        int id = readInt("Exam ID: ");
        Exam old = examService.getExam(id);

        if (old == null) {
            System.out.println("Exam not found.");
            return;
        }

        String name = readLine("New name: ");
        int duration = readInt("New duration: ");
        int marks = readInt("New total marks: ");
        boolean active = readLine("Active? (Y/N): ").equalsIgnoreCase("Y");

        examService.updateExam(new Exam(id, name, duration, marks, active));
        System.out.println("Exam updated.");
    }

    private void deleteExam() throws SQLException {
        int id = readInt("Exam ID: ");
        examService.deleteExam(id);
        System.out.println("Exam deleted.");
    }

    private void addQuestion() throws SQLException {
        int examId = readInt("Exam ID: ");
        String text = readLine("Question: ");
        String a = readLine("Option A: ");
        String b = readLine("Option B: ");
        String c = readLine("Option C: ");
        String d = readLine("Option D: ");
        char correct = readLine("Correct option (A/B/C/D): ").toUpperCase().charAt(0);
        int marks = readInt("Marks: ");

        examService.addQuestion(
                new Question(0, examId, text, a, b, c, d, correct, marks));
        System.out.println("Question added.");
    }

    private void viewQuestions() throws SQLException {
        int examId = readInt("Exam ID: ");
        List<Question> questions = examService.getQuestions(examId);

        for (Question q : questions) {
            System.out.println("\nID: " + q.getId());
            System.out.println("Q: " + q.getText());
            System.out.println("A: " + q.getOptionA());
            System.out.println("B: " + q.getOptionB());
            System.out.println("C: " + q.getOptionC());
            System.out.println("D: " + q.getOptionD());
            System.out.println("Correct: " + q.getCorrectOption());
            System.out.println("Marks: " + q.getMarks());
        }
    }

    private void updateQuestion() throws SQLException {
        int id = readInt("Question ID: ");
        int examId = readInt("Exam ID: ");
        String text = readLine("Question: ");
        String a = readLine("Option A: ");
        String b = readLine("Option B: ");
        String c = readLine("Option C: ");
        String d = readLine("Option D: ");
        char correct = readLine("Correct option: ").toUpperCase().charAt(0);
        int marks = readInt("Marks: ");

        examService.updateQuestion(
                new Question(id, examId, text, a, b, c, d, correct, marks));
        System.out.println("Question updated.");
    }

    private void deleteQuestion() throws SQLException {
        int id = readInt("Question ID: ");
        examService.deleteQuestion(id);
        System.out.println("Question deleted.");
    }

    private void viewResults() throws SQLException {
        List<Result> results = resultService.getAllResults();

        if (results.isEmpty()) {
            System.out.println("No results available.");
            return;
        }

        System.out.printf("%n%-10s %-15s %-25s %-10s %-15s%n",
                "Attempt", "Student", "Exam", "Score", "Status");

        for (Result r : results) {
            System.out.printf("%-10d %-15s %-25s %-10s %-15s%n",
                    r.getAttemptId(), r.getStudentId(), r.getExamName(),
                    r.getScore() + "/" + r.getTotalMarks(), r.getStatus());
        }
    }

    private int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number.");
            }
        }
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}
