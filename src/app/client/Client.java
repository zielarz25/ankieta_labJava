package app.client;

import app.QuestionsAndAnswers;
import app.client.utils.FXMLUtils;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
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

    private static final String CLIENT_MAIN_FXML_PATH = "../view/ClientMainPane.fxml";

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLUtils.fxmlLoad(CLIENT_MAIN_FXML_PATH);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();



    }

    public static void main(String[] args) {
        launch(args);
    }




}

