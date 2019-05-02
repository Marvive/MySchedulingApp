package scheduler.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import scheduler.model.Customer;

import java.util.Locale;
import java.util.ResourceBundle;

public class CustomerScreenController {

    @FXML
    private TableView<Class> customerTableView;

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
    private TextField customerPhoneTextField;

    @FXML
    private ComboBox<?> customerCityComboBox;

    @FXML
    private Button customerNewButton;

    @FXML
    private Button customerEditButton;

    @FXML
    private Button customerDeleteButton;

    @FXML
    void customerDeleteButtonHandler(ActionEvent event) {
        ResourceBundle rb = ResourceBundle.getBundle("customers", Locale.getDefault());
    }

    @FXML
    void customerEditButtonHandler(ActionEvent event) {
        ResourceBundle rb = ResourceBundle.getBundle("customers", Locale.getDefault());
    }

    @FXML
    void customerNewButtonHandler(ActionEvent event) {
        ResourceBundle rb = ResourceBundle.getBundle("customers", Locale.getDefault());
    }



    private ResourceBundle rb1 = ResourceBundle.getBundle("customers", Locale.getDefault());
    @FXML
    private ComboBox comboBox;
    /**
     * Sets that Data in the combo box
     * Used in initialize
     * TODO Placeholder for City names
     * */
    private void setData() {
        comboBox.getItems().clear();
        comboBox.getItems().addAll(
                rb1.getString("City1"),
                rb1.getString("City2"),
                rb1.getString("City3")
        );

    }


    // Set labels to local language (default is English)
    @FXML
    private void setLanguage() {
        ResourceBundle rb = ResourceBundle.getBundle("customers", Locale.getDefault());
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
//        TODO Add combo box
    }







    // Initialize screen elements
    @FXML
    public void initialize() {
        // Set local language
        setLanguage();
        setData();
        // Assign actions to buttons
        btnModifyCustomerSave.setOnAction(event -> saveModifyCustomer(event));
        btnModifyCustomerCancel.setOnAction(event -> cancelModifyCustomer(event));
        // Get customer to be modified via index
        customer = getCustomerRoster().get(customerIndexToModify);
        // Get customer information
        String customerName = customer.getCustomerName();
        String address = customer.getAddress();
        String address2 = customer.getAddress2();
        String city = customer.getCity();
        String country = customer.getCountry();
        String postalCode = customer.getPostalCode();
        String phone = customer.getPhone();
        // Populate information fields with current customer information
        txtModifyCustomerName.setText(customerName);
        txtModifyCustomerAddress.setText(address);
        txtModifyCustomerAddress2.setText(address2);
        txtModifyCustomerCity.setText(city);
        txtModifyCustomerCountry.setText(country);
        txtModifyCustomerPostalCode.setText(postalCode);
        txtModifyCustomerPhone.setText(phone);
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