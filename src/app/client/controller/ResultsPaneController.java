package app.client.controller;

import app.Answers;
import app.QuestionsAndAnswers;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
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

public class ResultsPaneController {
    @FXML
    private VBox vBox;

    private int licznik = 0;
    private int questionId = 0;
    private List<QuestionsAndAnswers> questionsAndAnswers = new ArrayList<>();
    private int numberOfQuestions;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    @FXML
    void initialize() throws IOException, ClassNotFoundException {

        Platform.runLater(()-> {
            try {
                showResults();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        });


    }

    private void showResults() throws IOException, ClassNotFoundException {

        numberOfQuestions = getNumberOfQuestions();
        for (int i = 0; i < numberOfQuestions; i++) {

            int numberOfPossibleAnswers = getNumberOfPossibleAnswers(i+1);
            System.out.println("Liczba odpowiedzi na pytanie: " + (i+1) + ": " + numberOfPossibleAnswers);
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            Answers answer = null;
            PieChart chart = new PieChart();
            for (int j = 0; j < numberOfPossibleAnswers; j++) {

                answer = getAnswersStats(i+1, j+1);

                pieChartData.add(new PieChart.Data(answer.getAnswer() + "(" + answer.getAnswersCount() + ")"
                        , answer.getAnswersCount()));

            }

            chart.setData(pieChartData);
            pieChartData.removeIf(data -> data.getPieValue() == 0);
            chart.setId(i+"");
            chart.setPrefSize(600,600);
            chart.setTitle(answer.getQuestion());
            vBox.getChildren().add(chart);

        }
    }

    private Answers getAnswersStats(int questionId, int answerId) throws IOException, ClassNotFoundException {
        out.writeObject("GETANSWERSSTATS:"+ questionId + ":" + answerId);
        Answers ret = (Answers) in.readObject();
        return ret;
    }

    private int getNumberOfPossibleAnswers(int i) throws IOException, ClassNotFoundException {
        out.writeObject("GETNUMBEROFPOSSIBLEANSWERS:"+i);
        String ret = (String)in.readObject();
        return Integer.valueOf(ret) ;
    }

    private int getNumberOfQuestions() throws IOException, ClassNotFoundException {
        out.writeObject("GETNUMBEROFQUESTIONS");
        String ret = (String)in.readObject();
        return Integer.valueOf(ret) ;
    }




    public void setSockets(Socket socket, ObjectInputStream in, ObjectOutputStream out) {
        this.socket = socket;
        this.in = in;
        this.out = out;
    }
}

