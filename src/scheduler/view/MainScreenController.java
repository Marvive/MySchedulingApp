package scheduler.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.text.Text;
import scheduler.MySchedulingApp;
import scheduler.model.User;

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

    @FXML
    void menuAppointmentsButtonHandler(ActionEvent event) {

    }

    @FXML
    void menuBarAppointmentsHandler(ActionEvent event) {

    }

    @FXML
    void menuBarCloseHandler(ActionEvent event) {

    }

    @FXML
    void menuBarCustomersHandler(ActionEvent event) {

    }

    @FXML
    void menuBarLogOutHandler(ActionEvent event) {

    }

    @FXML
    void menuBarReportsHandler(ActionEvent event) {

    }

    @FXML
    void menuCustomersButtonHandler(ActionEvent event) {

    }

    @FXML
    void menuReportsButtonHandler(ActionEvent event) {

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
