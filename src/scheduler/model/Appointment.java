package scheduler.model;

import javafx.beans.property.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

public class Appointment {

    private IntegerProperty appointmentId;
    private IntegerProperty customerId;
    private StringProperty title;
    private StringProperty type;
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

//    Constructor
    public Appointment(int appointmentId, int customerId, String title, String type, String location, String contact,
                       String url, Timestamp startTimestamp, Timestamp endTimestamp, Date startDate, Date endDate, String createdBy) {
        this.appointmentId = new SimpleIntegerProperty(appointmentId);
        this.customerId = new SimpleIntegerProperty(customerId);
        this.title = new SimpleStringProperty(title);
        this.type = new SimpleStringProperty(type);
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

//    Setters
    public void setAppointmentId(int appointmentId) {
        this.appointmentId.set(appointmentId);
    }

    public void setCustomerId(int customerId) {
        this.customerId.set(customerId);
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public void setType(String description) {
        this.type.set(description);
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

//    Getters
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

    public String getType() {
        return this.type.get();
    }

    public StringProperty typeProperty() {
        return this.type;
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

    private final ObjectProperty<LocalDateTime> ldt = new SimpleObjectProperty<LocalDateTime>();

    private final ObjectProperty<ZonedDateTime> zdt = new SimpleObjectProperty<>();

    private final ObjectProperty<Timestamp> timestampObjectProperty = new SimpleObjectProperty<Timestamp>();

    Appointment(LocalDateTime ldt) {
        this.ldt.set(ldt);
    }



    public ObjectProperty<LocalDateTime> startTimeProperty() {

        return ldt;
    }

    public ObjectProperty<LocalDateTime> endTimeProperty() {
        return ldt;
    }
//    public LocalDateTime getLdt() {
//        return ldt.get();
//    }
//
//    public void setLdt(LocalDateTime value) {
//        ldt.set(value);
//    }


//    Appointment Validation
    public static String isAppointmentValid(Customer customer, String title, String appointmentType,
                                            LocalDate appointmentDate, String startTime, String endTime) throws NumberFormatException {
        ResourceBundle rb = ResourceBundle.getBundle("resources/appointment", Locale.getDefault());
        ZoneId zoneID = ZoneId.systemDefault();
        String errorMessage = "";
        try {
//            These will not be else if's because it will not continue checking conditions
            if (customer == null) {
                errorMessage += rb.getString("errorCustomer");
            }
            if (title.length() == 0) {
                errorMessage += rb.getString("errorTitle");
            }
            if (appointmentType == null) {
                errorMessage += rb.getString("errorType");
            }
//        Grabs the strings from the box and converts to localTime
            LocalTime startLocalTime = LocalTime.parse(startTime, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));
            LocalTime endLocalTime = LocalTime.parse(endTime, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));
//        Combines Date and local Time
            LocalDateTime startDateTime = LocalDateTime.of(appointmentDate, startLocalTime);
            LocalDateTime endDateTime = LocalDateTime.of(appointmentDate, endLocalTime);
//        Turns LocalDateTime into ZonedDateTime
            ZonedDateTime startUTC = startDateTime.atZone(zoneID).withZoneSameInstant(ZoneId.of("UTC"));
            ZonedDateTime endUTC = endDateTime.atZone(zoneID).withZoneSameInstant(ZoneId.of("UTC"));
//        Turns ZonedDateTimes into Timestamps to be used with DB
            Timestamp startTimeStamp = Timestamp.valueOf(startUTC.toLocalDateTime());
            Timestamp endTimeStamp = Timestamp.valueOf(endUTC.toLocalDateTime());

//            If Start Time is after end time, it will alert with an error
            if (startTimeStamp.after(endTimeStamp)) {
                errorMessage += rb.getString("startBeforeEnd");
            }
//            If Date is on a weekend, then it will alert no errors allowed "Outside of business Hours"
            if (appointmentDate.getDayOfWeek().toString().toUpperCase().equals("SATURDAY") || appointmentDate.getDayOfWeek().toString().toUpperCase().equals("SUNDAY")) {
                errorMessage += rb.getString("errorNoWeekends");
            }
//            Throws an error if you choose a date or time in the past
            if (appointmentDate.isBefore(LocalDate.now()) || (startLocalTime.isBefore(LocalTime.now())) && appointmentDate.equals(LocalDate.now())) {
                errorMessage += rb.getString("notBeforeNow");
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            return errorMessage;
        }
    }
}
