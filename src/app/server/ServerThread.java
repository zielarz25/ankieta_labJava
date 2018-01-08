package app.server;

import app.QuestionsAndAnswers;
import app.server.model.QuestionsAndAnswersDAO;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ServerThread implements Runnable {
    private Socket socket;

    public ServerThread(Socket socket) {
        super();
        this.socket = socket;
    }

    @Override
    public void run() {
        ObjectInputStream in = null;
        ObjectOutputStream out = null;
        System.out.println("start app.server thread");
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

        // generate unique id for app.client
        String clientId = UUID.randomUUID().toString();

       // send list of questions to app.client
        List<QuestionsAndAnswers> questionsAndAnswersList = new ArrayList<QuestionsAndAnswers>();
        try {
            questionsAndAnswersList = QuestionsAndAnswersDAO.getAllQuestionsAndAnswers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            System.out.println("trying to send object");
            out.writeObject(questionsAndAnswersList);
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        // wait for response


    }
}
