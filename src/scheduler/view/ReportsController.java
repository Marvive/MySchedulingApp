package scheduler.view;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ReportsController {

    @FXML
    private Tab appointmentTab;

    @FXML
    private TableView<?> appointmentTableView;

    @FXML
    private TableColumn<?, ?> appointmentTitleColumn;

    @FXML
    private TableColumn<?, ?> appointmentTypeColumn;

    @FXML
    private TableColumn<?, ?> appointmentStartColumn;

    @FXML
    private TableColumn<?, ?> appointmentEndColumn;

    @FXML
    private TableColumn<?, ?> appointmentCustomerColumn;

    @FXML
    private Tab consultantTab;

    @FXML
    private TableView<?> scheduleTableView;

    @FXML
    private TableColumn<?, ?> scheduleMonthColumn;

    @FXML
    private TableColumn<?, ?> scheduleTypeColumn;

    @FXML
    private TableColumn<?, ?> scheduleAmountColumn;

    @FXML
    private Tab reportTab;


}


/*I. Provide the ability to generate each of the following reports:

• number of appointment types by month

• the schedule for each consultant

• one additional report of your choice*/