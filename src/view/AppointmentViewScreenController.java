package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class AppointmentViewScreenController {

    @FXML
    private TableView<?> apptTableView;

    @FXML
    private TableColumn<?, ?> titleColumn;

    @FXML
    private TableColumn<?, ?> typeColumn;

    @FXML
    private TableColumn<?, ?> startColumn;

    @FXML
    private TableColumn<?, ?> endColumn;

    @FXML
    private TableColumn<?, ?> customerColumn;

    @FXML
    private TableColumn<?, ?> consultantColumn;

    @FXML
    void handleDeleteAppt(ActionEvent event) {

    }

    @FXML
    void handleEditAppt(ActionEvent event) {

    }

    @FXML
    void handleNewAppt(ActionEvent event) {

    }

}



/*
*
* this should likely have an edit button that moves
* to the appointment edit screen
*
*
* D. Provide the ability to view the calendar by month and by week.
*
* */