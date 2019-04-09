package scheduler;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import scheduler.model.*;
import scheduler.view.*;

import java.io.IOException;

public class MySchedulingApp extends Application {
    private Stage primaryStage;

    @Override
//    Initial startup of App, calls showLoginScreen Method
    public void start(Stage primaryStage)  {

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("My Scheduling App");
        showLoginScreen();

//        Right now the path returns null
//        System.out.println(MySchedulingApp.class.getResource("view/Login.fxml"));
    }

    public void showLoginScreen() {
        try {
//            Method to load login screen
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MySchedulingApp.class.getResource("view/Login.fxml"));
            AnchorPane loginScreen = loader.load();

//            Gives the LoginController Access to the main app
            LoginController controller = loader.getController();
            controller.setLogin(this);

//            Show the scene containing the root layout.
            Scene scene = new Scene(loginScreen);
            primaryStage.setScene(scene);
            primaryStage.show();

//            To find any errors
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void showMenu(User currentUser) {
//        Method to show the Menu that can be called from anywhere.
    }

    public void showCustomerScreen(User currentUser) {
//        Method to show the CustomerScreen that can be called from anywhere.
    }

    public void showAppointmentViewScreen(User currentUser) {
//        Method to show the AppointmentViewScreen that can be called from anywhere.
    }

    public void showAppointmentEditScreen(User currentUser) {
//        Method to show the AppointmentEditScreen that can be called from anywhere.
    }

    public void showReports(User currentUser) {
//        Method to show the Reports that can be called from anywhere.
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