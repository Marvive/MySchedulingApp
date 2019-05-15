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
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

import static scheduler.model.CustomerRoster.getCustomerRoster;
import static scheduler.util.DatabaseConnection.addNewAppointment;
import static scheduler.util.DatabaseConnection.updateCustomerRoster;


/**
 * Primary difference here from the EditScreen is that this will not be autopopulated
 * based on what was selected. This will be a slightly more simple controller
 */

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
    private ComboBox<String> startTimePicker;

    @FXML
    private ComboBox<String> endTimePicker;

    @FXML
    private ComboBox<String> appointmentTypePicker;

    @FXML
    private TableView<Customer> customerSelectTableView;

    @FXML
    private TableColumn<Customer, String> customerColumn;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private MenuBar menuBar;

    @FXML
    private MenuItem menuBarGoReports;

    @FXML
    private MenuItem menuBarGoCustomers;

    @FXML
    private Menu menuBarFile;

    @FXML
    private Menu menuBarMain;

    @FXML
    private MenuItem menuBarGoMain;


    @FXML
    private MenuItem menuBarLogOut;

    @FXML
    private MenuItem menuBarClose;

    @FXML
    private Menu menuBarCustomers;

    @FXML
    private Menu menuBarReports;

    /**
     * Adds data to TableView
     * */
    private void updateCustomerTableView() {
        updateCustomerRoster();
        customerSelectTableView.setItems(getCustomerRoster());
    }

    private final ObservableList<String> startTimes = FXCollections.observableArrayList();
    private final ObservableList<String> endTimes = FXCollections.observableArrayList();
    private final DateTimeFormatter timeDTF = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
    private final DateTimeFormatter dateDTF = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);

    private ResourceBundle rb1 = ResourceBundle.getBundle("resources/appointmentAddScreen", Locale.getDefault());


    /**
     * menuBar Handlers
     */
    @FXML
    void menuBarCustomersHandler() {
        try {
            Parent customerParent = FXMLLoader.load(getClass().getResource("CustomerScreen.fxml"));
            Scene customerScene = new Scene(customerParent);
            Stage customerStage = (Stage) menuBar.getScene().getWindow();
            customerStage.setScene(customerScene);
            customerStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void menuBarLogOutHandler() {
        try {
            Parent addAppointmentParent = FXMLLoader.load(getClass().getResource("Login.fxml"));
            Scene addAppointmentScene = new Scene(addAppointmentParent);
            Stage addAppointmentStage = (Stage) menuBar.getScene().getWindow();
            addAppointmentStage.setScene(addAppointmentScene);
            addAppointmentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void menuBarReportsHandler() {
        try {
            Parent reportsParent = FXMLLoader.load(getClass().getResource("Reports.fxml"));
            Scene reportsScene = new Scene(reportsParent);
            Stage reportsStage = (Stage) menuBar.getScene().getWindow();
            reportsStage.setScene(reportsScene);
            reportsStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void menuBarAppointmentsHandler() {
        try {
            Parent addAppointmentParent = FXMLLoader.load(getClass().getResource("AppointmentViewScreen.fxml"));
            Scene addAppointmentScene = new Scene(addAppointmentParent);
            Stage addAppointmentStage = (Stage) menuBar.getScene().getWindow();
            addAppointmentStage.setScene(addAppointmentScene);
            addAppointmentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void menuBarMainHandler() {
        try {
            Parent addAppointmentParent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene addAppointmentScene = new Scene(addAppointmentParent);
            Stage addAppointmentStage = (Stage) menuBar.getScene().getWindow();
            addAppointmentStage.setScene(addAppointmentScene);
            addAppointmentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void menuBarCloseHandler() {
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

//    ObservableList to hold the customer assigned to the appointment
    private ObservableList<Customer> currentCustomers = FXCollections.observableArrayList();

    @FXML
    void cancelButtonHandler(ActionEvent event) {
        ResourceBundle rb = ResourceBundle.getBundle("resources/appointmentAddScreen", Locale.getDefault());
//        Alerts for the cancel
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle(rb.getString("cancelTitle"));
        alert.setHeaderText(rb.getString("cancelHeader"));
        alert.setContentText(rb.getString("cancelText"));
        Optional<ButtonType> result = alert.showAndWait();
//        If cancel, go back to Appointment Add
        if (result.get() == ButtonType.OK) {
            try {
                Parent mainScreenParent = FXMLLoader.load(getClass().getResource("AppointmentViewScreen.fxml"));
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
     * Saves the information to the tableView and into the database
     */
    @FXML
    void saveButtonHandler(ActionEvent event) {
//        Initializes the Customer
        Customer customer = null;
//        Grabs the name of currentCustomer
        if (currentCustomers.size() == 1) {
            customer = currentCustomers.get(0);
        }
        String title = appointmentTitleField.getText();
        LocalDate appointmentDate = datePicker.getValue();
        String appointmentType = appointmentTypePicker.getSelectionModel().getSelectedItem();
        String startAmPm = startTimePicker.getSelectionModel().getSelectedItem();
        String endAmPm = endTimePicker.getSelectionModel().getSelectedItem();
//        Submit and check for validation
        String errorMessage = Appointment.isAppointmentValid(customer, title,
                appointmentDate, startAmPm, endAmPm);
//        Check and print error message
        if (errorMessage.length() > 0) {
            ResourceBundle rb = ResourceBundle.getBundle("resources/appointmentAddScreen", Locale.getDefault());
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
//        Submit and return to AppointmentViewScreen Checks if it returns true
        if (addNewAppointment(customer, title, startUTC, endUTC)) {
            try {
//                Returns to AppointmentViewScreen if accepted
                Parent mainScreenParent = FXMLLoader.load(getClass().getResource("AppointmentViewScreen.fxml"));
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
     */
    @FXML
    private void setLanguage() {
        ResourceBundle rb = ResourceBundle.getBundle("resources/appointmentAddScreen", Locale.getDefault());
        editAppointmentText.setText(rb.getString("editAppointmentText"));
        AppointmentTitleText.setText(rb.getString("AppointmentTitleText"));
        dateText.setText(rb.getString("dateText"));
        startTimeText.setText(rb.getString("startTimeText"));
        endTimeText.setText(rb.getString("endTimeText"));
        customerColumn.setText(rb.getString("customerColumn"));
        appointmentTypeText.setText(rb.getString("appointmentTypeText"));
        saveButton.setText(rb.getString("saveButton"));
        cancelButton.setText(rb.getString("cancelButton"));

        menuBarLogOut.setText(rb.getString("menuBarLogOut"));
        menuBarClose.setText(rb.getString("menuBarClose"));
        menuBarMain.setText(rb.getString("menuBarMain"));
        menuBarReports.setText(rb.getString("menuBarReports"));
        menuBarFile.setText(rb.getString("menuBarFile"));
        menuBarGoMain.setText(rb.getString("menuBarGoMain"));
        menuBarGoReports.setText(rb.getString("menuBarGoReports"));

        menuBarCustomers.setText(rb.getString("menuBarCustomers"));
        menuBarGoCustomers.setText(rb.getString("menuBarGoCustomers"));
    }

    private void setData(){
//        Sets times available in 15 minutes increments within business hours 8-5
        LocalTime time = LocalTime.of(8, 0);
        do {
            startTimes.add(time.format(timeDTF));
            endTimes.add(time.format(timeDTF));
            time = time.plusMinutes(15);
        } while (!time.equals(LocalTime.of(17, 15)));
        startTimes.remove(startTimes.size() - 1);
        endTimes.remove(0);

        datePicker.setValue(LocalDate.now());

        startTimePicker.setItems(startTimes);
        endTimePicker.setItems(endTimes);
        startTimePicker.getSelectionModel().select(LocalTime.of(8, 0).format(timeDTF));
        endTimePicker.getSelectionModel().select(LocalTime.of(8, 15).format(timeDTF));

//        Initializes the Type ComboBox
        ObservableList<String> typeList = FXCollections.observableArrayList();
        typeList.addAll(
                rb1.getString("consultation"),
                rb1.getString("followUp"),
                rb1.getString("newAccount"),
                rb1.getString("closeAccount"));
        appointmentTypePicker.setItems(typeList);
    }

    @FXML
    public void initialize() {
//        Sets the Language
        setLanguage();
//        Lambdas to assign actions to buttons
        cancelButton.setOnAction(event -> cancelButtonHandler(event));
        saveButton.setOnAction(event -> saveButtonHandler(event));
//        Assigns Data to the Table Column
        updateCustomerTableView();
        customerColumn.setCellValueFactory(cellData -> cellData.getValue().customerNameProperty());
//        Sets all comboBox data
        setData();
    }

}
