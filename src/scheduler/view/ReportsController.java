package scheduler.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import scheduler.model.Appointment;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class ReportsController {

    @FXML
    private Tab appointmentTab;

    @FXML
    private TableView<Appointment> appointmentTableView;

    @FXML
    private TableColumn<Appointment, String> appointmentTitleColumn;

    @FXML
    private TableColumn<Appointment, String> appointmentTypeColumn;

    @FXML
    private TableColumn<Appointment, ?> appointmentStartColumn;

    @FXML
    private TableColumn<Appointment, ?> appointmentEndColumn;

    @FXML
    private TableColumn<Appointment, String> appointmentCustomerColumn;

    @FXML
    private Tab consultantTab;

    @FXML
    private TableView<?> scheduleTableView;

    @FXML
    private TableColumn<Appointment, ?> scheduleMonthColumn;

    @FXML
    private TableColumn<Appointment, ?> scheduleTypeColumn;

    @FXML
    private TableColumn<Appointment, ?> scheduleAmountColumn;

    @FXML
    private Tab reportTab;

    @FXML
    private MenuBar menuBar;

    @FXML
    private MenuItem menuBarLogOut;

    @FXML
    private MenuItem menuBarClose;

    @FXML
    private Menu menuBarAppointments;

    @FXML
    private Menu menuBarCustomers;

    @FXML
    private MenuItem menuBarGoAppointments;

    @FXML
    private MenuItem menuBarGoMain;

    @FXML
    private Menu menuBarMain;

    @FXML
    private MenuItem menuBarGoCustomers;

    @FXML
    private Menu menuBarFile;

    /**
     * Handler actions for customer screens
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

    /**
     * Logs you out
     */
    @FXML
    void menuBarLogOutHandler(ActionEvent event) {
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
    void menuBarAppointmentsHandler(ActionEvent event) {
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
    void menuBarMainHandler(ActionEvent event) {
        try {
            Parent customerParent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene customerScene = new Scene(customerParent);
            Stage customerStage = (Stage)  menuBar.getScene().getWindow();
            customerStage.setScene(customerScene);
            customerStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes Program
     */
    @FXML
    void menuBarCloseHandler(ActionEvent event) {
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

    private void setLanguage() {
        ResourceBundle rb = ResourceBundle.getBundle("resources/reports", Locale.getDefault());
        appointmentTitleColumn.setText(rb.getString("appointmentTitleColumn"));
        appointmentTypeColumn.setText(rb.getString("appointmentTypeColumn"));
        appointmentStartColumn.setText(rb.getString("appointmentStartColumn"));
        appointmentEndColumn.setText(rb.getString("appointmentEndColumn"));
        appointmentCustomerColumn.setText(rb.getString("appointmentCustomerColumn"));
        scheduleMonthColumn.setText(rb.getString("scheduleMonthColumn"));
        scheduleTypeColumn.setText(rb.getString("scheduleTypeColumn"));
        scheduleAmountColumn.setText(rb.getString("scheduleAmountColumn"));
        appointmentTab.setText(rb.getString("appointmentTab"));
        consultantTab.setText(rb.getString("consultantTab"));
        reportTab.setText(rb.getString("reportTab"));

        menuBarLogOut.setText(rb.getString("menuBarLogOut"));
        menuBarClose.setText(rb.getString("menuBarClose"));
        menuBarMain.setText(rb.getString("menuBarMain"));
        menuBarAppointments.setText(rb.getString("menuBarAppointments"));
        menuBarFile.setText(rb.getString("menuBarFile"));
        menuBarGoMain.setText(rb.getString("menuBarGoMain"));
        menuBarGoAppointments.setText(rb.getString("menuBarGoAppointments"));

        menuBarCustomers.setText(rb.getString("menuBarCustomers"));
        menuBarGoCustomers.setText(rb.getString("menuBarGoCustomers"));
    }

    @FXML
    public void initialize() {
//        Sets the local Language
        setLanguage();

//        TODO Add logic to this

        // Assign actions to buttons
//        btnModifyCustomerSave.setOnAction(event -> saveModifyCustomer(event));
//        btnModifyCustomerCancel.setOnAction(event -> cancelModifyCustomer(event));
//        // Get customer to be modified via index
//        customer = getCustomerRoster().get(customerIndexToModify);
//        // Get customer information
//        String customerName = customer.getCustomerName();
//        String address = customer.getAddress();
//        String address2 = customer.getAddress2();
//        String city = customer.getCity();
//        String country = customer.getCountry();
//        String postalCode = customer.getPostalCode();
//        String phone = customer.getPhone();
//        // Populate information fields with current customer information
//        txtModifyCustomerName.setText(customerName);
//        txtModifyCustomerAddress.setText(address);
//        txtModifyCustomerAddress2.setText(address2);
//        txtModifyCustomerCity.setText(city);
//        txtModifyCustomerCountry.setText(country);
//        txtModifyCustomerPostalCode.setText(postalCode);
//        txtModifyCustomerPhone.setText(phone);
    }

}


/*I. Provide the ability to generate each of the following reports:

• number of appointment types by month

• the schedule for each consultant

• one additional report of your choice*/