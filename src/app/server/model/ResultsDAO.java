package app.server.model;

import app.server.utils.DBUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

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
}
