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
import scheduler.model.AppointmentList;
import scheduler.util.DatabaseConnection;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import static java.time.LocalDate.now;
import static scheduler.model.AppointmentList.getAppointmentList;

public class AppointmentViewScreenController {

    @FXML
    private TableView<Appointment> appointmentTableView;

    @FXML
    private TableColumn<Appointment, String> titleColumn;

    @FXML
    private TableColumn<Appointment, String> typeColumn;

    @FXML
    private TableColumn<Appointment, String> startColumn;

    @FXML
    private TableColumn<Appointment, String> endColumn;

    @FXML
    private TableColumn<Appointment, String> customerColumn;

    @FXML
    private TableColumn<Appointment, String> dateColumn;

    @FXML
    private Button deleteAppointmentButton;

    @FXML
    private Button editAppointmentButton;

    @FXML
    private Button newAppointmentButton;

    @FXML
    private Text appointmentViewTitle;

    /**
     * MenuBar fxml
     */

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
     * Handler actions for menuBar
     */
    @FXML
    void menuBarCustomersHandler(ActionEvent event) {
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
    void menuBarCloseHandler() {
        ResourceBundle rb = ResourceBundle.getBundle("resources/MainScreen", Locale.getDefault());
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle(rb.getString("exitTitle"));
        alert.setHeaderText(rb.getString("exitHeader"));
        alert.setContentText(rb.getString("exitContent"));
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            System.exit(0);
        } else {
            System.out.println(rb.getString("cancelledExit"));
        }
    }

    private ResourceBundle rb1 = ResourceBundle.getBundle("resources/appointmentViewScreen", Locale.getDefault());

    @FXML
    private ComboBox<String> comboBox;

