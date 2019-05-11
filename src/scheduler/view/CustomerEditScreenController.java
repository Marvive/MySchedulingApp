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
import static scheduler.util.DatabaseConnection.*;

public class CustomerEditScreenController {

    @FXML
    private MenuBar menuBar;

    @FXML
    private MenuItem menuBarLogOut;

    @FXML
    private MenuItem menuBarClose;

    @FXML
    private Menu menuBarMain;

    @FXML
    private Menu menuBarAppointments;

    @FXML
    private Menu menuBarReports;

    @FXML
    private MenuItem menuBarGoReports;

    @FXML
    private MenuItem menuBarGoAppointments;

    @FXML
    private Menu menuBarFile;

    @FXML
    private MenuItem menuBarGoMain;

    @FXML
    private Text customerScreenText;

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
    private TextField customerNameTextField;

    @FXML
    private TextField customerPostalCode;

    @FXML
    private TextField customerCountryTextField;

    @FXML
    private TextField customerAddress2TextField;

    @FXML
    private TextField customerAddressTextField;

    @FXML
    private TextField customerPhoneTextField;

    @FXML
    private TextField customerCityTextField;

    @FXML
    private Button customerAddSaveButton;

    @FXML
    private Button customerAddCancelButton;

//    Initialize the customer
    private Customer customer;
//    Borrow indexToModify from CustomerScreen Controller
    private int customerIndexToModify = CustomerScreenController.getCustomerIndexToModify();

    /**
     * Button Handlers
     */
    @FXML
    void customerEditCancelHandler(ActionEvent event) {
//        Alert to confirm cancel
        ResourceBundle rb = ResourceBundle.getBundle("resources/customerEditScreen", Locale.getDefault());
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle(rb.getString("cancelTitle"));
        alert.setHeaderText(rb.getString("cancelHeader"));
        alert.setContentText(rb.getString("cancelText"));
        Optional<ButtonType> result = alert.showAndWait();
//        If cancelled, go back to customer screen
        if (result.get() == ButtonType.OK) {
            try {
                Parent mainScreenParent = FXMLLoader.load(getClass().getResource("CustomerScreen.fxml"));
                Scene mainScreenScene = new Scene(mainScreenParent);
                Stage mainScreenStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                mainScreenStage.setScene(mainScreenScene);
                mainScreenStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void customerEditSaveHandler(ActionEvent event) {
//        Grab information from TextFields
        int customerId = customer.getCustomerId();
        String customerName = customerNameTextField.getText();
        String address = customerAddressTextField.getText();
        String address2 = customerAddress2TextField.getText();
        String city = customerCityTextField.getText();
        String country = customerCountryTextField.getText();
        String postalCode = customerPostalCode.getText();
        String phone = customerPhoneTextField.getText();
//        Validate customer
        String errorMessage = Customer.isCustomerValid(customerName, address, city, country, postalCode, phone);
//        Error message notification
        if (errorMessage.length() > 0) {
            ResourceBundle rb = ResourceBundle.getBundle("resources/databaseConnection", Locale.getDefault());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(rb.getString("error"));
            alert.setHeaderText(rb.getString("errorModifyingCustomer"));
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return;
        }
//        Update Database and save
        int modifyCustomerCheck = modifyCustomer(customerId, customerName, address, address2, city, country, postalCode, phone);
//        Check if customer is active
        if (modifyCustomerCheck == 1) {
            ResourceBundle rb = ResourceBundle.getBundle("resources/databaseConnection", Locale.getDefault());
            // Create alert saying that customer already exists
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(rb.getString("error"));
            alert.setHeaderText(rb.getString("errorModifyingCustomer"));
            alert.setContentText(rb.getString("errorCustomerAlreadyExists"));
            alert.showAndWait();

        } else if (modifyCustomerCheck == 0) {
//        Check if customer is inactive
            // Calculate country, city and addressId's
            int countryId = calculateCountryId(country);
            int cityId = calculateCityId(city, countryId);
            int addressId = calculateAddressId(address, address2, postalCode, phone, cityId);
//            Change Customer to active
            setCustomerToActive(customerName, addressId);
        }
        try {
//            Return to Customer Screen
            Parent mainScreenParent = FXMLLoader.load(getClass().getResource("CustomerScreen.fxml"));
            Scene mainScreenScene = new Scene(mainScreenParent);
            Stage mainScreenStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            mainScreenStage.setScene(mainScreenScene);
            mainScreenStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Menu Handlers
     */
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


    private void getCustomerInfo() {
//        Selects customer to modify by the index number
        customer = getCustomerRoster().get(customerIndexToModify);
//        Grabs customer information from the DB
        String customerName = customer.getCustomerName();
        String address = customer.getAddress();
        String address2 = customer.getAddress2();
        String city = customer.getCity();
        String country = customer.getCountry();
        String postalCode = customer.getPostalCode();
        String phone = customer.getPhone();
        // Populate information fields with current customer information
//        Populates the fields of the customer
        customerNameTextField.setText(customerName);
        customerAddressTextField.setText(address);
        customerAddress2TextField.setText(address2);
        customerCityTextField.setText(city);
        customerCountryTextField.setText(country);
        customerPostalCode.setText(postalCode);
        customerPhoneTextField.setText(phone);
    }



    /**
     * Sets the language of the text on the screen Used in the initialize method
     */
    @FXML
    private void setLanguage() {
        ResourceBundle rb = ResourceBundle.getBundle("resources/customerEditScreen", Locale.getDefault());
        menuBarLogOut.setText(rb.getString("menuBarLogOut"));
        menuBarClose.setText(rb.getString("menuBarClose"));
        menuBarMain.setText(rb.getString("menuBarMain"));
        menuBarAppointments.setText(rb.getString("menuBarAppointments"));
        menuBarReports.setText(rb.getString("menuBarReports"));
        menuBarFile.setText(rb.getString("menuBarFile"));
        menuBarGoMain.setText(rb.getString("menuBarGoMain"));
        menuBarGoReports.setText(rb.getString("menuBarGoReports"));
        menuBarGoAppointments.setText(rb.getString("menuBarGoAppointments"));

        customerScreenText.setText(rb.getString("customerScreenText"));
        customerAddressLabel.setText(rb.getString("customerAddressLabel"));
        customerNameLabel.setText(rb.getString("customerNameLabel"));
        customerCountryLabel.setText(rb.getString("customerCountryLabel"));
        customerCityLabel.setText(rb.getString("customerCityLabel"));
        customerAddressLabel2.setText(rb.getString("customerAddressLabel2"));
        customerPhoneNumber.setText(rb.getString("customerPhoneNumber"));
        customerAddSaveButton.setText(rb.getString("customerAddSaveButton"));
        customerAddCancelButton.setText(rb.getString("customerAddCancelButton"));
        customerPostalCodeLabel.setText(rb.getString("customerPostalCodeLabel"));
    }

    @FXML
    public void initialize() {
//        Sets the local language
        setLanguage();
//        Populates information
        getCustomerInfo();
    }
}


/*
Provide the ability to add, update, and delete customer records
 in the database, including name, address, and phone number.

 F. Write exception controls to prevent each of the following.
  You may use the same mechanism of exception control more than once,
  but you must incorporate at least two different mechanisms of exception control.

• entering nonexistent or invalid customer data

* */