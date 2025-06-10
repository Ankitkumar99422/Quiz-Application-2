package project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionDAO {

    private Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:quiz.db");
    }

    public void addQuestion(Question q) {
        String sql = "INSERT INTO questions (id, question, optionA, optionB, optionC, optionD, correctOption) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, q.getId());
            pstmt.setString(2, q.getQuestion());
            pstmt.setString(3, q.getOptionA());
            pstmt.setString(4, q.getOptionB());
            pstmt.setString(5, q.getOptionC());
            pstmt.setString(6, q.getOptionD());
            pstmt.setString(7, q.getCorrectOption());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateQuestion(Question q) {
        String sql = "UPDATE questions SET question=?, optionA=?, optionB=?, optionC=?, optionD=?, correctOption=? WHERE id=?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, q.getQuestion());
            pstmt.setString(2, q.getOptionA());
            pstmt.setString(3, q.getOptionB());
            pstmt.setString(4, q.getOptionC());
            pstmt.setString(5, q.getOptionD());
            pstmt.setString(6, q.getCorrectOption());
            pstmt.setInt(7, q.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteQuestion(int id) {
        String sql = "DELETE FROM questions WHERE id=?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Question> getAllQuestions() {
        List<Question> list = new ArrayList<>();
        String sql = "SELECT * FROM questions";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Question q = new Question(
                        rs.getInt("id"),
                        rs.getString("question"),
                        rs.getString("optionA"),
                        rs.getString("optionB"),
                        rs.getString("optionC"),
                        rs.getString("optionD"),
                        rs.getString("correctOption")
                );
                list.add(q);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
