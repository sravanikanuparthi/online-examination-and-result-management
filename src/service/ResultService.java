package service;

import database.DatabaseManager;
import model.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResultService {

    public int createAttempt(String studentId, int examId) throws SQLException {
        String sql = "INSERT INTO exam_attempts(student_id,exam_id,start_time,status) " +
                     "VALUES(?,?,NOW(),'IN_PROGRESS')";
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, studentId);
            ps.setInt(2, examId);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        throw new SQLException("Could not create exam attempt.");
    }

    public synchronized void saveAnswer(int attemptId, int questionId,
                                         Character selected, boolean correct)
            throws SQLException {
        String sql = "INSERT INTO student_answers(attempt_id,question_id,selected_option,is_correct) " +
                     "VALUES(?,?,?,?) ON DUPLICATE KEY UPDATE selected_option=?, is_correct=?";
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, attemptId);
            ps.setInt(2, questionId);
            if (selected == null) ps.setNull(3, Types.CHAR);
            else ps.setString(3, selected.toString());
            ps.setBoolean(4, correct);
            if (selected == null) ps.setNull(5, Types.CHAR);
            else ps.setString(5, selected.toString());
            ps.setBoolean(6, correct);
            ps.executeUpdate();
        }
    }

    public synchronized void finalizeAttempt(int attemptId, int score, int attempted,
                                              int correct, int wrong, String status)
            throws SQLException {
        String sql = "UPDATE exam_attempts SET end_time=NOW(), score=?, attempted=?, " +
                     "correct=?, wrong=?, status=? WHERE attempt_id=?";
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, score);
            ps.setInt(2, attempted);
            ps.setInt(3, correct);
            ps.setInt(4, wrong);
            ps.setString(5, status);
            ps.setInt(6, attemptId);
            ps.executeUpdate();
        }
    }

    public List<Result> getStudentResults(String studentId) throws SQLException {
        String sql = "SELECT a.attempt_id, a.student_id, a.exam_id, e.exam_name, " +
                     "a.score, e.total_marks, a.attempted, a.correct, a.wrong, " +
                     "a.start_time, a.end_time, a.status " +
                     "FROM exam_attempts a JOIN exams e ON a.exam_id=e.exam_id " +
                     "WHERE a.student_id=? ORDER BY a.start_time DESC";

        List<Result> results = new ArrayList<>();

        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Timestamp start = rs.getTimestamp("start_time");
                    Timestamp end = rs.getTimestamp("end_time");

                    results.add(new Result(
                            rs.getInt("attempt_id"),
                            rs.getString("student_id"),
                            rs.getInt("exam_id"),
                            rs.getString("exam_name"),
                            rs.getInt("score"),
                            rs.getInt("total_marks"),
                            rs.getInt("attempted"),
                            rs.getInt("correct"),
                            rs.getInt("wrong"),
                            start == null ? null : start.toLocalDateTime(),
                            end == null ? null : end.toLocalDateTime(),
                            rs.getString("status")));
                }
            }
        }
        return results;
    }

    public List<Result> getAllResults() throws SQLException {
        String sql = "SELECT a.attempt_id, a.student_id, a.exam_id, e.exam_name, " +
                     "a.score, e.total_marks, a.attempted, a.correct, a.wrong, " +
                     "a.start_time, a.end_time, a.status " +
                     "FROM exam_attempts a JOIN exams e ON a.exam_id=e.exam_id " +
                     "ORDER BY a.start_time DESC";

        List<Result> results = new ArrayList<>();

        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Timestamp start = rs.getTimestamp("start_time");
                Timestamp end = rs.getTimestamp("end_time");
                results.add(new Result(
                        rs.getInt("attempt_id"), rs.getString("student_id"),
                        rs.getInt("exam_id"), rs.getString("exam_name"),
                        rs.getInt("score"), rs.getInt("total_marks"),
                        rs.getInt("attempted"), rs.getInt("correct"),
                        rs.getInt("wrong"),
                        start == null ? null : start.toLocalDateTime(),
                        end == null ? null : end.toLocalDateTime(),
                        rs.getString("status")));
            }
        }
        return results;
    }
}
