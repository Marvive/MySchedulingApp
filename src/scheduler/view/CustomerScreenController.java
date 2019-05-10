package scheduler.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

public class CustomerScreenController {

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
    private TableView<?> customerTableView;

    @FXML
    private TableColumn<?, ?> customerNameColumn;

    @FXML
    private TableColumn<?, ?> customerAddressColumn;

    @FXML
    private TableColumn<?, ?> customerAddress2Column;

    @FXML
    private TableColumn<?, ?> customerCityColumn;

    @FXML
    private TableColumn<?, ?> customerCountryColumn;

    @FXML
    private TableColumn<?, ?> customerPhoneColumn;

    @FXML
    private Button customerAddButton;

    @FXML
    private Button customerEditButton;

    @FXML
    private Text customerViewScreenText;

    @FXML
    void addButtonHandler(ActionEvent event) {

    }

    @FXML
    void editButtonHandler(ActionEvent event) {

    }

    @FXML
    void menuBarAppointmentsHandler(ActionEvent event) {

    }

    @FXML
    void menuBarCloseHandler(ActionEvent event) {

    }

    @FXML
    void menuBarLogOutHandler(ActionEvent event) {

    }

    @FXML
    void menuBarMainHandler(ActionEvent event) {

    }

    @FXML
    void menuBarReportsHandler(ActionEvent event) {

    }

}
