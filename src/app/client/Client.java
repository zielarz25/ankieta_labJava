package app.client;

import app.client.utils.FXMLUtils;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.Serializable;

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

