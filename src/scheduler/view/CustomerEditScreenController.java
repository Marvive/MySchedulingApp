package scheduler.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

public class CustomerEditScreenController {

    @FXML
    private MenuBar menuBar;

    @FXML
    private MenuItem menuBarLogOut;

    @FXML
    private MenuItem menuBarClose;

    @FXML
    private Menu menuBarMain;

    @FXML
    private Menu menuBarAppointments;

    @FXML
    private Menu menuBarReports;

    @FXML
    private Text customerScreenText;

    @FXML
    private Label customerIDLabel;

    @FXML
    private Label customerAddressLabel;

    @FXML
    private Label customerNameLabel;

    @FXML
    private Label customerCountryLabel;

    @FXML
    private Label customerCityLabel;

    @FXML
    private Label customerAddressLabel2;

    @FXML
    private Label customerPhoneNumber;

    @FXML
    private Label customerPostalCodeLabel;

    @FXML
    private TextField customerIDTextField;

    @FXML
    private TextField customerNameTextField;

    @FXML
    private TextField customerPostalCode;

    @FXML
    private TextField customerCountryTextField;

    @FXML
    private TextField customerAddress2TextField;

    @FXML
    private TextField customerAddressTextField;

    @FXML
    private TextField customerPhoneTextField;

    @FXML
    private ComboBox<String> customerCityComboBox;

    @FXML
    private Button customerAddSaveButton;

    @FXML
    private Button customerAddCancelButton;

    @FXML
    void customerAddCancelHandler(ActionEvent event) {

    }

    @FXML
    void customerAddSaveHandler(ActionEvent event) {

    }

    @FXML
    void menuBarAppointmentsHandler(ActionEvent event) {

    }

    @FXML
    void menuBarCloseHandler(ActionEvent event) {

    }

    @FXML
    void menuBarLogOutHandler(ActionEvent event) {

    }

    @FXML
    void menuBarMainHandler(ActionEvent event) {

    }

    @FXML
    void menuBarReportsHandler(ActionEvent event) {

    }

}


/*
Provide the ability to add, update, and delete customer records
 in the database, including name, address, and phone number.

 F. Write exception controls to prevent each of the following.
  You may use the same mechanism of exception control more than once,
  but you must incorporate at least two different mechanisms of exception control.

• scheduling an appointment outside business hours

• scheduling overlapping appointments

• entering nonexistent or invalid customer data

• entering an incorrect username and password
* */