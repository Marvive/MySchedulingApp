package scheduler;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MySchedulingApp extends Application {
    private Stage primaryStage;

    @Override
//    Initial startup of App, calls showLoginScreen Method
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setResizable(false);
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("My Scheduling App");
        Parent root = FXMLLoader.load(getClass().getResource("view/Login.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}

/**
Server name:  52.206.157.109
Database name:  U04zW0
Username:  U04zW0
Password:  53688393088
*/