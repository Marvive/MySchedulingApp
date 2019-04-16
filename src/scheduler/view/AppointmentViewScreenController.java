package scheduler.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.Locale;
import java.util.ResourceBundle;

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

    /**
     * Set's language for all the text on the screen
     * */
//    TODO
    @FXML
    private void setLanguage() {
        ResourceBundle rb = ResourceBundle.getBundle("AddModifyAppointment", Locale.getDefault());
        tvAppointmentSummaryTitleColumn.setText(rb.getString("lblTitle"));
        tvAppointmentSummaryDateColumn.setText(rb.getString("lblDate"));
        tvAppointmentSummaryContactColumn.setText(rb.getString("lblContact"));
        btnAppointmentSummaryGetInfo.setText(rb.getString("btnGetInfo"));
        btnAppointmentSummaryModify.setText(rb.getString("btnModify"));
        btnAppointmentSummaryDelete.setText(rb.getString("btnDelete"));
        btnAppointmentSummaryExit.setText(rb.getString("btnExit"));
        lblAppointmentSummaryTitle.setText(rb.getString("lblTitle") + ":");
        lblAppointmentSummaryDescription.setText(rb.getString("lblDescription") + ":");
        lblAppointmentSummaryLocation.setText(rb.getString("lblLocation") + ":");
        lblAppointmentSummaryContact.setText(rb.getString("lblContact") + ":");
        lblAppointmentSummaryURL.setText(rb.getString("lblUrl") + ":");
        lblAppointmentSummaryDate.setText(rb.getString("lblDate") + ":");
        lblAppointmentSummaryStartTime.setText(rb.getString("lblStartTime") + ":");
        lblAppointmentSummaryEndTime.setText(rb.getString("lblEndTime") + ":");
        lblAppointmentSummaryCreatedBy.setText(rb.getString("lblCreatedBy"));
    }
}



/*
*
* this should likely have an edit button that moves
* to the appointment edit screen
*
*
* D. Provide the ability to scheduler.view the calendar by month and by week.
*
* */