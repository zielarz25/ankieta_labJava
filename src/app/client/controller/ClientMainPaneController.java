package app.client.controller;

import app.client.utils.FXMLUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ClientMainPaneController {
    private static final String RESULTS_FXML_PATH = "../view/ResultsPane.fxml";
    private static final String SURVEY_FXML_PATH =  "../view/SurveyPane.fxml";

    private Socket socket;
    private InetAddress address;
    private static final int serverPort = 45000;

    @FXML
    private BorderPane mainBorderPane;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    @FXML
    void openResults(ActionEvent event) throws IOException {
        FXMLLoader loader = FXMLUtils.getLoader(RESULTS_FXML_PATH);
        Parent root = loader.load();
        ResultsPaneController controller = loader.getController();
        controller.setSockets(socket, in, out);
        mainBorderPane.setCenter(root);
    }

    @FXML
    void openSurvey(ActionEvent event) throws IOException {
//        Parent root = FXMLUtils.fxmlLoad(SURVEY_FXML_PATH);
        FXMLLoader loader = FXMLUtils.getLoader(SURVEY_FXML_PATH);
        Parent root = loader.load();
        SurveyPaneController controller = loader.getController();
        controller.setSockets(socket, in ,out);
        mainBorderPane.setCenter(root);
    }
    @FXML
    void initialize() {
        try {
            address = InetAddress.getLocalHost();
            socket = new Socket(address, serverPort);
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
