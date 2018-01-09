package app;

import java.io.Serializable;

public class Answers implements Serializable{
        int answersCount;
        String question;
        String answer;

    public Answers(int answersCount, String question, String answer) {
        this.answersCount = answersCount;
        this.question = question;
        this.answer = answer;
    }

    public Answers() {

    }

    public int getAnswersCount() {
        return answersCount;
    }

    public void setAnswersCount(int answersCount) {
        this.answersCount = answersCount;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
