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
import scheduler.util.DatabaseConnection;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

import static scheduler.model.AppointmentList.getAppointmentList;
import static scheduler.model.CustomerRoster.getCustomerRoster;
import static scheduler.util.DatabaseConnection.updateCustomerRoster;


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


    private final ObservableList<String> startTimes = FXCollections.observableArrayList();
    private final ObservableList<String> endTimes = FXCollections.observableArrayList();
    private final DateTimeFormatter timeDTF = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
    private final DateTimeFormatter dateDTF = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);

    private ResourceBundle rb1 = ResourceBundle.getBundle("resources/appointmentAddScreen", Locale.getDefault());
    private final ZoneId zoneID = ZoneId.systemDefault();

    /**
     * MenuBar FXML
     * */
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
     * menuBar Handlers
     */
    @FXML
    void menuBarCustomersHandler() {
        try {
            Parent customerParent = FXMLLoader.load(getClass().getResource("CustomerScreen.fxml"));
            Scene customerScene = new Scene(customerParent);
            Stage customerStage = (Stage)  menuBar.getScene().getWindow();
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
            Stage addAppointmentStage = (Stage)  menuBar.getScene().getWindow();
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
            Stage reportsStage = (Stage)  menuBar.getScene().getWindow();
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
            Stage addAppointmentStage = (Stage)  menuBar.getScene().getWindow();
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

    @FXML
    void menuBarMainHandler() {
        try {
            Parent mainParent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene mainScene = new Scene(mainParent);
            Stage mainStage = (Stage) menuBar.getScene().getWindow();
            mainStage.setScene(mainScene);
            mainStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void setLanguage() {
//        Just grabbed the rb info from AddScreen since it's the same!
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

    @FXML
    void cancelButtonHandler(ActionEvent event) {
        ResourceBundle rb = ResourceBundle.getBundle("resources/appointmentEditScreen", Locale.getDefault());
//        Alerts to Confirm the cancel
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle(rb.getString("cancelTitle"));
        alert.setHeaderText(rb.getString("cancelHeader"));
        alert.setContentText(rb.getString("cancelText"));
        Optional<ButtonType> result = alert.showAndWait();
//        If confirmed, go back to customer screen
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


//    Creating appointment initializer
    private Appointment appointment;
//    Grabs index of appointment
    private int appointmentIndexToModify = AppointmentViewScreenController.getAppointmentIndexToEdit();
//    Initialize observableList
    private ObservableList<Customer> currentCustomers = FXCollections.observableArrayList();

//    Update Customer Table View

    private void updateCustomerTableView() {
        updateCustomerRoster();
        customerSelectTableView.setItems(getCustomerRoster());
    }


    /**
     * Saves and updates database
     * */
    @FXML
    private void saveButtonHandler(ActionEvent event) {
        int appointmentId = appointment.getAppointmentId();
        Customer customer1 = customerSelectTableView.getSelectionModel().getSelectedItem();
        String title = appointmentTitleField.getText();
        LocalDate appointmentDate = datePicker.getValue();
        String appointmentType = appointmentTypePicker.getSelectionModel().getSelectedItem();
        String startTime = startTimePicker.getSelectionModel().getSelectedItem();
        String endTime = endTimePicker.getSelectionModel().getSelectedItem();
//        Grabs the strings from the box and converts to localTime
        LocalTime startLocalTime = LocalTime.parse(startTime, timeDTF);
        LocalTime endLocalTime = LocalTime.parse(endTime, timeDTF);
//        Combines Date and local Time
        LocalDateTime startDateTime = LocalDateTime.of(appointmentDate, startLocalTime);
        LocalDateTime endDateTime = LocalDateTime.of(appointmentDate, endLocalTime);
//        Turns LocalDateTime into ZonedDateTime
        ZonedDateTime startUTC = startDateTime.atZone(zoneID).withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime endUTC = endDateTime.atZone(zoneID).withZoneSameInstant(ZoneId.of("UTC"));

//        Submit and check for validation
        String errorMessage = Appointment.isAppointmentValid(customer1, title, appointmentType,
                appointmentDate, startTime, endTime);

//        Checks and alerts for any relevant errors
        if (customer1 == null) {
            ResourceBundle rb = ResourceBundle.getBundle("resources/appointmentAddScreen", Locale.getDefault());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(rb.getString("error"));
            alert.setHeaderText(rb.getString("selectCustomerHeader"));
            alert.setContentText(rb.getString("selectCustomerContent"));
            alert.showAndWait();
            return;
        }

        if (errorMessage.length() > 0) {
            ResourceBundle rb = ResourceBundle.getBundle("resources/appointmentAddScreen", Locale.getDefault());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(rb.getString("error"));
            alert.setHeaderText(rb.getString("errorAddingAppointment"));
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return;
        }

        if (DatabaseConnection.modifyAppointment(appointmentId, customer1, title, startUTC, endUTC)) {
            try {
//                Returns you to ViewScreen
                Parent mainScreenParent = FXMLLoader.load(getClass().getResource("AppointmentViewScreen.fxml"));
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


    /**
     * Sets comboBox Data
     * */
    private void setData(){
//        Sets times available in 15 minutes increments within business hours 8-5
        LocalTime time = LocalTime.of(8, 0);
        do {
            startTimes.add(time.format(timeDTF));
            endTimes.add(time.format(timeDTF));
            time = time.plusMinutes(15);
        } while (!time.equals(LocalTime.of(17, 15)));
        startTimes.remove(startTimes.size() - 1);
//        Removes first index of times since it shouldn't end at opening time
        endTimes.remove(0);

        startTimePicker.setItems(startTimes);
        endTimePicker.setItems(endTimes);

//        Initializes the Type ComboBox
        ObservableList<String> typeList = FXCollections.observableArrayList();
        typeList.addAll(
                rb1.getString("consultation"),
                rb1.getString("followUp"),
                rb1.getString("newAccount"),
                rb1.getString("closeAccount"));
        appointmentTypePicker.setItems(typeList);
    }


    /**
     * Initialize the data from selected item and set language
     * */
    @FXML
    public void initialize() {
//        Sets the local language
        setLanguage();
//        Creates actions for buttons
        saveButton.setOnAction(event -> saveButtonHandler(event));
        cancelButton.setOnAction(event -> cancelButtonHandler(event));

//        Modifies item based on appointment index
        appointment = getAppointmentList().get(appointmentIndexToModify);
//        Grabs information from the selected appointment
        String title = appointment.getTitle();
        Date appointmentDate = appointment.getStartDate();
//        Set LocalDate
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.setTime(appointmentDate);
        int appointmentYear = calendar.get(Calendar.YEAR);
        int appointmentMonth = calendar.get(Calendar.MONTH) + 1;
        int appointmentDay = calendar.get(Calendar.DAY_OF_MONTH);
        LocalDate appointmentLocalDate = LocalDate.of(appointmentYear, appointmentMonth, appointmentDay);

//        Grabbing start string information to set up combobox
        String startString = appointment.getStartString();
        String startInitial = startString.substring(0,8);
        if (startInitial.substring(0,1).equals("0")) {
            startInitial = startInitial.substring(1,8);
        }

        String endString = appointment.getEndString();
        String endInitial = endString.substring(0,8);
        if (endInitial.substring(0,1).equals("0")) {
            endInitial = endInitial.substring(1,8);
        }

//        Prepopulate information into fields
        appointmentTitleField.setText(title);
        datePicker.setValue(appointmentLocalDate);
        startTimePicker.getSelectionModel().select(startInitial);
        endTimePicker.getSelectionModel().select(endInitial);
//        Test to see if it will automatically set the type
        appointmentTypePicker.getSelectionModel().select(appointment.getType());
        setData();
//        Updates table views
        updateCustomerTableView();
//        Sets Data in TableView (Currently fails to load if you go directly to Edit Screen)
//        Not based on when this starts. Suspect it has something to do with the for loop.
        customerColumn.setCellValueFactory(cellData -> cellData.getValue().customerNameProperty());
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

* */