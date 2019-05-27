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
import scheduler.util.DatabaseConnection;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import static scheduler.model.CustomerList.getCustomerList;
import static scheduler.util.DatabaseConnection.setCustomerToInactive;

public class CustomerScreenController {

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
    private Button deleteButton;

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
    private void addButtonHandler(ActionEvent event) {
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
    private void editButtonHandler(ActionEvent event) {
//        Grabs customer from Table View
        Customer customerToModify = customerTableView.getSelectionModel().getSelectedItem();
//        Checks if no customer was selected
        if (customerToModify == null) {
            ResourceBundle rb = ResourceBundle.getBundle("resources/customerScreen", Locale.getDefault());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(rb.getString("error"));
            alert.setHeaderText(rb.getString("errorHeader"));
            alert.setContentText(rb.getString("errorContent"));
            alert.showAndWait();
            return;
        }
//        Sets index of the customers to be modified
        customerIndexToModify = getCustomerList().indexOf(customerToModify);
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
     * Deletes the selected customer from the Table then sets customer to inactive
     * */
    @FXML
    private void deleteButtonHandler(ActionEvent event) {
//        Grab selected customer
        Customer deletedCustomer = customerTableView.getSelectionModel().getSelectedItem();
//        Throw an alert if no customer was selected
        if (deletedCustomer == null) {
            ResourceBundle rb = ResourceBundle.getBundle("resources/MainScreen", Locale.getDefault());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(rb.getString("error"));
            alert.setHeaderText(rb.getString("selectDeleteHeader"));
            alert.setContentText(rb.getString("selectDeleteContent"));
            alert.showAndWait();
            return;
        }
//        deactivates and removes customer from DB
        setCustomerToInactive(deletedCustomer);
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
        ResourceBundle rb = ResourceBundle.getBundle("resources/MainScreen", Locale.getDefault());
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle(rb.getString("exitTitle"));
        alert.setHeaderText(rb.getString("exitHeader"));
        alert.setContentText(rb.getString("exitContent"));
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            System.exit(0);
        } else {
            System.out.println(rb.getString("cancelledExit"));
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

    private void updateCustomerTableView() {
        DatabaseConnection.updateCustomerList();
        customerTableView.setItems(getCustomerList());
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
        deleteButton.setText(rb.getString("deleteButton"));

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
//        Lambdas to set the values of the tableView cells
        customerNameColumn.setCellValueFactory(cellData -> cellData.getValue().customerNameProperty());
        customerAddressColumn.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        customerAddress2Column.setCellValueFactory(cellData -> cellData.getValue().address2Property());
        customerCityColumn.setCellValueFactory(cellData -> cellData.getValue().cityProperty());
        customerCountryColumn.setCellValueFactory(cellData -> cellData.getValue().countryProperty());
        customerPhoneColumn.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
    }

    @FXML
    public void initialize() {
//        Sets the language
        setLanguage();
//        If there are any customer changes, update the Table View
        updateCustomerTableView();
//        Sets the TableView Data
        setTableView();
    }
}
