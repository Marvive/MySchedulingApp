package scheduler.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class CustomerScreenController {

    @FXML
    private TableView<?> customerTableView;

    @FXML
    private TableColumn<?, ?> customerNameColumn;

    @FXML
    private TableColumn<?, ?> customerPhoneColumn;

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
    private TextField customerCountryTextField;

    @FXML
    private TextField customerAddress2TextField;

    @FXML
    private TextField customerAddressTextField;

    @FXML
    private TextField customerPhoneTextField;

    @FXML
    private ComboBox<?> customerCityComboBox;

    @FXML
    private Button customerNewButton;

    @FXML
    private Button customerEditButton;

    @FXML
    private Button customerDeleteButton;

    @FXML
    void customerDeleteButtonHandler(ActionEvent event) {

    }

    @FXML
    void customerEditButtonHandler(ActionEvent event) {

    }

    @FXML
    void customerNewButtonHandler(ActionEvent event) {

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