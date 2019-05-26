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
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import static scheduler.model.CustomerList.getCustomerList;
import static scheduler.util.DatabaseConnection.updateCustomerList;


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
     * Adds data to TableView
     * */
    private void updateCustomerTableView() {
        updateCustomerList();
        customerSelectTableView.setItems(getCustomerList());
    }

    private final ObservableList<String> startTimes = FXCollections.observableArrayList();
    private final ObservableList<String> endTimes = FXCollections.observableArrayList();
    private final DateTimeFormatter timeDTF = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
    private final DateTimeFormatter dateDTF = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);

    private ResourceBundle rb1 = ResourceBundle.getBundle("resources/appointmentAddScreen", Locale.getDefault());
    private final ZoneId zoneID = ZoneId.systemDefault();
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
    private void saveButtonHandler(ActionEvent event) {
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
//        Check and print error message
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

        if (DatabaseConnection.addNewAppointment(customer1, title, appointmentType, startUTC, endUTC)) {
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
//        Sets Data in TableView
        customerColumn.setCellValueFactory(cellData -> cellData.getValue().customerNameProperty());
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
//        Sets all comboBox data
        setData();
//        Defaults to first option in Type comboBox
        appointmentTypePicker.getSelectionModel().selectFirst();
//        Defaults to today for date comboBox
        datePicker.setValue(LocalDate.now());
    }

}
