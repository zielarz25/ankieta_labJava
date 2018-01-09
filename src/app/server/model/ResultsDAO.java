package app.server.model;

import app.Answers;
import app.server.utils.DBUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultsDAO {


    public static int getNumberOfQuestions() throws SQLException {
      String stmt = "SELECT count(id_pytania) as 'liczba_pytan' FROM pytania";

        ResultSet rs = null;
        try {
            rs= DBUtils.dbExecuteQuery(stmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int numberOfQuestions = 0;
        while (rs.next()) {
            numberOfQuestions = rs.getInt("liczba_pytan");
        }

      return numberOfQuestions;
    }

    public static int getNumberOfPossibleAnswers(String i) throws SQLException {
      String stmt = String.format("select count(id_odpowiedzi) as 'count' FROM odpowiedzi where id_pytania = %s ",i);

        ResultSet rs = null;
        try {
            rs= DBUtils.dbExecuteQuery(stmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int numberOfPossibleAnswers = 0;
        while (rs.next()) {
            numberOfPossibleAnswers = rs.getInt("count");
        }

      return numberOfPossibleAnswers;
    }
    public static Answers getAnswersCount(String questionId, String answerId) throws SQLException {
      String stmt = String.format("select count(id_uzytkownika) as \"count\", tresc_pytania, tresc_odpowiedzi" +
              " FROM odpowiedzi_uzytkownikow NATURAL JOIN odpowiedzi" +
              " NATURAL JOIN pytania where id_pytania = %s AND id_odpowiedzi = %s",questionId, answerId);

        ResultSet rs = null;
        try {
            rs= DBUtils.dbExecuteQuery(stmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<Answers> answers = new ArrayList<>();
        int answersCount = 0;
        String question = null;
        String answer = null;

        while (rs.next()) {
            answersCount = rs.getInt("count");
            question = rs.getString("tresc_pytania");
            answer = rs.getString("tresc_odpowiedzi");


        }
        Answers a = new Answers(answersCount, question, answer);

      return a;
    }
}
