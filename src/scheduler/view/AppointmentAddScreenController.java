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
import scheduler.model.Appointment;
import scheduler.model.Customer;

import java.io.IOException;
import java.text.ParseException;
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
        String title = txtAddAppointmentTitle.getText();
        String description = txtAddAppointmentDescription.getText();
        String location = txtAddAppointmentLocation.getText();
        String contact = txtAddAppointmentContact.getText();
        // If contact field has been left empty, fill with customers name and phone
        if (contact.length() == 0 && customer != null) {
            contact = customer.getCustomerName() + ", " + customer.getPhone();
        }
        String url = txtAddAppointmentUrl.getText();
        LocalDate appointmentDate = dateAddAppointmentDate.getValue();
        String startHour = txtAddAppointmentStartHour.getText();
        String startMinute = txtAddAppointmentStartMinute.getText();
        String startAmPm = choiceAddAppointmentStartAMPM.getSelectionModel().getSelectedItem();
        String endHour = txtAddAppointmentEndHour.getText();
        String endMinute = txtAddAppointmentEndMinute.getText();
        String endAmPm = choiceAddAppointmentEndAMPM.getSelectionModel().getSelectedItem();
        // Submit appointment information for validation
        String errorMessage = Appointment.isAppointmentValid(customer, title, description, location,
                appointmentDate, startHour, startMinute, startAmPm, endHour, endMinute, endAmPm);
        // Check if errorMessage contains anything
        if (errorMessage.length() > 0) {
            ResourceBundle rb = ResourceBundle.getBundle("AddModifyAppointment", Locale.getDefault());
//            Alerts with the error message
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
        // Format date and time strings into Date objects
        try {
            startLocal = localDateFormat.parse(appointmentDate.toString() + " " + startHour + ":" + startMinute + " " + startAmPm);
            endLocal = localDateFormat.parse(appointmentDate.toString() + " " + endHour + ":" + endMinute + " " + endAmPm);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Create ZonedDateTime out of Date objects
        ZonedDateTime startUTC = ZonedDateTime.ofInstant(startLocal.toInstant(), ZoneId.of("UTC"));
        ZonedDateTime endUTC = ZonedDateTime.ofInstant(endLocal.toInstant(), ZoneId.of("UTC"));
        // Submit information to be added to database. Check if 'true' is returned
        if (addNewAppointment(customer, title, description, location, contact, url, startUTC, endUTC)) {
            try {
                // Return to main screen
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
        lblAddAppointment.setText(rb.getString("lblAddAppointment"));
        lblAddAppointmentTitle.setText(rb.getString("lblTitle"));
        txtAddAppointmentTitle.setPromptText(rb.getString("lblTitle"));
        lblAddAppointmentDescription.setText(rb.getString("lblDescription"));
        txtAddAppointmentDescription.setPromptText(rb.getString("lblDescription"));
        lblAddAppointmentLocation.setText(rb.getString("lblLocation"));
        txtAddAppointmentLocation.setPromptText(rb.getString("lblLocation"));
        lblAddAppointmentContact.setText(rb.getString("lblContact"));
        txtAddAppointmentContact.setPromptText(rb.getString("lblContact"));
        lblAddAppointmentUrl.setText(rb.getString("lblUrl"));
        txtAddAppointmentUrl.setPromptText(rb.getString("lblUrl"));
        lblAddAppointmentDate.setText(rb.getString("lblDate"));
        lblAddAppointmentStartTime.setText(rb.getString("lblStartTime"));
        lblAddAppointmentEndTime.setText(rb.getString("lblEndTime"));
        tvAddAppointmentAddNameColumn.setText(rb.getString("lblNameColumn"));
        tvAddAppointmentAddCityColumn.setText(rb.getString("lblCityColumn"));
        tvAddAppointmentAddCountryColumn.setText(rb.getString("lblCountryColumn"));
        tvAddAppointmentAddPhoneColumn.setText(rb.getString("lblPhoneColumn"));
        tvAddAppointmentDeleteNameColumn.setText(rb.getString("lblNameColumn"));
        tvAddAppointmentDeleteCityColumn.setText(rb.getString("lblCityColumn"));
        tvAddAppointmentDeleteCountryColumn.setText(rb.getString("lblCountryColumn"));
        tvAddAppointmentDeletePhoneColumn.setText(rb.getString("lblPhoneColumn"));
        btnAddAppointmentAdd.setText(rb.getString("btnAdd"));
        btnAddAppointmentDelete.setText(rb.getString("btnDelete"));
        btnAddAppointmentSave.setText(rb.getString("btnSave"));
        btnAddAppointmentCancel.setText(rb.getString("btnCancel"));
    }

}
