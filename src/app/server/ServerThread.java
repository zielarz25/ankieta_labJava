package app.server;

import app.Answers;
import app.QuestionsAndAnswers;
import app.server.model.QuestionsAndAnswersDAO;
import app.server.model.ResultsDAO;
import app.server.utils.DBUtils;
import com.sun.org.apache.xpath.internal.SourceTree;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ServerThread implements Runnable {
    private Socket socket;
    private List<QuestionsAndAnswers> userAnswers = new ArrayList<>();

    public ServerThread(Socket socket) {
        super();
        this.socket = socket;
    }

    @Override
    public void run() {
        ObjectInputStream in = null;
        ObjectOutputStream out = null;
        System.out.println("start app server thread");
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

        // generate unique id for app.client
       // String clientId = (String) UUID.randomUUID().toString().replaceAll("[^0-9]", "").subSequence(0,17);
        String clientId = (String) UUID.randomUUID().toString().replaceAll("[^0-9]", "").subSequence(0,16);


        // odbieraj zapytania od klienta (odpowiedzi lub żądanie wyników)
    while (true) {
        String zapytanie = null;

        try {
            zapytanie = (String) in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Otrzyano zapytanie:  " + zapytanie + "od socketu: " + socket.toString());
        String[] czesci_zapytania = zapytanie.split(":");
//            System.out.println(czesci_zapytania[0]);
//            System.out.println(czesci_zapytania[1]);
//            System.out.println(czesci_zapytania[2]);

        if (czesci_zapytania[0].equals("ODP")) {
            System.out.println("Dodaj do bazy odpowiedz: nr_pyt=" + czesci_zapytania[1] + " nr_odp=" + czesci_zapytania[2]
                    + " nr_klienta=" + clientId);
            QuestionsAndAnswersDAO.insertAnswer(clientId + "", czesci_zapytania[1], czesci_zapytania[2]);
        } else if (czesci_zapytania[0].equals("RESULTS")) {
            System.out.println("Dawaj wyniki");
        } else if (czesci_zapytania[0].equals("GETMYANSWERS")) {
            try {
                userAnswers = QuestionsAndAnswersDAO.qetUserAnswers(clientId + "");
                try {
                    out.writeObject(userAnswers);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else if (czesci_zapytania[0].equals("GETNUMBEROFQUESTIONS")) {
            int numberOfQuestions = 0;
            try {
                numberOfQuestions = ResultsDAO.getNumberOfQuestions();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                out.writeObject(numberOfQuestions + "");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (czesci_zapytania[0].equals("GETNUMBEROFPOSSIBLEANSWERS")) {
            int numberOfPossibleAnswers = 0;
            try {
                String questionId = czesci_zapytania[1];
                numberOfPossibleAnswers = ResultsDAO.getNumberOfPossibleAnswers(questionId);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                out.writeObject(numberOfPossibleAnswers + "");
            } catch (IOException e) {
                e.printStackTrace();
            }


        } else if (czesci_zapytania[0].equals("GETANSWERSSTATS")) {
            Answers answers = null;
            try {
                String questionId = czesci_zapytania[1];
                String answerId = czesci_zapytania[2];
                answers = ResultsDAO.getAnswersCount(questionId, answerId);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                out.writeObject(answers);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (czesci_zapytania[0].equals("SHOWQUESTIONS")) {
            // send list of questions to app.client
            List<QuestionsAndAnswers> questionsAndAnswersList = new ArrayList<QuestionsAndAnswers>();
            try {
                questionsAndAnswersList = QuestionsAndAnswersDAO.getAllQuestionsAndAnswers();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                System.out.println("trying to send object to client: " + clientId);
                out.writeObject(questionsAndAnswersList);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    }
}