    /**
     * Sets that Data in the combo box
     * Used in initialize
     */
    private void setData() {
        DatabaseConnection.updateAppointmentList();
//        for (Appointment appointment : AppointmentList.getAppointmentList()) {
//            System.out.println("StartString " + appointment.getStartString());
//            System.out.println("StartDate " + appointment.getStartDate());
//            System.out.println("TimeStamp " + appointment.getStartTimestamp());
//            System.out.println("------");
//        }
        comboBox.getItems().clear();
        comboBox.getItems().addAll(
                rb1.getString("allView"),
                rb1.getString("weekView"),
                rb1.getString("monthView")
        );
//        Selects the first option so that comboBox will not be blank
        comboBox.getSelectionModel().selectFirst();
//        Lambda that sets an addListener on the comboBox options.
//        Better organized placing in line with the rest of the combobox data.
        comboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
                    if (newValue.equals(rb1.getString("allView"))) {
                        DatabaseConnection.updateAppointmentList();
                        appointmentTableView.setItems(getAppointmentList());
                    } else if (newValue.equals(rb1.getString("weekView"))) {
                        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
                        for (Appointment appointment : AppointmentList.getAppointmentList()
                        ) {
                            if (appointment.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isBefore(now().plusDays(7))) {
                                appointmentList.addAll(appointment);
                            }
                            appointmentTableView.setItems(appointmentList);
                        }
                    } else if (newValue.equals(rb1.getString("monthView"))) {
                        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
                        for (Appointment appointment : AppointmentList.getAppointmentList()
                        ) {
                            if (appointment.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isBefore(now().plusDays(31))) {
                                appointmentList.addAll(appointment);
                            }
                            appointmentTableView.setItems(appointmentList);
                        }
                    }
                }
        );


    }


    @FXML
    void newAppointmentHandler(ActionEvent event) {
        try {
            Parent addAppointmentParent = FXMLLoader.load(getClass().getResource("AppointmentAddScreen.fxml"));
            Scene addAppointmentScene = new Scene(addAppointmentParent);
            Stage addAppointmentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            addAppointmentStage.setScene(addAppointmentScene);
            addAppointmentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //    Variable to hold appointment index to edit
    private static int appointmentIndexToEdit;

    /**
     * Set's language for all the text that can appear on the screen
     * Used in initialize
     */
    @FXML
    private void setLanguage() {
        ResourceBundle rb = ResourceBundle.getBundle("resources/appointmentViewScreen", Locale.getDefault());
        titleColumn.setText(rb.getString("titleColumn"));
        typeColumn.setText(rb.getString("typeColumn"));
        startColumn.setText(rb.getString("startColumn"));
        endColumn.setText(rb.getString("endColumn"));
        customerColumn.setText(rb.getString("customerColumn"));
        dateColumn.setText(rb.getString("dateColumn"));
        editAppointmentButton.setText(rb.getString("editAppointmentButton"));
        deleteAppointmentButton.setText(rb.getString("deleteAppointmentButton"));
        newAppointmentButton.setText(rb.getString("newAppointmentButton"));
        appointmentViewTitle.setText(rb.getString("appointmentViewTitle"));
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
    private void editAppointmentHandler(ActionEvent event) {
//        Grab selected appointment to edit
        Appointment appointmentToEdit = appointmentTableView.getSelectionModel().getSelectedItem();
//        Ensures something is selected before editing
        if (appointmentToEdit == null) {
            ResourceBundle rb = ResourceBundle.getBundle("resources/appointmentViewScreen", Locale.getDefault());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(rb.getString("errorTitle"));
            alert.setHeaderText(rb.getString("errorEditAppointment"));
            alert.setContentText(rb.getString("errorSelectAppointment"));
            alert.showAndWait();
            return;
        }
//        Set the index of the appointment to edit
        appointmentIndexToEdit = AppointmentList.getAppointmentList().indexOf(appointmentToEdit);
//        Try to open the appointmentEditScreen
        try {
            Parent modifyAppointmentParent = FXMLLoader.load(getClass().getResource("AppointmentEditScreen.fxml"));
            Scene modifyAppointmentScene = new Scene(modifyAppointmentParent);
            Stage modifyAppointmentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            modifyAppointmentStage.setScene(modifyAppointmentScene);
            modifyAppointmentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes the selected appointment.
     */
    @FXML
    private void deleteAppointmentHandler(ActionEvent event) {
//        Grabs selected appointment from Table View
        Appointment appointmentToDelete = appointmentTableView.getSelectionModel().getSelectedItem();
//        Check if one was selected then alert if not
        if (appointmentToDelete == null) {
            ResourceBundle rb = ResourceBundle.getBundle("resources/appointmentViewScreen", Locale.getDefault());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(rb.getString("errorTitle"));
            alert.setHeaderText(rb.getString("errorDeleteHeader"));
            alert.setContentText(rb.getString("errorDeleteContent"));
            alert.showAndWait();
        } else {
//        Submits the appointment to be deleted
            DatabaseConnection.deleteAppointment(appointmentToDelete);
        }
    }


    //    Returns appointment index to Edit
    public static int getAppointmentIndexToEdit() {
        return appointmentIndexToEdit;
    }

    //    Updates the TableView
    @FXML
    private void updateAddAppointmentTableView() {
        DatabaseConnection.updateAppointmentList();
        appointmentTableView.setItems(getAppointmentList());
    }

    @FXML
    private void setTableView() {
//        Lambda is used here to populate the tableView. Much easier than not using a lambda.
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        startColumn.setCellValueFactory(cellData -> cellData.getValue().startStringProperty());
        endColumn.setCellValueFactory(cellData -> cellData.getValue().endStringProperty());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateStringProperty());
        customerColumn.setCellValueFactory(cellData -> cellData.getValue().contactProperty());
    }

    /**
     * Grabs information to initialize screen and calls set language
     */
    public void initialize() {
        setLanguage();
//        Lambdas to call methods on buttons. More efficient than manipulating files/
        newAppointmentButton.setOnAction(event -> newAppointmentHandler(event));
        editAppointmentButton.setOnAction(event -> editAppointmentHandler(event));
        deleteAppointmentButton.setOnAction(event -> deleteAppointmentHandler(event));
//        Updates the Table View When initialized
        updateAddAppointmentTableView();
//        Sets the table view columns
        setTableView();
//        Sets Data for combobox
        setData();
    }

}





/*
 *
 * this should likely have an edit button that moves
 * to the appointment edit screen
 *
 *
 * D. Provide the ability to view the calendar by month and by week.
 *
 * */