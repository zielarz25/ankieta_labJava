package app.client;

import app.QuestionsAndAnswers;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Client extends Application implements Serializable {
    private static final int serverPort = 45000;

    @Override
    public void start(Stage primaryStage) throws Exception {

    }

    public static void main(String[] args) {

        ObjectInputStream in = null;
        ObjectOutputStream out = null;

        // create socket from address and port
        InetAddress address = null;
        try {
           // address = InetAddress.getByName("localhost");
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        Socket socket = null;
        try {
            socket = new Socket(address,serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // read question and answers
        List<QuestionsAndAnswers> questionsAndAnswersList = new ArrayList<QuestionsAndAnswers>();

        try {
            System.out.println("trying to read list from stream");
            questionsAndAnswersList = (List<QuestionsAndAnswers>) in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println(questionsAndAnswersList.get(0).getQuestion());

        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

      //  launch(args);
    }
}
