package app.server.model;

import app.QuestionsAndAnswers;
import app.server.utils.DBUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuestionsAndAnswersDAO {
    public static List<QuestionsAndAnswers> getAllQuestionsAndAnswers() throws SQLException {
        String stmt = String.format("SELECT * from odpowiedzi NATURAL JOIN pytania");
        ResultSet rs = null;
        try {
            rs= DBUtils.dbExecuteQuery(stmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<QuestionsAndAnswers> list = new ArrayList<QuestionsAndAnswers>();

        while (rs.next()) {
            int questionId = rs.getInt("id_pytania");
            int answerId = rs.getInt("id_odpowiedzi");
            String question = rs.getString("tresc_odpowiedzi");
            String answer = rs.getString("tresc_pytania");

            QuestionsAndAnswers qa = new QuestionsAndAnswers(questionId, answerId, question, answer);
            list.add(qa);
        }
        return list;
    }
}
