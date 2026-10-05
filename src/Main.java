import app.AdminApp;
import app.StudentApp;
import exception.InvalidLoginException;
import model.Student;
import service.AuthenticationService;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    private final Scanner scanner = new Scanner(System.in);
    private final AuthenticationService authService = new AuthenticationService();

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        while (true) {
            System.out.println("\n========================================");
            System.out.println("       ONLINE EXAMINATION SYSTEM");
            System.out.println("========================================");
            System.out.println("1. Student Login");
            System.out.println("2. Admin Login");
            System.out.println("3. Register Student");
            System.out.println("4. Exit");

            int choice = readInt("Enter choice: ");

            try {
                switch (choice) {
                    case 1 -> studentLogin();
                    case 2 -> adminLogin();
                    case 3 -> registerStudent();
                    case 4 -> {
                        System.out.println("Thank you for using the system.");
                        return;
                    }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void studentLogin() {
        String id = readLine("Student ID: ");
        String password = readLine("Password: ");

        try {
            Student student = authService.studentLogin(id, password);
            System.out.println("Login successful.");
            new StudentApp(scanner, student).run();
        } catch (InvalidLoginException e) {
            System.out.println(e.getMessage());
        }
    }

    private void adminLogin() {
        String username = readLine("Admin username: ");
        String password = readLine("Password: ");

        try {
            authService.adminLogin(username, password);
            System.out.println("Admin login successful.");
            new AdminApp(scanner).run();
        } catch (InvalidLoginException e) {
            System.out.println(e.getMessage());
        }
    }

    private void registerStudent() {
        String id = readLine("Student ID: ");
        String name = readLine("Name: ");
        String password = readLine("Password: ");
        String email = readLine("Email: ");

        try {
            authService.registerStudent(id, name, password, email);
            System.out.println("Student registered successfully.");
        } catch (SQLException e) {
            System.out.println("Registration failed: " + e.getMessage());
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
