package scheduler.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import scheduler.model.Appointment;
import scheduler.model.Customer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import static scheduler.util.DatabaseConnection.addNewAppointment;

/**
 * Primary difference here from the EditScreen is that this will not be autopopulated
 * based on what was selected. This will be a slightly more simple controller
 * */

public class AppointmentAddScreenController {

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

    // ObservableList to hold customers currently assigned to appointment
    private ObservableList<Customer> currentCustomers = FXCollections.observableArrayList();

    @FXML
    void cancelButtonHandler(ActionEvent event) {
        ResourceBundle rb = ResourceBundle.getBundle("AddModifyAppointment", Locale.getDefault());
        // Show alert to confirm cancel
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle(rb.getString("confirmCancel"));
        alert.setHeaderText(rb.getString("confirmCancel"));
        alert.setContentText(rb.getString("confirmCancelAddingMessage"));
        Optional<ButtonType> result = alert.showAndWait();
        // If the 'OK' button is clicked, return to main screen
        if(result.get() == ButtonType.OK) {
            try {
                Parent mainScreenParent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene mainScreenScene = new Scene(mainScreenParent);
                Stage mainScreenStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                mainScreenStage.setScene(mainScreenScene);
                mainScreenStage.show();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void customerSearchButtonHandler(ActionEvent event) {

    }

    @FXML
    void customerSearchField(ActionEvent event) {

    }

    /**
     * Saves the information to the tableview and into the database
     * */
    @FXML
    void saveButtonHandler(ActionEvent event) {
//        Inititializes the Customer
        Customer customer = null;
//        Grabs the name of currentCustomer
        if (currentCustomers.size() == 1) {
            customer = currentCustomers.get(0);
        }
//        Grabs appointment information to set TODO
        String title = appointmentTitleField.getText();
        LocalDate appointmentDate = datePicker.getValue();
//        TODO Figure out how to grab value of combo box
        String startAmPm = startTimePicker.getSelectionModel().getSelectedItem();
        String endAmPm = endTimePicker.getSelectionModel().getSelectedItem();
//        Submit and check for validation
        String errorMessage = Appointment.isAppointmentValid(customer, title,
                appointmentDate, startAmPm, endAmPm);
//        Check and print error message
        if (errorMessage.length() > 0) {
            ResourceBundle rb = ResourceBundle.getBundle("AddModifyAppointment", Locale.getDefault());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(rb.getString("error"));
            alert.setHeaderText(rb.getString("errorAddingAppointment"));
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return;
        }
        SimpleDateFormat localDateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm a");
        localDateFormat.setTimeZone(TimeZone.getDefault());
        Date startLocal = null;
        Date endLocal = null;
////        Change date and time strings into date objects
//        try {
//            startLocal = localDateFormat.parse(appointmentDate.toString() + " " + startHour + ":" + startMinute + " " + startAmPm);
//            endLocal = localDateFormat.parse(appointmentDate.toString() + " " + endHour + ":" + endMinute + " " + endAmPm);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        // Create ZonedDateTime out of Date objects
//        TODO Change combo box selection into UTC
        ZonedDateTime startUTC = ZonedDateTime.ofInstant(startLocal.toInstant(), ZoneId.of("UTC"));
        ZonedDateTime endUTC = ZonedDateTime.ofInstant(endLocal.toInstant(), ZoneId.of("UTC"));
//        Submit and return to main screen. Checks if it returns true
        if (addNewAppointment(customer, title, startUTC, endUTC)) {
            try {
//                Returns to main screen if accepted
                Parent mainScreenParent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene mainScreenScene = new Scene(mainScreenParent);
                Stage mainScreenStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                mainScreenStage.setScene(mainScreenScene);
                mainScreenStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    /**
     * Sets the language of the text on the screen Used in the initialize method
     * TODO
     * */
    @FXML
    private void setLanguage() {
        ResourceBundle rb = ResourceBundle.getBundle("AddModifyAppointment", Locale.getDefault());
        editAppointmentText.setText(rb.getString("lblAddAppointment"));
        AppointmentTitleText.setText(rb.getString("lblTitle"));
        dateText.setText(rb.getString("lblDate"));
        startTimeText.setText(rb.getString("lblStartTime"));
        endTimeText.setText(rb.getString("lblEndTime"));
        customerColumn.setText(rb.getString("lblNameColumn"));
        appointmentTypeText.setText(rb.getString("aptType"));
        customerSearchButton.setText(rb.getString("btnSearch"));
        saveButton.setText(rb.getString("btnSave"));
        cancelButton.setText(rb.getString("btnCancel"));
    }

}
