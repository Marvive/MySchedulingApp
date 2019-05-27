package scheduler.model;

import javafx.beans.property.*;

public class CustomerAppointmentsTable {

    private StringProperty customer;
    private StringProperty consultation;
    private StringProperty followUp;
    private StringProperty newAccount;
    private StringProperty closeAccount;
    private StringProperty total;

    public CustomerAppointmentsTable(String customer, String counter1, String counter2, String counter3, String counter4, String counterTotal) {
        this.customer = new SimpleStringProperty(customer);
        this.consultation = new SimpleStringProperty(counter1);
        this.followUp = new SimpleStringProperty(counter2);
        this.newAccount = new SimpleStringProperty(counter3);
        this.closeAccount = new SimpleStringProperty(counter4);
        this.total = new SimpleStringProperty(counterTotal);
    }

    public String getCustomer() {
        return customer.get();
    }

    public StringProperty customerProperty() {
        return customer;
    }

    public String getConsultation() {
        return consultation.get();
    }

    public StringProperty consultationProperty() {
        return consultation;
    }

    public String getFollowUp() {
        return followUp.get();
    }

    public StringProperty followUpProperty() {
        return followUp;
    }

    public String getNewAccount() {
        return newAccount.get();
    }

    public StringProperty newAccountProperty() {
        return newAccount;
    }

    public String getCloseAccount() {
        return closeAccount.get();
    }

    public StringProperty closeAccountProperty() {
        return closeAccount;
    }

    public String getTotal() {
        return total.get();
    }

    public StringProperty totalProperty() {
        return total;
    }
}
