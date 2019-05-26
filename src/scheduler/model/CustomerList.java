package scheduler.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class CustomerList {

    private static ObservableList<Customer> customerList = FXCollections.observableArrayList();

//    customerList getter
    public static ObservableList<Customer> getCustomerList() {
        return customerList;
    }
}

