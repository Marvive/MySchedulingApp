package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class AppointmentEditScreenController {

    @FXML
    private Text editAppointmentText;

    @FXML
    private Label AppointmentTitleText;

    @FXML
    private Label appointmentTypeText;

    @FXML
    private Label startTimeText;

    @FXML
    private Label endTimeText;

    @FXML
    private Label dateText;

    @FXML
    private TextField appointmentTitleField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ComboBox<?> startTimePicker;

    @FXML
    private ComboBox<?> endTimePicker;

    @FXML
    private ComboBox<?> appointmentTypePicker;

    @FXML
    private Button customerSearchButton;

    @FXML
    private TableView<?> customerSelectTableView;

    @FXML
    private TableColumn<?, ?> customerColumn;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    void cancelButtonHandler(ActionEvent event) {

    }

    @FXML
    void customerSearchButtonHandler(ActionEvent event) {

    }

    @FXML
    void customerSearchField(ActionEvent event) {

    }

    @FXML
    void saveButtonHandler(ActionEvent event) {

    }

}


/*
* C. Provide the ability to add, update, and delete appointments,
 * capturing the type of appointment and a link to the
  * specific customer record in the database.
  *
  * E. Provide the ability to automatically adjust appointment times
   * based on user time zones and daylight saving time.
   *
   *
   * F. Write exception controls to prevent each of the following. You may use the same mechanism of exception control more than once, but you must incorporate at least two different mechanisms of exception control.

• scheduling an appointment outside business hours

• scheduling overlapping appointments

• entering nonexistent or invalid customer data

• entering an incorrect username and password
* */