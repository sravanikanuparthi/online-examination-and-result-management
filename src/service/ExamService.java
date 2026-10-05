package service;

import database.DatabaseManager;
import model.Exam;
import model.Question;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExamService {

    public List<Exam> getActiveExams() throws SQLException {
        String sql = "SELECT exam_id, exam_name, duration_minutes, total_marks, active " +
                     "FROM exams WHERE active = TRUE ORDER BY exam_id";
        List<Exam> exams = new ArrayList<>();

        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                exams.add(new Exam(
                        rs.getInt("exam_id"),
                        rs.getString("exam_name"),
                        rs.getInt("duration_minutes"),
                        rs.getInt("total_marks"),
                        rs.getBoolean("active")));
            }
        }
        return exams;
    }

    public List<Exam> getAllExams() throws SQLException {
        String sql = "SELECT exam_id, exam_name, duration_minutes, total_marks, active " +
                     "FROM exams ORDER BY exam_id";
        List<Exam> exams = new ArrayList<>();

        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                exams.add(new Exam(
                        rs.getInt("exam_id"),
                        rs.getString("exam_name"),
                        rs.getInt("duration_minutes"),
                        rs.getInt("total_marks"),
                        rs.getBoolean("active")));
            }
        }
        return exams;
    }

    public Exam getExam(int id) throws SQLException {
        String sql = "SELECT exam_id, exam_name, duration_minutes, total_marks, active " +
                     "FROM exams WHERE exam_id = ?";
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Exam(
                            rs.getInt("exam_id"),
                            rs.getString("exam_name"),
                            rs.getInt("duration_minutes"),
                            rs.getInt("total_marks"),
                            rs.getBoolean("active"));
                }
            }
        }
        return null;
    }

    public List<Question> getQuestions(int examId) throws SQLException {
        String sql = "SELECT question_id, exam_id, question_text, option_a, option_b, " +
                     "option_c, option_d, correct_option, marks FROM questions " +
                     "WHERE exam_id = ? ORDER BY question_id";
        List<Question> questions = new ArrayList<>();

        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, examId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    questions.add(new Question(
                            rs.getInt("question_id"),
                            rs.getInt("exam_id"),
                            rs.getString("question_text"),
                            rs.getString("option_a"),
                            rs.getString("option_b"),
                            rs.getString("option_c"),
                            rs.getString("option_d"),
                            rs.getString("correct_option").charAt(0),
                            rs.getInt("marks")));
                }
            }
        }
        return questions;
    }

    public void createExam(Exam exam) throws SQLException {
        String sql = "INSERT INTO exams(exam_id,exam_name,duration_minutes,total_marks,active) " +
                     "VALUES(?,?,?,?,?)";
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, exam.getId());
            ps.setString(2, exam.getName());
            ps.setInt(3, exam.getDurationMinutes());
            ps.setInt(4, exam.getTotalMarks());
            ps.setBoolean(5, exam.isActive());
            ps.executeUpdate();
        }
    }

    public void updateExam(Exam exam) throws SQLException {
        String sql = "UPDATE exams SET exam_name=?, duration_minutes=?, total_marks=?, active=? " +
                     "WHERE exam_id=?";
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, exam.getName());
            ps.setInt(2, exam.getDurationMinutes());
            ps.setInt(3, exam.getTotalMarks());
            ps.setBoolean(4, exam.isActive());
            ps.setInt(5, exam.getId());
            ps.executeUpdate();
        }
    }

    public void deleteExam(int id) throws SQLException {
        String sql = "DELETE FROM exams WHERE exam_id=?";
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public void addQuestion(Question q) throws SQLException {
        String sql = "INSERT INTO questions(exam_id,question_text,option_a,option_b," +
                     "option_c,option_d,correct_option,marks) VALUES(?,?,?,?,?,?,?,?)";
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, q.getExamId());
            ps.setString(2, q.getText());
            ps.setString(3, q.getOptionA());
            ps.setString(4, q.getOptionB());
            ps.setString(5, q.getOptionC());
            ps.setString(6, q.getOptionD());
            ps.setString(7, String.valueOf(q.getCorrectOption()));
            ps.setInt(8, q.getMarks());
            ps.executeUpdate();
        }
    }

    public void updateQuestion(Question q) throws SQLException {
        String sql = "UPDATE questions SET question_text=?, option_a=?, option_b=?, " +
                     "option_c=?, option_d=?, correct_option=?, marks=? WHERE question_id=?";
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, q.getText());
            ps.setString(2, q.getOptionA());
            ps.setString(3, q.getOptionB());
            ps.setString(4, q.getOptionC());
            ps.setString(5, q.getOptionD());
            ps.setString(6, String.valueOf(q.getCorrectOption()));
            ps.setInt(7, q.getMarks());
            ps.setInt(8, q.getId());
            ps.executeUpdate();
        }
    }

    public void deleteQuestion(int id) throws SQLException {
        String sql = "DELETE FROM questions WHERE question_id=?";
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
