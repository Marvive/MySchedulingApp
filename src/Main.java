import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.net.URLClassLoader;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
//        ClassLoader cl = ClassLoader.getSystemClassLoader();
//
//        URL[] urls = ((URLClassLoader)cl).getURLs();
//
//        for(URL url: urls){
//            System.out.println(url.getFile());
//        }
//        FXMLLoader frmLogIn = new FXMLLoader(getClass().getClassLoader().getResource("/view/Login.fxml"));
//        Parent login = frmLogIn.load();

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/Login.fxml"));
        primaryStage.setTitle("The Scheduling App");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
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