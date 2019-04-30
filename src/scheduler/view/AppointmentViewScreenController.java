package scheduler.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import scheduler.model.Appointment;
import scheduler.util.DatabaseConnection;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Locale;
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
    private Text appoinmentViewTitle;

    @FXML
    void handleNewAppt(ActionEvent event) {
        try {
            Parent addAppointmentParent = FXMLLoader.load(getClass().getResource("AddAppointment.fxml"));
            Scene addAppointmentScene = new Scene(addAppointmentParent);
            Stage addAppointmentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            addAppointmentStage.setScene(addAppointmentScene);
            addAppointmentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        // Holds index of the appointment that will be modified
        private static int appointmentIndexToModify;

        /**
         * Set's language for all the text that can appear on the screen
         * */
//    TODO
        @FXML
        private void setLanguage () {
            ResourceBundle rb = ResourceBundle.getBundle("AddModifyAppointment", Locale.getDefault());
            titleColumn.setText(rb.getString("lblTitle"));
            typeColumn.setText(rb.getString("lblDate"));
            startColumn.setText(rb.getString("lblContact"));
            endColumn.setText(rb.getString("btnGetInfo"));
            customerColumn.setText(rb.getString("btnGetInfo"));
            consultantColumn.setText(rb.getString("btnGetInfo"));
            editAppointmentButton.setText(rb.getString("btnModify"));
            deleteAppointmentButton.setText(rb.getString("btnDelete"));
            newAppointmentButton.setText(rb.getString("btnExit"));
            appoinmentViewTitle.setText(rb.getString("lblTitle") + ":");
//            TODO, These will be the labels for week, month, all appointments
            lblAppointmentSummaryDescription.setText(rb.getString("lblDescription") + ":");
            lblAppointmentSummaryLocation.setText(rb.getString("lblLocation") + ":");
            lblAppointmentSummaryContact.setText(rb.getString("lblContact") + ":");

        }

        @FXML
        private void handleEditAppt (ActionEvent event){
//        Grab selected appointment to modify
            Appointment appointmentToModify = apptTableView.getSelectionModel().getSelectedItem();
//        Ensures something is selected
            if (appointmentToModify == null) {
                ResourceBundle rb = ResourceBundle.getBundle("AddModifyAppointment", Locale.getDefault());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(rb.getString("error"));
                alert.setHeaderText(rb.getString("errorModifyingAppointment"));
                alert.setContentText(rb.getString("errorModifyingAppointmentPleaseSelect"));
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
        private void handleDeleteAppt (ActionEvent event){
            // Get the selected appointment from the table view
            Appointment appointmentToDelete = apptTableView.getSelectionModel().getSelectedItem();
            // Check if no appointment was selected if
            if (appointmentToDelete == null) {
                // Show alert saying an appointment must be selected to delete
                ResourceBundle rb = ResourceBundle.getBundle("AddModifyAppointment", Locale.getDefault());
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
         * */
        @FXML
        private void exit (ActionEvent event){
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
        public static int getAppointmentIndexToModify () {
            return appointmentIndexToModify;
        }

        // Update the table view
        @FXML
        public void updateAddAppointmentTableView () {
            updateAppointmentList();
            apptTableView.setItems(getAppointmentList());
        }

        /**
         * Grabs information to initialize screen and calls set language
         * */
        @FXML
        public void initialize () {
            setLanguage();
//        Lambdas to call methods
            editAppointmentButton.setOnAction(event -> handleEditAppt(event));
            deleteAppointmentButton.setOnAction(event -> handleDeleteAppt(event));
//        Puts data to the table view
            titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
            typeColumn.setCellValueFactory(cellData -> cellData.getValue().dateStringProperty());
            startColumn.setCellValueFactory(cellData -> cellData.getValue().contactProperty());
            endColumn.setCellValueFactory(cellData -> cellData.getValue().contactProperty());
            customerColumn.setCellValueFactory(cellData -> cellData.getValue().contactProperty());
            consultantColumn.setCellValueFactory(cellData -> cellData.getValue().contactProperty());
            // Update table view
            updateAddAppointmentTableView();
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