package scheduler.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import scheduler.MySchedulingApp;
import scheduler.model.User;

import java.io.IOException;
import java.util.Optional;

public class MainScreenController {

    @FXML
    private MenuItem menuBarLogOut;

    @FXML
    private MenuItem menuBarClose;

    @FXML
    private Menu menuBarAppointments;

    @FXML
    private Menu menuBarCustomers;

    @FXML
    private Menu menuBarReports;

    @FXML
    private Button menuAppointmentsButton;

    @FXML
    private Button menuCustomersButton;

    @FXML
    private Button menuReportsButton;

    @FXML
    private Text mainMenuText;


    /**
     * Actions to move to the AppointmentViewScreen
     */

    @FXML
    void menuAppointmentsButtonHandler(ActionEvent event) {
        try {
            Parent addAppointmentParent = FXMLLoader.load(getClass().getResource("AppointmentViewScreen.fxml"));
            Scene addAppointmentScene = new Scene(addAppointmentParent);
            Stage addAppointmentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            addAppointmentStage.setScene(addAppointmentScene);
            addAppointmentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void menuBarAppointmentsHandler(ActionEvent event) {
        try {
            Parent addAppointmentParent = FXMLLoader.load(getClass().getResource("AppointmentViewScreen.fxml"));
            Scene addAppointmentScene = new Scene(addAppointmentParent);
            Stage addAppointmentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            addAppointmentStage.setScene(addAppointmentScene);
            addAppointmentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes Program
     */
    @FXML
    void menuBarCloseHandler(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Exit");
        alert.setHeaderText("Exiting Program!");
        alert.setContentText("Press OK to exit the Program");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            System.exit(0);
        } else {
            System.out.println("Cancelled Exit");
        }
    }


    /**
     * Handler actions for customer screens
     */
    @FXML
    void menuBarCustomersHandler(ActionEvent event) {
        try {
            Parent reportsParent = FXMLLoader.load(getClass().getResource("CustomerScreen.fxml"));
            Scene reportsScene = new Scene(reportsParent);
            Stage reportsStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            reportsStage.setScene(reportsScene);
            reportsStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void menuCustomersButtonHandler(ActionEvent event) {
        try {
            Parent reportsParent = FXMLLoader.load(getClass().getResource("CustomerScreen.fxml"));
            Scene reportsScene = new Scene(reportsParent);
            Stage reportsStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            reportsStage.setScene(reportsScene);
            reportsStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Logs you out
     */
    @FXML
    void menuBarLogOutHandler(ActionEvent event) {
        try {
            Parent addAppointmentParent = FXMLLoader.load(getClass().getResource("Login.fxml"));
            Scene addAppointmentScene = new Scene(addAppointmentParent);
            Stage addAppointmentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            addAppointmentStage.setScene(addAppointmentScene);
            addAppointmentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Handlers for Reports Buttons
     */
    @FXML
    void menuReportsButtonHandler(ActionEvent event) {
        try {
            Parent reportsParent = FXMLLoader.load(getClass().getResource("Reports.fxml"));
            Scene reportsScene = new Scene(reportsParent);
            Stage reportsStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            reportsStage.setScene(reportsScene);
            reportsStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void menuBarReportsHandler(ActionEvent event) {
        try {
            Parent reportsParent = FXMLLoader.load(getClass().getResource("Reports.fxml"));
            Scene reportsScene = new Scene(reportsParent);
            Stage reportsStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            reportsStage.setScene(reportsScene);
            reportsStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private MySchedulingApp mainApp;
    private User currentUser;

    /*
     * Non FXML
     * */
    public void setMain(MySchedulingApp mainApp, User currentUser) {
        this.mainApp = mainApp;
        this.currentUser = currentUser;

//        TODO add a logout option the states the name of the user
//        logoutUser.setText("Logout: " + currentUser.getUsername());
    }

}
