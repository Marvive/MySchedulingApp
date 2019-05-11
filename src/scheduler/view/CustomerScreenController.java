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
import static scheduler.util.DatabaseConnection.updateCustomerRoster;

public class CustomerScreenController {

    @FXML
    private MenuBar menuBar;

    @FXML
    private MenuItem menuBarLogOut;

    @FXML
    private MenuItem menuBarClose;

    @FXML
    private MenuItem menuBarGoMain;

    @FXML
    private MenuItem menuBarGoReports;

    @FXML
    private MenuItem menuBarGoAppointments;

    @FXML
    private Menu menuBarMain;

    @FXML
    private Menu menuBarAppointments;

    @FXML
    private Menu menuBarReports;

    @FXML
    private Menu menuBarFile;

    @FXML
    private TableView<Customer> customerTableView;

    @FXML
    private TableColumn<Customer, String> customerNameColumn;

    @FXML
    private TableColumn<Customer, String> customerAddressColumn;

    @FXML
    private TableColumn<Customer, String> customerAddress2Column;

    @FXML
    private TableColumn<Customer, String> customerCityColumn;

    @FXML
    private TableColumn<Customer, String> customerCountryColumn;

    @FXML
    private TableColumn<Customer, String> customerPhoneColumn;

    @FXML
    private Button customerAddButton;

    @FXML
    private Button customerEditButton;

    @FXML
    private Text customerViewScreenText;

    private static int customerIndexToModify;

//    Returns Customer index to modify
    public static int getCustomerIndexToModify() {
        return customerIndexToModify;
    }

    /**
     * Button Handlers
     */

    @FXML
    void addButtonHandler(ActionEvent event) {
            try {
                Parent addAppointmentParent = FXMLLoader.load(getClass().getResource("CustomerAddScreen.fxml"));
                Scene addAppointmentScene = new Scene(addAppointmentParent);
                Stage addAppointmentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                addAppointmentStage.setScene(addAppointmentScene);
                addAppointmentStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    @FXML
//    TODO
    void editButtonHandler(ActionEvent event) {
//        Grabs customer from Table View
        Customer customerToModify = customerTableView.getSelectionModel().getSelectedItem();
//        Checks if no customer was selected
        if (customerToModify == null) {
            // Create alert saying a customer must be selected to be modified
            ResourceBundle rb = ResourceBundle.getBundle("resources/customerScreen", Locale.getDefault());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(rb.getString("error"));
            alert.setHeaderText(rb.getString("errorHeader"));
            alert.setContentText(rb.getString("errorContent"));
            alert.showAndWait();
            return;
        }
//        Sets index of the customers to be modified
        customerIndexToModify = getCustomerRoster().indexOf(customerToModify);
//        Opens customerEditScreen
        try {
            Parent modifyCustomerParent = FXMLLoader.load(getClass().getResource("CustomerEditScreen.fxml"));
            Scene modifyCustomerScene = new Scene(modifyCustomerParent);
            Stage modifyCustomerStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            modifyCustomerStage.setScene(modifyCustomerScene);
            modifyCustomerStage.show();
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

    public void updateCustomerTableView() {
        updateCustomerRoster();
        customerTableView.setItems(getCustomerRoster());
    }


//    Setting The Language. Used in initialize
    @FXML
    private void setLanguage() {
        ResourceBundle rb = ResourceBundle.getBundle("resources/customerScreen", Locale.getDefault());
        customerNameColumn.setText(rb.getString("customerNameColumn"));
        customerPhoneColumn.setText(rb.getString("customerPhoneColumn"));
        customerViewScreenText.setText(rb.getString("customerScreenText"));
        customerAddressColumn.setText(rb.getString("customerAddressLabel"));
        customerCountryColumn.setText(rb.getString("customerCountryLabel"));
        customerCityColumn.setText(rb.getString("customerCityLabel"));
        customerAddress2Column.setText(rb.getString("customerAddressLabel2"));
        customerAddButton.setText(rb.getString("customerAddButton"));
        customerEditButton.setText(rb.getString("customerEditButton"));

        menuBarLogOut.setText(rb.getString("menuBarLogOut"));
        menuBarClose.setText(rb.getString("menuBarClose"));
        menuBarMain.setText(rb.getString("menuBarMain"));
        menuBarAppointments.setText(rb.getString("menuBarAppointments"));
        menuBarReports.setText(rb.getString("menuBarReports"));
        menuBarFile.setText(rb.getString("menuBarFile"));
        menuBarGoMain.setText(rb.getString("menuBarGoMain"));
        menuBarGoReports.setText(rb.getString("menuBarGoReports"));
        menuBarGoAppointments.setText(rb.getString("menuBarGoAppointments"));

    }

    @FXML
    private void setTableView() {
        customerNameColumn.setCellValueFactory(cellData -> cellData.getValue().customerNameProperty());
        customerAddressColumn.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        customerAddress2Column.setCellValueFactory(cellData -> cellData.getValue().address2Property());
        customerCityColumn.setCellValueFactory(cellData -> cellData.getValue().cityProperty());
        customerCountryColumn.setCellValueFactory(cellData -> cellData.getValue().countryProperty());
        customerPhoneColumn.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
    }


//    Initialize the Screen
    @FXML
    public void initialize() {
//        Sets the language
        setLanguage();
//        Sets the TableView Data
        setTableView();
//        If there are any customer changes, update the Table View
        updateCustomerTableView();
    }
}
