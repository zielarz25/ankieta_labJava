package app.client.controller;

import app.QuestionsAndAnswers;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class SurveyPaneController {
    private static final int serverPort = 45000;
    private Socket socket;

    public void setSockets(Socket socket, ObjectInputStream in, ObjectOutputStream out) {

        this.socket = socket;
        this.in = in;
        this.out = out;
        System.out.println("Set socket for survey: " + socket.toString());
    }

    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;
    private InetAddress address = null;
    @FXML
    private AnchorPane mainPane;

    @FXML
    private VBox vBox;

    private int licznik = 0;
    private int questionId = 0;


    @FXML
    void initialize() {
        Platform.runLater(()->showQuestions());
    }

    private List<QuestionsAndAnswers> questionsAndAnswersList;
    private List<QuestionsAndAnswers> myAnswers = new ArrayList<>();

    private void showQuestions() {
        // create socket from address and port
//        try {
//            // address = InetAddress.getByName("localhost");
//            address = InetAddress.getLocalHost();
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }


//
//        try {
//            in = new ObjectInputStream(socket.getInputStream());
//            out = new ObjectOutputStream(socket.getOutputStream());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        // read question and answers
        questionsAndAnswersList = new ArrayList<QuestionsAndAnswers>();

        try {
            System.out.println("trying to read list from stream");
            questionsAndAnswersList = (List<QuestionsAndAnswers>) in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


////////////////////////////
        //showNextQuestion(1);
        showNextQuestion();


///////////////////


//        try {
//            in.close();
//            out.close();
//            socket.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    // Shows questions and answers
    void showNextQuestion() {
        if (licznik < questionsAndAnswersList.size() - 1) {
            // spawns question
            Label question = new Label(questionsAndAnswersList.get(licznik).getQuestion());
            questionId = questionsAndAnswersList.get(licznik).getQuestionId();
            vBox.getChildren().add(question);

            List<RadioButton> answers = new ArrayList<>();
            ToggleGroup group = new ToggleGroup();
            // sprawns answers
            while (questionsAndAnswersList.get(licznik).getQuestionId() == questionId && licznik < questionsAndAnswersList.size() - 1 ) {

                RadioButton answer = new RadioButton(questionsAndAnswersList.get(licznik).getAnswer());
                answer.setId(questionsAndAnswersList.get(licznik).getAnswerId()+"");
                answer.setToggleGroup(group);
                answers.add(answer);

                licznik++;
            }
            questionId++;
            Button button = new Button("OK");
            vBox.getChildren().addAll(answers);
            vBox.getChildren().add(button);

            // onfirmed answer
           button.setOnAction(new EventHandler<ActionEvent>() {
               @Override public void handle(ActionEvent e) {
                    String odpowiedz = ((RadioButton) group.getSelectedToggle()).getId();
                   System.out.println("Wyslij odpowiedz numer " + odpowiedz +" na pytanie: " + (questionId - 1));

                   try {
                       String zapytanie = "ODP:" + (questionId - 1) + ":" + odpowiedz;
                       System.out.println("Wysłano zapytanie: " + zapytanie);
                       out.writeObject(zapytanie);
                   } catch (IOException e1) {
                       e1.printStackTrace();
                   }
                   vBox.getChildren().clear();
                   if (licznik < questionsAndAnswersList.size() - 1 ) {
                       showNextQuestion();
                   } else {
                       Label label = new Label("Dziękujemy za odpowiedzi");
                       showUserAnswers();
                       vBox.getChildren().add(label);
                       licznik = 0;
                       questionId = 0;
                   }
               }
           });
        }
    }

    private void showUserAnswers() {
        Label title = new Label("Twoje odpowiedzi: ");
        vBox.getChildren().add(title);
        try {
            out.writeObject("GETMYANSWERS");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            myAnswers = (List<QuestionsAndAnswers>) in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        for(int i = 0; i < myAnswers.size(); i++) {
            Label pytanie = new Label(myAnswers.get(i).getQuestion());
            Label odpowiedz = new Label(myAnswers.get(i).getAnswer());
            vBox.getChildren().add(pytanie);
            vBox.getChildren().add(odpowiedz);
        }


    }

}

