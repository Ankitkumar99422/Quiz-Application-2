package project;

import java.io.*;
import java.util.*;

public class FileQuestionDAO {
    private static final String FILE_NAME = "questions.csv";

    public static void addQuestion(Question q) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(formatQuestion(q));
            writer.newLine();
        }
    }

    public static List<Question> getAllQuestions() throws IOException {
        List<Question> questions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                questions.add(parseQuestion(line));
            }
        }
        return questions;
    }

    public static void updateQuestion(Question updated) throws IOException {
        List<Question> questions = getAllQuestions();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Question q : questions) {
                if (q.getId() == updated.getId()) {
                    writer.write(formatQuestion(updated));
                } else {
                    writer.write(formatQuestion(q));
                }
                writer.newLine();
            }
        }
    }

    public static void deleteQuestion(int id) throws IOException {
        List<Question> questions = getAllQuestions();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Question q : questions) {
                if (q.getId() != id) {
                    writer.write(formatQuestion(q));
                    writer.newLine();
                }
            }
        }
    }

    private static String formatQuestion(Question q) {
        return q.getId() + "," + escape(q.getQuestion()) + "," + escape(q.getOptionA()) + "," +
               escape(q.getOptionB()) + "," + escape(q.getOptionC()) + "," + escape(q.getOptionD()) + "," +
               q.getCorrectOption();
    }

    private static Question parseQuestion(String line) {
        String[] parts = line.split(",", -1);
        return new Question(
            Integer.parseInt(parts[0]),
            unescape(parts[1]),
            unescape(parts[2]),
            unescape(parts[3]),
            unescape(parts[4]),
            unescape(parts[5]),
            parts[6]
        );
    }

    private static String escape(String input) {
        return input.replace(",", "%2C");
    }

    private static String unescape(String input) {
        return input.replace("%2C", ",");
    }
}