import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import view.LoginController;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

public class Main extends Application {
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {


        primaryStage.setTitle("The Scheduling App");

////        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/Login.fxml"));
//
//        primaryStage.setScene(new Scene(root));
//        primaryStage.show();
        showLoginScreen();
    }

    public void showLoginScreen() {
        try {
            // Load Login Screen.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/view/Login.fxml"));
            AnchorPane loginScreen = (AnchorPane) loader.load();

            // Give the controller access to the main app.
            LoginController controller = loader.getController();
            controller.setLogin(this);

            // Show the scene containing the root layout.
            Scene scene = new Scene(loginScreen);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}


/*
Server name:  52.206.157.109
Database name:  U04zW0
Username:  U04zW0
Password:  53688393088
*/