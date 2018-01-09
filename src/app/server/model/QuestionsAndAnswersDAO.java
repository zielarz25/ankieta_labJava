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
            String question = rs.getString("tresc_pytania");
            String answer = rs.getString("tresc_odpowiedzi");

            QuestionsAndAnswers qa = new QuestionsAndAnswers(questionId, answerId, question, answer);
            list.add(qa);
        }
        return list;
    }

    public static void insertAnswer( String userId, String questionId, String answerId){
        String stmt = String.format("INSERT INTO odpowiedzi_uzytkownikow (id_uzytkownika, id_pytania, id_odpowiedzi) " +
                        "VALUES(%s, %s, %s)", userId, questionId, answerId);

        try {
            DBUtils.dbExecuteUpdate(stmt);
            System.out.println("UPDATE: " + stmt);
        } catch (SQLException e) {
            System.out.println("An error occurred while inserting answer: " + e);
            e.printStackTrace();

        }
    }

    public static List<QuestionsAndAnswers> qetUserAnswers(String userId) throws SQLException {

        String stmt = String.format("SELECT tresc_pytania, tresc_odpowiedzi " +
                "FROM odpowiedzi_uzytkownikow NATURAL JOIN pytania NATURAL JOIN" +
                " odpowiedzi WHERE id_uzytkownika = %s",userId);
        ResultSet rs = null;
        try {
            rs= DBUtils.dbExecuteQuery(stmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<QuestionsAndAnswers> list = new ArrayList<QuestionsAndAnswers>();

        while (rs.next()) {
            String question = rs.getString("tresc_pytania");
            String answer = rs.getString("tresc_odpowiedzi");

            QuestionsAndAnswers qa = new QuestionsAndAnswers(question, answer);
            list.add(qa);
        }
        return list;
    }
}
