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
import scheduler.model.Customer;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import static scheduler.model.CustomerRoster.getCustomerRoster;

public class CustomerEditScreenController {

    @FXML
    private TableView<Customer> customerTableView;

    @FXML
    private TableColumn<Customer, String> customerNameColumn;

    @FXML
    private TableColumn<Customer, String> customerPhoneColumn;

    @FXML
    private Text customerScreenText;

    @FXML
    private Label customerIDLabel;

    @FXML
    private Label customerAddressLabel;

    @FXML
    private Label customerNameLabel;

    @FXML
    private Label customerCountryLabel;

    @FXML
    private Label customerCityLabel;

    @FXML
    private Label customerAddressLabel2;

    @FXML
    private Label customerPhoneNumber;

    @FXML
    private Label customerPostalCodeLabel;

    @FXML
    private TextField customerIDTextField;

    @FXML
    private TextField customerNameTextField;

    @FXML
    private TextField customerCountryTextField;

    @FXML
    private TextField customerAddress2TextField;

    @FXML
    private TextField customerAddressTextField;

    @FXML
    private TextField customerPostalCode;

    @FXML
    private TextField customerPhoneTextField;

    @FXML
    private ComboBox<String> customerCityComboBox;

    @FXML
    private Button customerNewButton;

    @FXML
    private Button customerEditButton;

    @FXML
    private Button customerDeleteButton;

    @FXML
    private MenuBar menuBar;

    @FXML
    private MenuItem menuBarLogOut;

    @FXML
    private MenuItem menuBarClose;

    @FXML
    private MenuItem menuBarAppointmentsItem;

    @FXML
    private Menu menuBarAppointments;

    @FXML
    private Menu menuBarCustomers;

    @FXML
    private Menu menuBarReports;


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

    /**
     * Handlers for Reports Buttons
     */
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

    @FXML
    void menuBarReportsHandler(ActionEvent event) {
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
    void menuBarMainHandler(ActionEvent event) {
        try {
            Parent addAppointmentParent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene addAppointmentScene = new Scene(addAppointmentParent);
            Stage addAppointmentStage = (Stage)  menuBar.getScene().getWindow();
            addAppointmentStage.setScene(addAppointmentScene);
            addAppointmentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void customerDeleteButtonHandler(ActionEvent event) {
        ResourceBundle rb = ResourceBundle.getBundle("resources/customers", Locale.getDefault());
    }

    @FXML
    private void customerNewButtonHandler(ActionEvent event) {
        ResourceBundle rb = ResourceBundle.getBundle("resources/customers", Locale.getDefault());
    }

//    Initialize customer variable to create a Customer

    /**
     * IN PROGRESS
     */
    private Customer customer;
    // Holds index of the customer that will be modified
    private static int customerIndexToModify;

    // Return the customer index to be modified
    public static int getCustomerIndexToModify() {
        return customerIndexToModify;
    }

    // Open modify customer window
    @FXML
    private void customerEditButtonHandler(ActionEvent event) {
        // Get selected customer from table view
        Customer customerToModify = customerTableView.getSelectionModel().getSelectedItem();
        // Check if no customer was selected
        if (customerToModify == null) {
            // Create alert saying a customer must be selected to be modified
            ResourceBundle rb = ResourceBundle.getBundle("resources/customers", Locale.getDefault());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            TODO Add into RB
            alert.setTitle(rb.getString("error"));
            alert.setHeaderText(rb.getString("errorHeader"));
            alert.setContentText(rb.getString("errorContent"));
            alert.showAndWait();
            return;
        }
        // Set the index of the customer to be modified
        customerIndexToModify = getCustomerRoster().indexOf(customerToModify);
        // Open modify customer window
        try {
            Parent modifyCustomerParent = FXMLLoader.load(getClass().getResource("ModifyCustomer.fxml"));
            Scene modifyCustomerScene = new Scene(modifyCustomerParent);
            Stage modifyCustomerStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            modifyCustomerStage.setScene(modifyCustomerScene);
            modifyCustomerStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//########################################################################

    private ResourceBundle rb1 = ResourceBundle.getBundle("resources/customers", Locale.getDefault());

    @FXML
//    private ComboBox comboBox;
    /**
     * Sets that Data in the combo box
     * Used in initialize
     * TODO Placeholder for City names
     * */
    private void setComboData() {
        customerCityComboBox.getItems().clear();
        customerCityComboBox.getItems().addAll(
                rb1.getString("City1"),
                rb1.getString("City2"),
                rb1.getString("City3")
        );

    }


    // Set labels to local language (default is English)
    @FXML
    private void setLanguage() {
        ResourceBundle rb = ResourceBundle.getBundle("resources/customers", Locale.getDefault());
        customerNameColumn.setText(rb.getString("customerNameColumn"));
        customerPhoneColumn.setText(rb.getString("customerPhoneColumn"));
        customerScreenText.setText(rb.getString("customerScreenText"));
        customerIDLabel.setText(rb.getString("customerIDLabel"));
        customerAddressLabel.setText(rb.getString("customerAddressLabel"));
        customerNameLabel.setText(rb.getString("customerNameLabel"));
        customerCountryLabel.setText(rb.getString("customerCountryLabel"));
        customerCityLabel.setText(rb.getString("customerCityLabel"));
        customerAddressLabel2.setText(rb.getString("customerAddressLabel2"));
        customerPhoneNumber.setText(rb.getString("customerPhoneNumber"));
        customerPostalCodeLabel.setText(rb.getString("customerPostalCodeLabel"));
        customerNewButton.setText(rb.getString("customerNewButton"));
        customerEditButton.setText(rb.getString("customerEditButton"));
        customerDeleteButton.setText(rb.getString("customerDeleteButton"));
    }


    // Initialize screen elements
    @FXML
    public void initialize() {
//        Sets the language
        setLanguage();
//        Sets information in the Combo Box
        setComboData();
        customerNameColumn.setCellValueFactory(cellData -> cellData.getValue().customerNameProperty());
        customerPhoneColumn.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
//        Lambdas to assign actions to buttons
        customerNewButton.setOnAction(event -> customerNewButtonHandler(event));
        customerEditButton.setOnAction(event -> customerEditButtonHandler(event));
        customerDeleteButton.setOnAction(event -> customerDeleteButtonHandler(event));

        customer = getCustomerRoster().get(customerIndexToModify);
//        Grab customer by index if the modify button was pressed
        if (customer != null) {
            try {
                customer = getCustomerRoster().get(customerIndexToModify);
                String customerName = customer.getCustomerName();
                String address = customer.getAddress();
                String address2 = customer.getAddress2();
                String city = customer.getCity();
                String country = customer.getCountry();
                String postalCode = customer.getPostalCode();
                String phone = customer.getPhone();
//            Populate the Phone number and name
                customerNameTextField.setText(customerName);
                customerAddressTextField.setText(address);
                customerAddress2TextField.setText(address2);
//            customerCityComboBox.set(city);
                customerCountryTextField.setText(country);
                customerPostalCode.setText(postalCode);
                customerPhoneTextField.setText(phone);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Get customer information


    }


}

/*
Provide the ability to add, update, and delete customer records
 in the database, including name, address, and phone number.

 F. Write exception controls to prevent each of the following.
  You may use the same mechanism of exception control more than once,
  but you must incorporate at least two different mechanisms of exception control.

• scheduling an appointment outside business hours

• scheduling overlapping appointments

• entering nonexistent or invalid customer data

• entering an incorrect username and password
* */