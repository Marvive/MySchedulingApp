package scheduler.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

public class Appointment {

    private IntegerProperty appointmentId;
    private IntegerProperty customerId;
    private StringProperty title;
    private StringProperty description;
    private StringProperty location;
    private StringProperty contact;
    private StringProperty url;
    private Timestamp startTimestamp;
    private Timestamp endTimestamp;
    private Date startDate;
    private Date endDate;
    private StringProperty dateString;
    private StringProperty startString;
    private StringProperty endString;
    private StringProperty createdBy;

    // Constructor
    public Appointment(int appointmentId, int customerId, String title, String description, String location, String contact,
                       String url, Timestamp startTimestamp, Timestamp endTimestamp, Date startDate, Date endDate, String createdBy) {
        this.appointmentId = new SimpleIntegerProperty(appointmentId);
        this.customerId = new SimpleIntegerProperty(customerId);
        this.title = new SimpleStringProperty(title);
        this.description = new SimpleStringProperty(description);
        this.location = new SimpleStringProperty(location);
        this.contact = new SimpleStringProperty(contact);
        this.url = new SimpleStringProperty(url);
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.startDate = startDate;
        this.endDate = endDate;
        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
        this.dateString = new SimpleStringProperty(format.format(startDate));
        SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm a z");
        this.startString = new SimpleStringProperty(formatTime.format(startDate));
        this.endString = new SimpleStringProperty(formatTime.format(endDate));
        this.createdBy = new SimpleStringProperty(createdBy);
    }

    // Setters
//    Setters/Mutators
    public void setAppointmentId(int appointmentId) {
        this.appointmentId.set(appointmentId);
    }

    public void setCustomerId(int customerId) {
        this.customerId.set(customerId);
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public void setLocation(String location) {
        this.location.set(location);
    }

    public void setContact(String contact) {
        this.contact.set(contact);
    }

    public void setUrl(String url) {
        this.url.set(url);
    }

    public void setStartTimestamp(Timestamp startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public void setEndTimestamp(Timestamp endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy.set(createdBy);
    }

//    Getters/Accessors
    public int getAppointmentId() {
        return this.appointmentId.get();
    }

    public IntegerProperty appointmentIdProperty() {
        return this.appointmentId;
    }

    public int getCustomerId() {
        return this.customerId.get();
    }

    public IntegerProperty customerIdProperty() {
        return this.customerId;
    }

    public String getTitle() {
        return this.title.get();
    }

    public StringProperty titleProperty() {
        return this.title;
    }

    public String getDescription() {
        return this.description.get();
    }

    public StringProperty descriptionProperty() {
        return this.description;
    }

    public String getLocation() {
        return this.location.get();
    }

    public StringProperty locationProperty() {
        return this.location;
    }

    public String getContact() {
        return this.contact.get();
    }

    public StringProperty contactProperty() {
        return this.contact;
    }

    public String getUrl() {
        return this.url.get();
    }

    public StringProperty urlProperty() {
        return this.url;
    }

    public Timestamp getStartTimestamp() {
        return this.startTimestamp;
    }

    public Timestamp getEndTimestamp() {
        return this.endTimestamp;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public String getDateString() {
        return this.dateString.get();
    }

    public StringProperty dateStringProperty() {
        return this.dateString;
    }

    public String getStartString() {
        return this.startString.get();
    }

    public StringProperty startStringProperty() {
        return this.startString;
    }

    public String getEndString() {
        return this.endString.get();
    }

    public StringProperty endStringProperty() {
        return this.endString;
    }

    public String getCreatedBy() {
        return this.createdBy.get();
    }

    public StringProperty createdByProperty() {
        return this.createdBy;
    }

//    Checks to see whether the appointment can be made
    public static String isAppointmentValid(Customer customer, String title, LocalDate appointmentDate, String startAmPm,
                                            String endAmPm) throws NumberFormatException {
        ResourceBundle rb = ResourceBundle.getBundle("Appointment", Locale.getDefault());
        String errorMessage = "";
//        TODO create a outside of hours error?
        try {
            if (customer == null) {
                errorMessage = errorMessage + rb.getString("errorCustomer");
            } else if (title.length() == 0) {
                errorMessage = errorMessage + rb.getString("errorTitle");
            } else if ((startAmPm.equals("PM") && endAmPm.equals("AM")) || (startAmPm.equals(endAmPm))) {
                errorMessage = errorMessage + rb.getString("errorStartAfterEnd");
            } else if (appointmentDate.getDayOfWeek().toString().toUpperCase().equals("SATURDAY")
                    || appointmentDate.getDayOfWeek().toString().toUpperCase().equals("SUNDAY")) {
                errorMessage = errorMessage + rb.getString("errorDateIsWeekend");
            }
        } catch (NumberFormatException e) {
            errorMessage = errorMessage + rb.getString("errorStartEndInteger");
        } finally {
            return errorMessage;
        }
    }


}
