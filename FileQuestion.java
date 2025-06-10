package project;
public class FileQuestion {

    private int id;
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String answer;

    public FileQuestion(int id, String question, String option1, String option2, String option3, String option4, String answer) {
        this.id = id;
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.answer = answer;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getOption1() {
        return option1;
    }

    public String getOption2() {
        return option2;
    }

    public String getOption3() {
        return option3;
    }

    public String getOption4() {
        return option4;
    }

    public String getAnswer() {
        return answer;
    }

    // Setters
    public void setQuestion(String question) {
        this.question = question;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }


    @Override
    public String toString() {
        return id + "|" + escape(question) + "|" + escape(option1) + "|" + escape(option2) + "|" +
                escape(option3) + "|" + escape(option4) + "|" + escape(answer);
    }

    private String escape(String s) {
        return s == null ? "" : s.replace("|", "\\|");
    }

    public static FileQuestion fromString(String line) {
        String[] parts = line.split("(?<!\\\\)\\|", -1); // split on unescaped pipe
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].replace("\\|", "|");
        }
        if (parts.length != 7) {
            throw new IllegalArgumentException("Invalid line format for FileQuestion: " + line);
        }
        int id = Integer.parseInt(parts[0]);
        return new FileQuestion(id, parts[1], parts[2], parts[3], parts[4], parts[5], parts[6]);
    }
}

