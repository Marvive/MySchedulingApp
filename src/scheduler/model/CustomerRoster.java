package scheduler.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class CustomerRoster {

    private static ObservableList<Customer> customerRoster = FXCollections.observableArrayList();

//    customerRoster getter
    public static ObservableList<Customer> getCustomerRoster() {
        return customerRoster;
    }
}

