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
import scheduler.model.CustomerAppointmentsTable;
import scheduler.util.DatabaseConnection;

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
    private Tab customerAppointmentTab;

    @FXML
    private TableView<CustomerAppointmentsTable> customerAppointmentsTableView;

    @FXML
    private TableColumn<CustomerAppointmentsTable, String> cACustomerColumn;

    @FXML
    private TableColumn<CustomerAppointmentsTable, String> cAConsultationColumn;

    @FXML
    private TableColumn<CustomerAppointmentsTable, String> cAFollowUpColumn;

    @FXML
    private TableColumn<CustomerAppointmentsTable, String> cANewAccountColumn;

    @FXML
    private TableColumn<CustomerAppointmentsTable, String> cACloseAccountColumn;

    @FXML
    private TableColumn<CustomerAppointmentsTable, String> caTotalColumn;


    @FXML
    private Tab reportTab;

    @FXML
    private Text consultantText;

    @FXML
    private ComboBox<String> consultantCombo;

    /**
     * Menu FXML
     */

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


////    private final CategoryAxis xAxis = new CategoryAxis();
////    private final NumberAxis yAxis = new NumberAxis();
//
//    @FXML
//    private BarChart<String, Integer> barChart;
//    @FXML
//    private CategoryAxis xAxis;
//    @FXML
//    private NumberAxis yAxis;
//
//
////    CategoryAxis xAxis    = new CategoryAxis();
////xAxis.setLabel("Devices");
////
////    NumberAxis yAxis = new NumberAxis();
////yAxis.setLabel("Visits");
////
////    BarChart     barChart = new BarChart(xAxis, yAxis);
//    private HashMap<String, Integer> hashmap;

    /**
     * Menu Handlers
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

    /**
     * Logs you out
     */
    @FXML
    void menuBarLogOutHandler(ActionEvent event) {
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
    void menuBarAppointmentsHandler(ActionEvent event) {
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
    void menuBarMainHandler(ActionEvent event) {
        try {
            Parent customerParent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene customerScene = new Scene(customerParent);
            Stage customerStage = (Stage) menuBar.getScene().getWindow();
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

    /**
     * Shows the appointment Types per month report Table
     */
    @FXML
    private void setAppointmentTypesByMonthTableView() {
        DatabaseConnection.updateAppointmentList();
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
            switch (month) {
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
        for (String set : finalDestinations) {
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
//        Sets TableView "data"
        appointmentTypesByMonthTableView.setItems(data);
//        Lambdas to easily assign cellData to properties
        scheduleMonthColumn.setCellValueFactory(cellData -> cellData.getValue().monthProperty());
        scheduleTypeColumn.setCellValueFactory(cellData -> cellData.getValue().tableTypeProperty());
        scheduleAmountColumn.setCellValueFactory(cellData -> cellData.getValue().numberOfAppointmentsProperty());
    }

    /**
     * Shows the schedule of each consultant based on which consultant is selected
     */
    @FXML
    private void setConsultantScheduleTableView() {
        updateAppointmentList();

        ObservableList<String> consultantList = FXCollections.observableArrayList();

//        call getCreatedBy() method to see who created, then add to ArrayList
//        for each appointment in the appointmentList, get created by and add to consultantsWithAppointments
        for (Appointment appointment : AppointmentList.getAppointmentList()) {
//            if (appointment.getStartDate().after(Calendar.getInstance().getTime())) {
            String consultant = appointment.getCreatedBy();
            if (!consultantList.contains(consultant)) {
                consultantList.add(consultant);
            }
//            }
        }
        consultantCombo.getItems().clear();
        consultantCombo.setItems(FXCollections.observableArrayList(consultantList));
        consultantCombo.getSelectionModel().selectFirst();

//        Sets table data to the default item selected
        ObservableList<Appointment> appointmentListFirst = FXCollections.observableArrayList();
        for (Appointment appointment : AppointmentList.getAppointmentList()) {
            if (appointment.getCreatedBy().equals(consultantCombo.getSelectionModel().selectedItemProperty().get())) {
                appointmentListFirst.addAll(appointment);
            }
        }

        consultantScheduleTableView.setItems(appointmentListFirst);

//        Lambda that sets an addListener on the comboBox options. Changes TableView on selection
        consultantCombo.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
                    ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
                    for (Appointment appointment : AppointmentList.getAppointmentList()) {
                        String consultant = appointment.getCreatedBy();
                        if (newValue.equals(consultant)) {
                            appointmentList.addAll(appointment);
                        }
                    }
                    consultantScheduleTableView.setItems(appointmentList);
                }
        );

        appointmentTitleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        appointmentTypeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        appointmentDateColumn.setCellValueFactory(cellData -> cellData.getValue().dateStringProperty());
        appointmentTimeColumn.setCellValueFactory(cellData -> cellData.getValue().startStringProperty());
        appointmentCustomerColumn.setCellValueFactory(cellData -> cellData.getValue().contactProperty());
    }


    //    Prep for bar chart
    private ResourceBundle rb1 = ResourceBundle.getBundle("resources/reports", Locale.getDefault());
    private String consultationString = rb1.getString("consultation");
    private String followUpString = rb1.getString("followUp");
    private String newAccountString = rb1.getString("newAccount");
    private String closeAccountString = rb1.getString("closeAccount");

//    private ObservableList<XYChart.Series<String, Double>> getChartData() {
//        double aValue = 0;
//
//        ObservableList<XYChart.Series<String, Double>> data = FXCollections.observableArrayList();
//
//        XYChart.Series<String, Double> aSeries = new XYChart.Series<String, Double>();
//
//        for (Appointment appointment: AppointmentList.getAppointmentList()) {
//            aSeries.getData().add(consultationString, aValue);
//        }
//
//
//        return null;
//    }

    /**
     * Sets a bar chart of how many appointments each customer has
     */
//    @FXML
//    private void setBarChart() {
//        DatabaseConnection.updateAppointmentList();
//        ObservableList<XYChart.Data<String, Integer>> data1 = FXCollections.observableArrayList();
//        ObservableList<XYChart.Data<String, Integer>> data2 = FXCollections.observableArrayList();
//        ObservableList<XYChart.Data<String, Integer>> data3 = FXCollections.observableArrayList();
//        ObservableList<XYChart.Data<String, Integer>> data4 = FXCollections.observableArrayList();
//
//
////        private HashMap<String, Integer> hashmap;
//
//        XYChart.Series series1 = new XYChart.Series();
//        XYChart.Series<String, Integer> series2 = new XYChart.Series();
//        XYChart.Series<String, Integer> series3 = new XYChart.Series();
//        XYChart.Series<String, Integer> series4 = new XYChart.Series();
//
////        XYChart.Series series1 = new XYChart.Series();
////        XYChart.Series series2 = new XYChart.Series();
////        XYChart.Series series3 = new XYChart.Series();
////        XYChart.Series series4 = new XYChart.Series();
//
////        Axis Labels
//        xAxis.setLabel(rb1.getString("customers"));
//        yAxis.setLabel(rb1.getString("appointments"));
//
//
//        ArrayList<String> customerNamesWithAppointments = new ArrayList<>();
////        Setting the Categories
//        for (Appointment appointment : AppointmentList.getAppointmentList()) {
//            String name = appointment.getContact();
//            if (!(customerNamesWithAppointments.contains(name))) {
//                customerNamesWithAppointments.add(name);
//            }
//        }
//
//        xAxis.setCategories(FXCollections.observableArrayList(customerNamesWithAppointments));
//        xAxis.invalidateRange(customerNamesWithAppointments);
//        series1.setName("Current Test");
//
//
////        barChart.getData().add(series1);
//
//
//        for (String customer : customerNamesWithAppointments) {
//            int counter1 = 0;
//            int counter2 = 0;
//            int counter3 = 0;
//            int counter4 = 0;
//            String type1 = "";
//            String type2 = "";
//            String type3 = "";
//            String type4 = "";
//            for (Appointment appointment : AppointmentList.getAppointmentList()) {
//                if (customer.equals(appointment.getContact())) {
//                    if (appointment.getType().equals(consultationString)) {
//                        type1 = appointment.getType();
//                        counter1++;
//
//                    } else if (appointment.getType().equals(followUpString)) {
//                        type2 = appointment.getType();
//                        counter2++;
//
//                    } else if (appointment.getType().equals(newAccountString)) {
//                        type3 = appointment.getType();
//                        counter3++;
//
//                    } else if (appointment.getType().equals(closeAccountString)) {
//                        type4 = appointment.getType();
//                        counter4++;
//                    }
//                }
//            }
//            if (!type1.equals("")) {
////                data1.add(new XYChart.Data<>(type1, counter1, customer));
//                series1.getData().add(new XYChart.Data<>(type1, counter1));
//                hashmap.put(type1, counter1);
//            }
//            if (!type2.equals("")) {
////                data2.add(new XYChart.Data<>(type2, counter2, customer));
//                series2.getData().add(new XYChart.Data<>(type2, counter2));
//            }
//            if (!type3.equals("")) {
////                data3.add(new XYChart.Data<>(type3, counter3, customer));
//                series3.getData().add(new XYChart.Data<>(type3, counter3));
//            }
//            if (!type4.equals("")) {
////                data4.add(new XYChart.Data<>(type4, counter4, customer));
//                series4.getData().add(new XYChart.Data<>(type4, counter4));
//            }
//        }
//
//
//        barChart.getData().add(series1);
//
//
//        System.out.println(hashmap);
////        System.out.println(data1);
////        System.out.println(data2);
////        System.out.println(data3);
////        System.out.println(data4);
////        System.out.println(series1);
////        System.out.println(series2);
////        System.out.println(series3);
////        System.out.println(series4);
//
////        barChart.getData().addAll(data1);
////        barChart.getData().addAll(series1, series2, series3, series4);
////
////        barChart.getData().add(new XYChart.Series<>(consultationString, data1));
////        barChart.getData().add(new XYChart.Series<>(consultationString, data1));
////        barChart.getData().add(new XYChart.Series<>(followUpString, data2));
////        barChart.getData().add(new XYChart.Series<>(newAccountString, data3));
////        barChart.getData().add(new XYChart.Series<>(closeAccountString, data4));
////
////
////        while (true) {
////            String title = appointmentgetTitle();
////            int counter = 0;
////            for (:
////                 ) {
////
////            }
////
////            data.add(new XYChart.Data<>(title, counter));
////            counter++;
//    }


//        data.addAll(series);
//        barChart.getData().add(series);


//
////        new XYChart.Data<>(item.getName(), item.getValue1()))
//        ObservableList<XYChart.Data<String, Number>> data = FXCollections.observableArrayList();
//        ObservableList<XYChart.Data<String, Number>> data2 = FXCollections.observableArrayList();
//        ObservableList<XYChart.Data<String, Number>> data3 = FXCollections.observableArrayList();
//        ObservableList<XYChart.Data<String, Number>> data4 = FXCollections.observableArrayList();
//
//
////        For each customer in customerNamesWithAppointments, get the types of Appointments and add to data
//        for (Appointment appointment: AppointmentList.getAppointmentList()) {
//            for (String customer : customerNamesWithAppointments) {
//                if (appointment.getContact().equals(customer)) {
////                    data.add();
//                    if (appointment.getType().equals(consultationString)) {
//                        data.add(consultationString, );
//                    } else if (appointment.getType().equals(followUpString)) {
//                        data2.add(followUpString, );
//                    } else if (appointment.getType().equals(newAccountString)) {
//                        data3.add(newAccountString, );
//                    } else if (appointment.getType().equals(closeAccountString)) {
//                        data4.add(closeAccountString, );
//                    } else {
//                        System.out.println(appointment.getType());
//                    }
////                    System.out.println(appointment.getType());
//                }
//            }
//        }
//
//        barChart.getData().add(new XYChart.Series<>(consultationString, data));
//        barChart.getData().add(new XYChart.Series<>(followUpString, data2));
//        barChart.getData().add(new XYChart.Series<>(newAccountString, data3));
//        barChart.getData().add(new XYChart.Series<>(closeAccountString, data4));


//        BarChart<String, Number> chart =
//                new BarChart<String, Number>(new CategoryAxis(), new NumberAxis());
//        chart.getData().add(new XYChart.Series<>("Value 1", dataSet1));
//        chart.getData().add(new XYChart.Series<>("Value 2", dataSet2));


//}


    /**
     * Each row will be a customer showing types of appointments and the total
     */
    @FXML
    private void setCustomerAppointmentsTableView() {
        DatabaseConnection.updateAppointmentList();


        ArrayList<String> customerNamesWithAppointments = new ArrayList<>();
//        Setting the Categories
        for (Appointment appointment : AppointmentList.getAppointmentList()) {
            String name = appointment.getContact();
            if (!(customerNamesWithAppointments.contains(name))) {
                customerNamesWithAppointments.add(name);
            }
        }

        final ObservableList<CustomerAppointmentsTable> caData = FXCollections.observableArrayList();
        for (String customer : customerNamesWithAppointments) {
            int counter1 = 0;
            int counter2 = 0;
            int counter3 = 0;
            int counter4 = 0;
            int counterTotal = 0;
//            String type1 = "";
//            String type2 = "";
//            String type3 = "";
//            String type4 = "";
            for (Appointment appointment : AppointmentList.getAppointmentList()) {
                if (customer.equals(appointment.getContact())) {
                    if (appointment.getType().equals(consultationString)) {
//                        type1 = appointment.getType();
                        counter1++;

                    } else if (appointment.getType().equals(followUpString)) {
//                        type2 = appointment.getType();
                        counter2++;

                    } else if (appointment.getType().equals(newAccountString)) {
//                        type3 = appointment.getType();
                        counter3++;

                    } else if (appointment.getType().equals(closeAccountString)) {
//                        type4 = appointment.getType();
                        counter4++;
                    }
                    counterTotal++;
                }

            }
//            if (!type1.equals("")) {
//
//            }
//            if (!type2.equals("")) {
//
//            }
//            if (!type3.equals("")) {
//
//            }
//            if (!type4.equals("")) {
//
//            }
            String consultationCounter = String.valueOf(counter1);
            String followUpCounter = String.valueOf(counter2);
            String newAccountCounter = String.valueOf(counter3);
            String closeAccountCounter = String.valueOf(counter4);
            String totalCounter = String.valueOf(counterTotal);
            caData.add(new CustomerAppointmentsTable(customer, consultationCounter, followUpCounter, newAccountCounter, closeAccountCounter, totalCounter));
        }


        customerAppointmentsTableView.setItems(caData);
        cACustomerColumn.setCellValueFactory(cellData -> cellData.getValue().customerProperty());
        cAConsultationColumn.setCellValueFactory(cellData -> cellData.getValue().consultationProperty());
        cAFollowUpColumn.setCellValueFactory(cellData -> cellData.getValue().followUpProperty());
        cANewAccountColumn.setCellValueFactory(cellData -> cellData.getValue().newAccountProperty());
        cACloseAccountColumn.setCellValueFactory(cellData -> cellData.getValue().closeAccountProperty());
        caTotalColumn.setCellValueFactory(cellData -> cellData.getValue().totalProperty());
    }

    public void initialize() {
//        Sets the local Language
        setLanguage();
//        Sets Data on the AppointmentTableView
        setAppointmentTypesByMonthTableView();
//        Sets Data on the ConsultantScheduleTableView
        setConsultantScheduleTableView();

//        Sets the Bar Chart Data
//        setBarChart();

        setCustomerAppointmentsTableView();
    }

}


/*I. Provide the ability to generate each of the following reports:

• number of appointment types by month

• the schedule for each consultant

• one additional report of your choice*/