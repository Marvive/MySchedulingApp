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

import static scheduler.util.DatabaseConnection.addNewCustomer;

public class CustomerAddScreenController {

    /**
     * MenuBar FXML
     * */
    @FXML
    private MenuBar menuBar;

    @FXML
    private MenuItem menuBarLogOut;

    @FXML
    private MenuItem menuBarClose;

    @FXML
    private MenuItem menuBarGoReports;

    @FXML
    private MenuItem menuBarGoAppointments;

    @FXML
    private MenuItem menuBarGoMain;

    @FXML
    private Menu menuBarFile;

    @FXML
    private Menu menuBarMain;

    @FXML
    private Menu menuBarAppointments;

    @FXML
    private Menu menuBarReports;

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

    @FXML
    void customerAddCancelHandler(ActionEvent event) {
//        Alert to confirm cancel
        ResourceBundle rb = ResourceBundle.getBundle("resources/customerAddScreen", Locale.getDefault());
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
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void customerAddSaveHandler(ActionEvent event) {
//        Grabs inputs
        String customerName = customerNameTextField.getText();
        String address = customerAddressTextField.getText();
        String address2 = customerAddress2TextField.getText();
        String city = customerCityTextField.getText();
        String country = customerCountryTextField.getText();
        String postalCode = customerPostalCode.getText();
        String phone = customerPhoneTextField.getText();
//        Validates Customer
        String errorMessage = Customer.isCustomerValid(customerName, address, city, country, postalCode, phone);
//        Error Popup Dialog
        if (errorMessage.length() > 0) {
            ResourceBundle rb = ResourceBundle.getBundle("resources/customerAddScreen", Locale.getDefault());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(rb.getString("error"));
            alert.setHeaderText(rb.getString("errorHeader"));
            alert.setContentText(errorMessage);
            alert.showAndWait();
        } else {
//        If no errors, then it will attempt to add customer to DB
            try {
//                Method from Database Connection
                addNewCustomer(customerName, address, address2, city, country, postalCode, phone);
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

    /**
     * Sets the language of the text on the screen Used in the initialize method
     */
    @FXML
    private void setLanguage() {
        ResourceBundle rb = ResourceBundle.getBundle("resources/customerAddScreen", Locale.getDefault());
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
    }
}

