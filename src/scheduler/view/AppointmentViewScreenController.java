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
import scheduler.util.DatabaseConnection;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import static scheduler.model.AppointmentList.getAppointmentList;
import static scheduler.util.DatabaseConnection.updateAppointmentList;

public class AppointmentViewScreenController {

    @FXML
    private TableView<Appointment> apptTableView;

    @FXML
    private TableColumn<Appointment, String> titleColumn;

    @FXML
    private TableColumn<Appointment, String> typeColumn;

    @FXML
    private TableColumn<Appointment, LocalDateTime> startColumn;

    @FXML
    private TableColumn<Appointment, LocalDateTime> endColumn;

    @FXML
    private TableColumn<Appointment, String> customerColumn;

    @FXML
    private TableColumn<Appointment, String> consultantColumn;

    @FXML
    private Button deleteAppointmentButton;

    @FXML
    private Button editAppointmentButton;

    @FXML
    private Button newAppointmentButton;

    @FXML
    private Text appointmentViewTitle;

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
    void menuBarMainHandler() {
        try {
            Parent mainParent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene mainScene = new Scene(mainParent);
            Stage mainStage = (Stage)  menuBar.getScene().getWindow();
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



    private ResourceBundle rb1 = ResourceBundle.getBundle("resources/appointmentViewScreen", Locale.getDefault());

    @FXML
    private ComboBox comboBox;

    /**
     * Sets that Data in the combo box
     * Used in initialize
     * */
    private void setData() {
        comboBox.getItems().clear();
        comboBox.getItems().addAll(
                rb1.getString("allView"),
                rb1.getString("weekView"),
                rb1.getString("monthView")
        );

    }


    @FXML
    void handleNewAppt(ActionEvent event) {
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

//    Variable to hold index of appointment to modify
    private static int appointmentIndexToModify;

    /**
     * Set's language for all the text that can appear on the screen
     */
    @FXML
    private void setLanguage() {
        ResourceBundle rb = ResourceBundle.getBundle("resources/appointmentViewScreen", Locale.getDefault());
        titleColumn.setText(rb.getString("titleColumn"));
        typeColumn.setText(rb.getString("typeColumn"));
        startColumn.setText(rb.getString("startColumn"));
        endColumn.setText(rb.getString("endColumn"));
        customerColumn.setText(rb.getString("customerColumn"));
        consultantColumn.setText(rb.getString("consultantColumn"));
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
    private void handleEditAppt(ActionEvent event) {
//        Grab selected appointment to modify
        Appointment appointmentToModify = apptTableView.getSelectionModel().getSelectedItem();
//        Ensures something is selected before editing
        if (appointmentToModify == null) {
            ResourceBundle rb = ResourceBundle.getBundle("resources/appointmentViewScreen", Locale.getDefault());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(rb.getString("error"));
            alert.setHeaderText(rb.getString("errorEditAppointment"));
            alert.setContentText(rb.getString("errorSelectAppointment"));
            alert.showAndWait();
            return;
        }
        // Set the index of the appointment that was selected to be modified
        appointmentIndexToModify = getAppointmentList().indexOf(appointmentToModify);
        // Open modify appointment window
        try {
            Parent modifyAppointmentParent = FXMLLoader.load(getClass().getResource("ModifyAppointment.fxml"));
            Scene modifyAppointmentScene = new Scene(modifyAppointmentParent);
            Stage modifyAppointmentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            modifyAppointmentStage.setScene(modifyAppointmentScene);
            modifyAppointmentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Delete the selected appointment
    @FXML
    private void handleDeleteAppt(ActionEvent event) {
        // Get the selected appointment from the table view
        Appointment appointmentToDelete = apptTableView.getSelectionModel().getSelectedItem();
        // Check if no appointment was selected if
        if (appointmentToDelete == null) {
            // Show alert saying an appointment must be selected to delete
            ResourceBundle rb = ResourceBundle.getBundle("resources/appointmentViewScreen", Locale.getDefault());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(rb.getString("error"));
            alert.setHeaderText(rb.getString("errorDeletingAppointment"));
            alert.setContentText(rb.getString("errorDeletingAppointmentMessage"));
            alert.showAndWait();
            return;
        }
        // Submit appointment to be deleted
        DatabaseConnection.deleteAppointment(appointmentToDelete);
    }


    /**
     * Go back to main Screen
     */
    @FXML
    private void exit(ActionEvent event) {
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

    // Return the appointment index to be modified
    public static int getAppointmentIndexToModify() {
        return appointmentIndexToModify;
    }

    // Update the table view
    @FXML
    private void updateAddAppointmentTableView() {
        updateAppointmentList();
        apptTableView.setItems(getAppointmentList());
    }

    /**
     * Grabs information to initialize screen and calls set language
     */
    @FXML
    public void initialize() {
        setLanguage();
//        Lambdas to call methods
        editAppointmentButton.setOnAction(event -> handleEditAppt(event));
        deleteAppointmentButton.setOnAction(event -> handleDeleteAppt(event));
//        Puts data to the table view
//        TODO These exist somewhere in the models
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().dateStringProperty());
        startColumn.setCellValueFactory(cellData -> cellData.getValue().startTimeProperty());
        endColumn.setCellValueFactory(cellData -> cellData.getValue().endTimeProperty());
        customerColumn.setCellValueFactory(cellData -> cellData.getValue().contactProperty());
        consultantColumn.setCellValueFactory(cellData -> cellData.getValue().contactProperty());
//        Updates the Table View
        updateAddAppointmentTableView();
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