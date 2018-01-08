package server;

import javafx.application.Application;
import javafx.stage.Stage;
import server.utils.DBUtils;

import java.sql.SQLException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
//        primaryStage.setTitle("Hello World");
//        primaryStage.setScene(new Scene(root, 300, 275));
//        primaryStage.show();
    }


    public static void main(String[] args) {
//
        DBUtils.dbConnect();
//
//        DBUtils.dbDisconnect();
        
            DBUtils.fillDatabase();

    }
}
