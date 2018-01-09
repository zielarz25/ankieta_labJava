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

        // generate unique id for app client, only with digits, that fits into MySQL BigInt
        String clientId = (String) UUID.randomUUID().toString().replaceAll("[^0-9]", "").subSequence(0,16);

        // checks in loop for client's requests
        while (true) {
            // request from client
            String query = null;

            // read the query
            try {
                query = (String) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
//            System.out.println("Otrzyano zapytanie:  " + query + "z socketu: " + socket.toString());

            // array containing parts of client's query [QUERY:PARAM1:PARAM2]
            String[] queryParts = query.split(":");


            switch (queryParts[0]) {
                // request contains user answers (questionId and answerId)
                // sends answers to dabatase
                // [ODP:questionId:answerId]
                case "ODP" : {
//                    System.out.println("Dodaj do bazy odpowiedz: nr_pyt=" + queryParts[1] + " nr_odp=" + queryParts[2]
//                            + " nr_klienta=" + clientId);
                    QuestionsAndAnswersDAO.insertAnswer(clientId + "", queryParts[1], queryParts[2]);
                    break;
                }
                // resends to client his answers to questions from db
                // [GETMYANSWERS]
                case "GETMYANSWERS" : {
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
                    break;
                }
                // request to send number of questions in survey
                // [GETNUMBEROFQUESTIONS]
                case "GETNUMBEROFQUESTIONS" : {
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
                    break;
                }
                // sends to client number of possible answers for specific question
                // [GETNUMBEROFPOSSIBLEANSWERS:questionId]
                case "GETNUMBEROFPOSSIBLEANSWERS" : {
                    int numberOfPossibleAnswers = 0;
                    try {
                        String questionId = queryParts[1];
                        numberOfPossibleAnswers = ResultsDAO.getNumberOfPossibleAnswers(questionId);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    try {
                        out.writeObject(numberOfPossibleAnswers + "");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                // sends answers stats to generate pieChart
                // [GETANSWERSSTATS:questionId:answerId]
                // sens object<Answers>
                case "GETANSWERSSTATS" : {
                    Answers answers = null;
                    try {
                        String questionId = queryParts[1];
                        String answerId = queryParts[2];
                        answers = ResultsDAO.getAnswersCount(questionId, answerId);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    try {
                        out.writeObject(answers);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                // sends questions and answers to client
                // [SHOWQUESTIONS]
                // sends object<List<QuestionsAndAnswers>>
                case "SHOWQUESTIONS" : {
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

                    break;
                }
                default: {
                    break;
                }
            }
        }
    }
}
