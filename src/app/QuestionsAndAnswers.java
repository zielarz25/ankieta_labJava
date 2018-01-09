package app;

import java.io.Serializable;

public class QuestionsAndAnswers implements Serializable {
    private int questionId;
    private int answerId;
    private String question;
    private String answer;


    public QuestionsAndAnswers(int questionId, int answerId, String question, String answer) {
        this.questionId = questionId;
        this.answerId = answerId;
        this.question = question;
        this.answer = answer;
    }

    public QuestionsAndAnswers(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public QuestionsAndAnswers() {

    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
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

    @Override
    public String toString() {
        return "QuestionsAndAnswers{" +
                "questionId=" + questionId +
                ", answerId=" + answerId +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}

