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
import scheduler.model.AppointmentList;
import scheduler.util.DatabaseConnection;

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
    private TableView<Appointment> appointmentTypesByMonthTableView;

    @FXML
    private TableColumn<Appointment, String> scheduleMonthColumn;

    @FXML
    private TableColumn<Appointment, String> scheduleTypeColumn;

    @FXML
    private TableColumn<Appointment, Integer> scheduleAmountColumn;

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
        ArrayList<String> monthsWithAppointments = new ArrayList<>();
//        For each appointment in the list, add a year and month combo to arrayList
        for (Appointment appointment : AppointmentList.getAppointmentList()) {
            Date startDate = appointment.getStartDate();
//            System.out.println(startDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
//            System.out.println(calendar);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            String monthString = "";
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

            String yearMonthString = (monthString + " " + year);


//            System.out.println(yearMonthString);
            monthsWithAppointments.add(yearMonthString);


            for (String appointmentFor : monthsWithAppointments) {
                for (String appointmentFive monthsWithAppointments) {
                    if (appointmentFor == )
                }


            }


//            String yearMonth = year + "-" + month;
//            if (month < 10) {
//                yearMonth = year + "-0" + month;
//            } else if (!monthsWithAppointments.contains(yearMonth)) {
//                monthsWithAppointments.add(yearMonth);
//            }
        }
//        Test
        System.out.println(monthsWithAppointments);


        /*
        * This section will create an int for each item in the arrayList
        * based on how many "Types" it finds
        * */

////        Sorting the years and months
//        Collections.sort(monthsWithAppointments);
//        for (String yearMonth : monthsWithAppointments) {
////            Grabs the year and month values again
//            int year = Integer.parseInt(yearMonth.substring(0, 4));
//            int month = Integer.parseInt(yearMonth.substring(5, 7));
////            Initializing the counter
//            int typeCount = 0;
//            ArrayList<String> types = new ArrayList<>();
//            for (Appointment appointment : AppointmentList.getAppointmentList()) {
////                Grabs the appointment start date
//                Date startDate = appointment.getStartDate();
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTime(startDate);
////                Grabs year and month values from appointment
//                int appointmentYear = calendar.get(Calendar.YEAR);
//                int appointmentMonth = calendar.get(Calendar.MONTH) + 1;
////                If the years and month match, grab type
//                if ((year == appointmentYear) && (month == appointmentMonth)) {
//                    String type = appointment.getType();
////                    If not already in arrayList, add it then up the counter
//                    if (!types.contains(type)) {
//                        types.add(type);
//                        typeCount++;
//                    }
//                }
//            }
//        }




//
//        scheduleMonthColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
//        scheduleTypeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
//        scheduleAmountColumn.setCellValueFactory(cellData -> cellData.getValue().startStringProperty());
//

    }




    @FXML
    private void setConsultantScheduleTableView() {
//        Placeholder
        scheduleMonthColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
    }

    @FXML
    public void initialize() {
//        Sets the local Language
        setLanguage();
//        Sets Data on the AppointmentTableView
        setAppointmentTypesByMonthTableView();
//        Sets Data on the ConsultantScheduleTableView
        setConsultantScheduleTableView();

        DatabaseConnection.generateAppointmentTypeByMonthReport();

    }

}


/*I. Provide the ability to generate each of the following reports:

• number of appointment types by month

• the schedule for each consultant

• one additional report of your choice*/