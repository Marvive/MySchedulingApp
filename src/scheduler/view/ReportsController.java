package scheduler.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import scheduler.model.Appointment;
import scheduler.model.AppointmentList;
import scheduler.model.AppointmentTypesByMonthTable;

import java.io.IOException;
import java.util.*;

import static scheduler.util.DatabaseConnection.updateAppointmentList;

public class ReportsController {

    @FXML
    private Tab consultantTab;

    @FXML
    private TableView<Appointment> consultantScheduleTableView;

    @FXML
    private TableColumn<?, String> appointmentTitleColumn;

    @FXML
    private TableColumn<?, String> appointmentTypeColumn;

    @FXML
    private TableColumn<?, ?> appointmentStartColumn;

    @FXML
    private TableColumn<?, ?> appointmentEndColumn;

    @FXML
    private TableColumn<?, String> appointmentCustomerColumn;

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
    private void setAppointmentTypesByMonthTableView() {
        updateAppointmentList();
//        Will contain an array of strings
        ArrayList<String> monthsWithAppointmentsWithTypes = new ArrayList<>();
        ArrayList<Integer> appointmentsPerMonth = new ArrayList<>();
        ArrayList<String> literalDate = new ArrayList<>();
        ArrayList<String> literalType = new ArrayList<>();
//        For each appointment in the list, add a year and month combo to arrayList
        for (Appointment appointment : AppointmentList.getAppointmentList()) {
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
            String yearMonthString = (monthString + " " + year);
            String typeString = appointment.getType();


//            System.out.println(yearMonthString);
            monthsWithAppointmentsWithTypes.add(yearMonthStringType);
            literalDate.add(yearMonthString);
            literalType.add(typeString);

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


        ArrayList<String> finalDestinations = new ArrayList<>();

        for (int i = 0; i < monthsWithAppointmentsWithTypes.size(); i++) {
            finalDestinations.add(monthsWithAppointmentsWithTypes.get(i) + "," + appointmentsPerMonth.get(i));
        }

        ArrayList<String> transmuteToUnique = new ArrayList<>();
        for(String set : finalDestinations) {
            if (!transmuteToUnique.contains(set)) {
                transmuteToUnique.add(set);
            }
        }

        ArrayList<ArrayList> outputs = new ArrayList<>();

        final ObservableList<AppointmentTypesByMonthTable> data = FXCollections.observableArrayList();
        for (String item : transmuteToUnique) {
            ArrayList<String> outputLOL = new ArrayList<>();
            String[] output = item.split(",");
            outputLOL.add(output[0]);
            outputLOL.add(output[1]);
            outputLOL.add(output[2]);
            outputs.add(outputLOL);
            data.add(new AppointmentTypesByMonthTable(output[0], output[1], output[2]));
        }

        appointmentTypesByMonthTableView.setItems(data);
        scheduleMonthColumn.setCellValueFactory(cellData -> cellData.getValue().monthProperty());
        scheduleTypeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        scheduleAmountColumn.setCellValueFactory(cellData -> cellData.getValue().numberOfAppointmentsProperty());
    }




    @FXML
    private void setConsultantScheduleTableView() {

    }

    @FXML
    public void initialize() {
//        Sets the local Language
        setLanguage();
//        Sets Data on the AppointmentTableView
        setAppointmentTypesByMonthTableView();
//        Sets Data on the ConsultantScheduleTableView
        setConsultantScheduleTableView();

//        DatabaseConnection.generateAppointmentTypeByMonthReport();

    }

}


/*I. Provide the ability to generate each of the following reports:

• number of appointment types by month

• the schedule for each consultant

• one additional report of your choice*/