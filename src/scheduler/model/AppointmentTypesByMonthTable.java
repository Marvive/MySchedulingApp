package scheduler.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AppointmentTypesByMonthTable {

    private StringProperty month;
    private StringProperty type;
    private StringProperty numberOfAppointments;

//    Constructor
    public AppointmentTypesByMonthTable(String month, String type, String numberOfAppointments) {
        this.month = new SimpleStringProperty(month);
        this.type = new SimpleStringProperty(type);
        this.numberOfAppointments = new SimpleStringProperty(numberOfAppointments);
    }

    public String getTableMonth() {
        return month.get();
    }

    public StringProperty monthProperty() {
        return month;
    }

    public void setTableMonth(String month) {
        this.month.set(month);
    }

    public String getTableType() {
        return type.get();
    }

    public StringProperty tableTypeProperty() {
        return type;
    }

    public void setTableType(String type) {
        this.type.set(type);
    }

    public String getNumberOfAppointments() {
        return numberOfAppointments.get();
    }

    public StringProperty numberOfAppointmentsProperty() {
        return numberOfAppointments;
    }

    public void setNumberOfAppointments(String numberOfAppointments) {
        this.numberOfAppointments.set(numberOfAppointments);
    }
}
