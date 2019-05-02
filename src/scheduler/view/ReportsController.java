package scheduler.view;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import scheduler.model.Appointment;

import java.util.Locale;
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

    private void setLanguage() {
        ResourceBundle rb = ResourceBundle.getBundle("reports", Locale.getDefault());
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