package util;

import model.Result;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public final class FileExporter {
    private FileExporter() {}

    public static String exportResult(Result result) throws IOException {
        File dir = new File("reports");
        if (!dir.exists()) dir.mkdirs();

        String filename = "reports/result_" + result.getStudentId() +
                "_" + result.getAttemptId() + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("ONLINE EXAMINATION SYSTEM\n");
            writer.write("==========================\n");
            writer.write("Student ID: " + result.getStudentId() + "\n");
            writer.write("Exam: " + result.getExamName() + "\n");
            writer.write("Score: " + result.getScore() + "/" + result.getTotalMarks() + "\n");
            writer.write("Attempted: " + result.getAttempted() + "\n");
            writer.write("Correct: " + result.getCorrect() + "\n");
            writer.write("Wrong: " + result.getWrong() + "\n");
            writer.write("Status: " + result.getStatus() + "\n");
        }

        return filename;
    }
}
