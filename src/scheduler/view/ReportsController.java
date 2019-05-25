package scheduler.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import scheduler.model.Appointment;
import scheduler.model.AppointmentList;
import scheduler.model.AppointmentTypesByMonthTable;

import java.io.IOException;
import java.util.*;

import static scheduler.model.AppointmentList.getAppointmentList;
import static scheduler.util.DatabaseConnection.updateAppointmentList;

public class ReportsController {

    @FXML
    private Tab consultantTab;

    @FXML
    private TableView<Appointment> consultantScheduleTableView;

    @FXML
    private TableColumn<Appointment, String> appointmentTitleColumn;

    @FXML
    private TableColumn<Appointment, String> appointmentTypeColumn;

    @FXML
    private TableColumn<Appointment, String> appointmentDateColumn;

    @FXML
    private TableColumn<Appointment, String> appointmentTimeColumn;

    @FXML
    private TableColumn<Appointment, String> appointmentCustomerColumn;

    @FXML
    private Tab appointmentTab;

    @FXML
    private TableView<AppointmentTypesByMonthTable> appointmentTypesByMonthTableView;

    @FXML
    private TableColumn<AppointmentTypesByMonthTable, String> scheduleMonthColumn;

    @FXML
    private TableColumn<AppointmentTypesByMonthTable, String> scheduleTypeColumn;

    @FXML
    private TableColumn<AppointmentTypesByMonthTable, String> scheduleAmountColumn;

    @FXML
    private Tab reportTab;

    @FXML
    private Text consultantText;

    @FXML
    private ComboBox<String> consultantCombo;

    /**
     * Menu FXML
     * */

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
     * Menu Handlers
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
        appointmentDateColumn.setText(rb.getString("appointmentDateColumn"));
        appointmentTimeColumn.setText(rb.getString("appointmentTimeColumn"));
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

        consultantText.setText(rb.getString("consultantText"));
    }

    @FXML
    private void setAppointmentTypesByMonthTableView() {
        updateAppointmentList();
//        Will contain an array of strings
        ArrayList<String> monthsWithAppointmentsWithTypes = new ArrayList<>();
        ArrayList<Integer> appointmentsPerMonth = new ArrayList<>();

//        For each appointment in the list, add a year and month combo to arrayList
        for (Appointment appointment : getAppointmentList()) {
            Date startDate = appointment.getStartDate();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            String monthString = "";

//            Switch turns month numbers into words
            switch(month) {
                case 1:
                    monthString = "January";
                    break;
                case 2:
                    monthString = "February";
                    break;
                case 3:
                    monthString = "March";
                    break;
                case 4:
                    monthString = "April";
                    break;
                case 5:
                    monthString = "May";
                    break;
                case 6:
                    monthString = "June";
                    break;
                case 7:
                    monthString = "July";
                    break;
                case 8:
                    monthString = "August";
                    break;
                case 9:
                    monthString = "September";
                    break;
                case 10:
                    monthString = "October";
                    break;
                case 11:
                    monthString = "November";
                    break;
                case 12:
                    monthString = "December";
                    break;
            }

//            For every appointment, create a string with the month, year, then Appointment Type
            String yearMonthStringType = (monthString + " " + year + "," + appointment.getType());

            monthsWithAppointmentsWithTypes.add(yearMonthStringType);

        }
//        For Loop to compare each string to see if they match exactly across all params
        for (String appointmentFor : monthsWithAppointmentsWithTypes) {
            int amountPerMonth = 0;
            for (String appointmentFive : monthsWithAppointmentsWithTypes) {
                if (appointmentFor.equals(appointmentFive)) {
                    amountPerMonth++;
                }
            }
            appointmentsPerMonth.add(amountPerMonth);
        }

//        Creates a string with all of the data so that you can see what is unique
        ArrayList<String> finalDestinations = new ArrayList<>();
        for (int i = 0; i < monthsWithAppointmentsWithTypes.size(); i++) {
            finalDestinations.add(monthsWithAppointmentsWithTypes.get(i) + "," + appointmentsPerMonth.get(i));
        }

//        Sets unique strings as an array
        ArrayList<String> transmuteToUnique = new ArrayList<>();
        for(String set : finalDestinations) {
            if (!transmuteToUnique.contains(set)) {
                transmuteToUnique.add(set);
            }
        }

        final ObservableList<AppointmentTypesByMonthTable> data = FXCollections.observableArrayList();

        for (String item : transmuteToUnique) {
//            For each item in transmute, split each item by a comma delimiter
            String[] output = item.split(",");
//            Add data to the observableList. Strings become Params for AppointmentTypesByMonthTable class
            data.add(new AppointmentTypesByMonthTable(output[0], output[1], output[2]));
        }
//        Sets TableView "data")
        appointmentTypesByMonthTableView.setItems(data);
//        Lambdas to easily assign cellData to properties
        scheduleMonthColumn.setCellValueFactory(cellData -> cellData.getValue().monthProperty());
        scheduleTypeColumn.setCellValueFactory(cellData -> cellData.getValue().tableTypeProperty());
        scheduleAmountColumn.setCellValueFactory(cellData -> cellData.getValue().numberOfAppointmentsProperty());
    }



/**
 * Ideally, we would like to have this changed based on a combo box for each consultant
 * */
    @FXML
    private void setConsultantScheduleTableView() {
        updateAppointmentList();
        consultantScheduleTableView.setItems(getAppointmentList());


//        ArrayList<String> consultantsWithAppointments = new ArrayList<>();
        ObservableList<String> consultantList = FXCollections.observableArrayList();
//        call getCreatedBy() method to see who created, then add to ArrayList
//        for each appointment in the appointmentList, get created by and add to consultantsWithAppointments
        for (Appointment appointment : AppointmentList.getAppointmentList()) {
            String consultant = appointment.getCreatedBy();
            if (!consultantList.contains(consultant)) {
                consultantList.add(consultant);
            }
        }

        consultantCombo.getItems().clear();
        consultantCombo.setItems(FXCollections.observableArrayList(consultantList));
        consultantCombo.getSelectionModel().selectFirst();


        appointmentTitleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        appointmentTypeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        appointmentDateColumn.setCellValueFactory(cellData -> cellData.getValue().dateStringProperty());
        appointmentTimeColumn.setCellValueFactory(cellData -> cellData.getValue().startStringProperty());
        appointmentCustomerColumn.setCellValueFactory(cellData -> cellData.getValue().contactProperty());
    }

    @FXML
    public void initialize() {
//        Sets the local Language
        setLanguage();
//        Sets Data on the AppointmentTableView
        setAppointmentTypesByMonthTableView();
//        Sets Data on the ConsultantScheduleTableView
        setConsultantScheduleTableView();

//        setLastReport();
    }

}


/*I. Provide the ability to generate each of the following reports:

• number of appointment types by month

• the schedule for each consultant

• one additional report of your choice*/