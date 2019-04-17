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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import static scheduler.model.CustomerRoster.getCustomerRoster;
import static scheduler.util.DatabaseConnection.modifyAppointment;


/**
 * Primary difference here from the EditScreen is that this will not be autopopulated
 * based on what was selected. This will be a slightly more simple controller
 */

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
    private void setLanguage() {
        ResourceBundle rb = ResourceBundle.getBundle("AddModifyAppointment", Locale.getDefault());
        lblModifyAppointment.setText(rb.getString("lblModifyAppointment"));
        lblModifyAppointmentTitle.setText(rb.getString("lblTitle"));
        txtModifyAppointmentTitle.setPromptText(rb.getString("lblTitle"));
        lblModifyAppointmentDescription.setText(rb.getString("lblDescription"));
        txtModifyAppointmentDescription.setPromptText(rb.getString("lblDescription"));
        lblModifyAppointmentLocation.setText(rb.getString("lblLocation"));
        txtModifyAppointmentLocation.setPromptText(rb.getString("lblLocation"));
        lblModifyAppointmentContact.setText(rb.getString("lblContact"));
        txtModifyAppointmentContact.setPromptText(rb.getString("lblContact"));
        lblModifyAppointmentUrl.setText(rb.getString("lblUrl"));
        txtModifyAppointmentUrl.setPromptText(rb.getString("lblUrl"));
        lblModifyAppointmentDate.setText(rb.getString("lblDate"));
        lblModifyAppointmentStartTime.setText(rb.getString("lblStartTime"));
        lblModifyAppointmentEndTime.setText(rb.getString("lblEndTime"));
        tvModifyAppointmentAddNameColumn.setText(rb.getString("lblNameColumn"));
        tvModifyAppointmentAddCityColumn.setText(rb.getString("lblCityColumn"));
        tvModifyAppointmentAddCountryColumn.setText(rb.getString("lblCountryColumn"));
        tvModifyAppointmentAddPhoneColumn.setText(rb.getString("lblPhoneColumn"));
        tvModifyAppointmentDeleteNameColumn.setText(rb.getString("lblNameColumn"));
        tvModifyAppointmentDeleteCityColumn.setText(rb.getString("lblCityColumn"));
        tvModifyAppointmentDeleteCountryColumn.setText(rb.getString("lblCountryColumn"));
        tvModifyAppointmentDeletePhoneColumn.setText(rb.getString("lblPhoneColumn"));
        btnModifyAppointmentAdd.setText(rb.getString("btnAdd"));
        btnModifyAppointmentDelete.setText(rb.getString("btnDelete"));
        btnModifyAppointmentSave.setText(rb.getString("btnSave"));
        btnModifyAppointmentCancel.setText(rb.getString("btnCancel"));
    }

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
        if (result.get() == ButtonType.OK) {
            try {
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


//    Creating appointment initializer
    private Appointment appointment;
//    Grabs index of appointment
    int appointmentIndexToModify = AppointmentViewScreenController.getAppointmentIndexToModify();
//    Initialize observablelist
    private ObservableList<Customer> currentCustomers = FXCollections.observableArrayList();
    @FXML
    void customerSearchButtonHandler(ActionEvent event) {

    }

    @FXML
    void customerSearchField(ActionEvent event) {

    }

//    Update information to database
    @FXML
    private void saveModifyAppointment(ActionEvent event) {
//        Creates customer
        Customer customer = null;
//        Grabs the currentCustomer state
        if (currentCustomers.size() == 1) {
            customer = currentCustomers.get(0);
        }
//        Gets changed information
        int appointmentId = appointment.getAppointmentId();
        String title = txtModifyAppointmentTitle.getText();
        String description = txtModifyAppointmentDescription.getText();
        String location = txtModifyAppointmentLocation.getText();
        String url = txtModifyAppointmentUrl.getText();
        LocalDate appointmentDate = dateModifyAppointmentDate.getValue();
        String startHour = txtModifyAppointmentStartHour.getText();
        String startMinute = txtModifyAppointmentStartMinute.getText();
        String startAmPm = choiceModifyAppointmentStartAMPM.getSelectionModel().getSelectedItem();
        String endHour = txtModifyAppointmentEndHour.getText();
        String endMinute = txtModifyAppointmentEndMinute.getText();
        String endAmPm = choiceModifyAppointmentEndAMPM.getSelectionModel().getSelectedItem();
//        Attempt to submit verifying validity
        String errorMessage = Appointment.isAppointmentValid(customer, title, description, location,
                appointmentDate, startHour, startMinute, startAmPm, endHour, endMinute, endAmPm);
//        Checks and alerts for any relevant errors
        if (errorMessage.length() > 0) {
            ResourceBundle rb = ResourceBundle.getBundle("AddModifyAppointment", Locale.getDefault());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(rb.getString("error"));
            alert.setHeaderText(rb.getString("errorModifyingAppointment"));
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return;
        }
        SimpleDateFormat localDateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm a");
        localDateFormat.setTimeZone(TimeZone.getDefault());
        Date startLocal = null;
        Date endLocal = null;
//        Formats date and time into Date objects
        try {
            startLocal = localDateFormat.parse(appointmentDate.toString() + " " + startHour + ":" + startMinute + " " + startAmPm);
            endLocal = localDateFormat.parse(appointmentDate.toString() + " " + endHour + ":" + endMinute + " " + endAmPm);
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        Create ZonedDateTime out of Date objects
        ZonedDateTime startUTC = ZonedDateTime.ofInstant(startLocal.toInstant(), ZoneId.of("UTC"));
        ZonedDateTime endUTC = ZonedDateTime.ofInstant(endLocal.toInstant(), ZoneId.of("UTC"));
//        Return true if successful update to database
        if (modifyAppointment(appointmentId, customer, title, description, location, url, startUTC, endUTC)) {
            try {
                // Return to appointment summary window
                Parent mainScreenParent = FXMLLoader.load(getClass().getResource("AppointmentSummary.fxml"));
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
     * Initialize the data from selected item and set language
     * */
    @FXML
    public void initialize() {
//        Sets the local language
        setLanguage();
//        Creates actions for buttons
        btnModifyAppointmentAdd.setOnAction(event -> addCustomerToDeleteTableView(event));
        btnModifyAppointmentDelete.setOnAction(event -> deleteCustomerFromDeleteTableView(event));
        btnModifyAppointmentSave.setOnAction(event -> saveModifyAppointment(event));
        btnModifyAppointmentCancel.setOnAction(event -> cancelModifyAppointment(event));
//        Modifies item based on appointment index
        appointment = getAppointmentList().get(appointmentIndexToModify);
//        Grabs information from the selected appointment
        String title = appointment.getTitle();
        String description = appointment.getDescription();
        String location = appointment.getLocation();
        String contact = appointment.getContact();
        String url = appointment.getUrl();
        Date appointmentDate = appointment.getStartDate();
//        Set LocalDate
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.setTime(appointmentDate);
        int appointmentYear = calendar.get(Calendar.YEAR);
        int appointmentMonth = calendar.get(Calendar.MONTH) + 1;
        int appointmentDay = calendar.get(Calendar.DAY_OF_MONTH);
        LocalDate appointmentLocalDate = LocalDate.of(appointmentYear, appointmentMonth, appointmentDay);
//        Make it more readable with AM/PM
        String startString = appointment.getStartString();
        String startHour = startString.substring(0,2);
        if (Integer.parseInt(startHour) < 10) {
            startHour = startHour.substring(1,2);
        }
        String startMinute = startString.substring(3,5);
        String startAmPm = startString.substring(6,8);
        String endString = appointment.getEndString();
        String endHour = endString.substring(0,2);
        if (Integer.parseInt(endHour) < 10) {
            endHour = endHour.substring(1,2);
        }
        String endMinute = endString.substring(3,5);
        String endAmPm = endString.substring(6,8);
        // Get customer to add to currentCustomers via customerId
        int customerId = appointment.getCustomerId();
        ObservableList<Customer> customerRoster = getCustomerRoster();
        for (Customer customer : customerRoster) {
            if (customer.getCustomerId() == customerId) {
                currentCustomers.add(customer);
            }
        }
//        Prepopulate information into fields
        txtModifyAppointmentTitle.setText(title);
        txtModifyAppointmentDescription.setText(description);
        txtModifyAppointmentLocation.setText(location);
        txtModifyAppointmentContact.setText(contact);
        txtModifyAppointmentUrl.setText(url);
        dateModifyAppointmentDate.setValue(appointmentLocalDate);
        txtModifyAppointmentStartHour.setText(startHour);
        txtModifyAppointmentStartMinute.setText(startMinute);
        choiceModifyAppointmentStartAMPM.setValue(startAmPm);
        txtModifyAppointmentEndHour.setText(endHour);
        txtModifyAppointmentEndMinute.setText(endMinute);
        choiceModifyAppointmentEndAMPM.setValue(endAmPm);
//        Lambdas to assign the populated data to table views
        tvModifyAppointmentAddNameColumn.setCellValueFactory(cellData -> cellData.getValue().customerNameProperty());
        tvModifyAppointmentAddCityColumn.setCellValueFactory(cellData -> cellData.getValue().cityProperty());
        tvModifyAppointmentAddCountryColumn.setCellValueFactory(cellData -> cellData.getValue().countryProperty());
        tvModifyAppointmentAddPhoneColumn.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
        tvModifyAppointmentDeleteNameColumn.setCellValueFactory(cellData -> cellData.getValue().customerNameProperty());
        tvModifyAppointmentDeleteCityColumn.setCellValueFactory(cellData -> cellData.getValue().cityProperty());
        tvModifyAppointmentDeleteCountryColumn.setCellValueFactory(cellData -> cellData.getValue().countryProperty());
        tvModifyAppointmentDeletePhoneColumn.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
//        Updates table views
        updateModifyAppointmentAddTableView();
        updateModifyAppointmentDeleteTableView();
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