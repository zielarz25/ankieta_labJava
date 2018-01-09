package app.client.controller;

import app.QuestionsAndAnswers;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SurveyPaneController {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private int licznik = 0;            // current record counter
    private int questionId = 0;
    private List<QuestionsAndAnswers> questionsAndAnswersList;
    private List<QuestionsAndAnswers> myAnswers = new ArrayList<>();

    @FXML
    private VBox vBox;

    @FXML
    void initialize() {
        Platform.runLater(()->showQuestions());
    }



    private void showQuestions() {
        // read question and answers
        questionsAndAnswersList = new ArrayList<QuestionsAndAnswers>();

        try {
            // Requests questions from server
            out.writeObject("SHOWQUESTIONS");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
//            System.out.println("trying to read list from stream");
            questionsAndAnswersList = (List<QuestionsAndAnswers>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        showNextQuestion();
    }

    // Shows questions and answers
    void showNextQuestion() {
        if (licznik < questionsAndAnswersList.size() - 1) {
            List<RadioButton> answers = new ArrayList<>();
            ToggleGroup group = new ToggleGroup();

            // spawns question
            Label question = new Label(questionsAndAnswersList.get(licznik).getQuestion());
            question.setFont(Font.font(null, FontWeight.BOLD, 18));
            questionId = questionsAndAnswersList.get(licznik).getQuestionId();
            vBox.getChildren().add(question);

            // sprawns answers
            while (questionsAndAnswersList.get(licznik).getQuestionId() == questionId && licznik < questionsAndAnswersList.size() - 1 ) {

                RadioButton answer = new RadioButton(questionsAndAnswersList.get(licznik).getAnswer());
                answer.setFont(Font.font(null,12));
                answer.setPadding(new Insets(20,0,10,0));
                answer.setId(questionsAndAnswersList.get(licznik).getAnswerId()+"");
                answer.setToggleGroup(group);
                answers.add(answer);

                licznik++;
            }
            questionId++;
            Button button = new Button("OK");
            vBox.getChildren().addAll(answers);
            vBox.getChildren().add(button);

           // cnfirmed answer
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
                       showUserAnswers();
                       licznik = 0;
                       questionId = 0;
                   }
               }
           });
        }
    }

    private void showUserAnswers() {
        Label title = new Label("Twoje odpowiedzi: ");
        title.setFont(Font.font(null, FontWeight.BOLD, 16));
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
            pytanie.setFont(Font.font(null, FontWeight.BOLD, 16));
            pytanie.setPadding(new Insets(10,0,0,0));
            odpowiedz.setFont(Font.font(null,12));
            odpowiedz.setPadding(new Insets(10,0,0,0));

            vBox.getChildren().add(pytanie);
            vBox.getChildren().add(odpowiedz);
        }


    }

    public void setSockets(Socket socket, ObjectInputStream in, ObjectOutputStream out) {

        this.socket = socket;
        this.in = in;
        this.out = out;
        System.out.println("Set socket for survey: " + socket.toString());
    }

}

