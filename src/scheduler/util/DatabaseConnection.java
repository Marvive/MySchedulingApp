package scheduler.util;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import scheduler.model.Appointment;
import scheduler.model.AppointmentList;
import scheduler.model.Customer;
import scheduler.model.CustomerRoster;
import scheduler.view.LoginController;

import javax.xml.transform.Result;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.*;
import java.time.Instant;
import java.util.Date;
import java.util.*;

/**
 * Server name:  52.206.157.109
 * Database name:  U04zW0
 * Username:  U04zW0
 * Password:  53688393088
 *
 * This will probably be the most complex part of the project since I have to figure out how to properly interact with
 * the SQL database.
 */


public class DatabaseConnection {
    //    Created finals to frequently reference SQL information provided by Ucertify
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String db = "U04zW0";
    private static final String url = "jdbc:mysql://52.206.157.109/" + db;
    private static final String user = "U04zW0";
    private static final String pass = "53688393088";

    private static String currentUser;
    private static int openCount = 0;


    /**
     * Sets the current user for public access
     */
    private static void setCurrentUser(String userName) {
        currentUser = userName;
    }

    /**
     * Method to validate if the user is a valid user
     */
    public static boolean validateLogin(String userName, String password) {
        int userId = getUserId(userName);
        boolean correctPassword = checkPassword(userId, password);
        if (correctPassword) {
            setCurrentUser(userName);
            try {
                Path path = Paths.get("UserLog.txt");
                Files.write(path, Arrays.asList("User " + currentUser + " logged in at " + Date.from(Instant.now()).toString() + "."),
                        StandardCharsets.UTF_8, Files.exists(path) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Method to reference the Usernames grom the SQL Database
     */
    private static int getUserId(String userName) {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        int userId = -1;

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement stmt = conn.createStatement()) {

            ResultSet userIdSet = stmt.executeQuery("SELECT userId FROM user WHERE userName = '" + userName + "'");

            if (userIdSet.next()) {
                userId = userIdSet.getInt("userId");
            }
            userIdSet.close();
        } catch (SQLException e) {
            LoginController.incrementDatabaseError();
        }
        return userId;
    }

    /**
     * Method to reference the password from the SQL Database
     */
    private static boolean checkPassword(int userId, String password) {
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement stmt = conn.createStatement()) {

            ResultSet passwordSet = stmt.executeQuery("SELECT password FROM user WHERE userId = " + userId);
            String dbPassword;
            if (passwordSet.next()) {
                dbPassword = passwordSet.getString("password");
            } else {
                return false;
            }
            passwordSet.close();

            if (dbPassword.equals(password)) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Method to create a notification upon logging in
     */
    public static void loginAppointmentNotification() {
        if (openCount == 0) {
            ObservableList<Appointment> userAppointments = FXCollections.observableArrayList();
            for (Appointment appointment : AppointmentList.getAppointmentList()) {
                if (appointment.getCreatedBy().equals(currentUser)) {
                    userAppointments.add(appointment);
                }
            }
//            For loop to cycle through appointments to see if any for the user are within 15 mins
            for (Appointment appointment : userAppointments) {
//                Creating ability to check for 15 minutes from appointment time
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(Date.from(Instant.now()));
                calendar.add(Calendar.MINUTE, 15);
                Date notificationCutoff = calendar.getTime();
//                Generates message for the notification
                if (appointment.getStartDate().before(notificationCutoff)) {

                    ResourceBundle rb = ResourceBundle.getBundle("MainScreen", Locale.getDefault());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle(rb.getString("notificationUpcomingAppointment"));
                    alert.setContentText(rb.getString("notificationUpcomingAppointmentmessage") + "\n" + rb.getString("lblTitle")
                            + ": " + appointment.getTitle() + "\n" + rb.getString("lblDescription") + ": " + appointment.getDescription() +
                            "\n" + rb.getString("lblLocation") + ": " + appointment.getLocation() + "\n" + rb.getString("lblContact") +
                            ": " + appointment.getContact() + "\n" + rb.getString("lblUrl") + ": " + appointment.getUrl() + "\n" +
                            rb.getString("lblDate") + ": " + appointment.getDateString() + "\n" + rb.getString("lblStartTime") + ": " +
                            appointment.getStartString() + "\n" + rb.getString("lblEndTime") + ": " + appointment.getEndString());
                    alert.showAndWait();
                }
            }
//            Counter is to make sure that the notification only shows up once during login
            openCount++;
        }
    }

    /**
     * Method to update the customer roster when the database changes
     */
    public static void updateCustomerRoster() {
//        Try to connect to DB
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement stmt = conn.createStatement()) {
//            Query the database and clear
            ObservableList<Customer> customerRoster = CustomerRoster.getCustomerRoster();
//            Clear method removes everything from an arraylist. We need to clear in order to update the information
            customerRoster.clear();
//            Queries database to create an INT list of customerId's
            ResultSet customerIdResultSet = stmt.executeQuery("SELECT customerId FROM customer WHERE active = 1");
            ArrayList<Integer> customerIdList = new ArrayList<>();
            while (customerIdResultSet.next()) {
                customerIdList.add(customerIdResultSet.getInt(1));
            }
//            for loop to create a customer object for each customerId in the list, then adds Customer to customerRoster
            for (int customerId : customerIdList) {
//                Creates a customer instance
                Customer customer = new Customer();
//                Query databse for instance
                ResultSet customerResultSet = stmt.executeQuery("SELECT customerName, active, addressId FROM customer WHERE customerId = " + customerId);
                customerResultSet.next();
                String customerName = customerResultSet.getString(1);
                int active = customerResultSet.getInt(2);
                int addressId = customerResultSet.getInt(3);
//                sets information using methods from the Customer class
                customer.setCustomerId(customerId);
                customer.setCustomerName(customerName);
                customer.setActive(active);
                customer.setAddressId(addressId);
//                Queries database for address information
                ResultSet addressResultSet = stmt.executeQuery("SELECT address, address2, postalCode, phone, cityId FROM address WHERE addressId = " + addressId);
                addressResultSet.next();
                String address = addressResultSet.getString(1);
                String address2 = addressResultSet.getString(2);
                String postalCode = addressResultSet.getString(3);
                String phone = addressResultSet.getString(4);
                int cityId = addressResultSet.getInt(5);
//                sets Address information using methods from Customer class
                customer.setAddress(address);
                customer.setAddress2(address2);
                customer.setPostalCode(postalCode);
                customer.setPhone(phone);
                customer.setCityId(cityId);
//                Queries DB again for City information
                ResultSet cityResultSet = stmt.executeQuery("SELECT city, countryId FROM city WHERE cityId = " + cityId);
                cityResultSet.next();
                String city = cityResultSet.getString(1);
                int countryId = cityResultSet.getInt(2);
//                sets City information using methods from Customer class
                customer.setCity(city);
                customer.setCountryId(countryId);
//                Queries DB again for Country information
                ResultSet countryResultSet = stmt.executeQuery("SELECT country FROM country WHERE countryId = " + countryId);
                countryResultSet.next();
                String country = countryResultSet.getString(1);
//                sets Country information using methods from Customer class
                customer.setCountry(country);
//                Adds the new Customer object based on the information to the customerRoster instance
                customerRoster.add(customer);
            }
        } catch (SQLException e) {
            ResourceBundle rb = ResourceBundle.getBundle("DBManager", Locale.getDefault());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(rb.getString("error"));
            alert.setHeaderText(rb.getString("errorConnectingToDatabase"));
            alert.setContentText(rb.getString("errorConnectingToDatabaseMessage"));
            alert.show();
        }
    }

    /**
     * Method to add a customer to the Database. Contains a check to make sure that the user does not already exist.
     */
    public static void addNewCustomer(String customerName, String address, String address2,
                                      String city, String country, String postalCode, String phone) {
//        Try to get the address information
        try {
            int countryId = calculateCountryId(country);
            int cityId = calculateCityId(city, countryId);
            int addressId = calculateAddressId(address, address2, postalCode, phone, cityId);
//            Check if customer is already in the database
            if (checkIfCustomerExists(customerName, addressId)) {
//                if true, then it will query DB
                try (Connection conn = DriverManager.getConnection(url, user, pass);
                     Statement stmt = conn.createStatement()) {
                    ResultSet activeResultSet = stmt.executeQuery("SELECT active FROM customer WHERE " +
                            "customerName = '" + customerName + "' AND addressId = " + addressId);
                    activeResultSet.next();
                    int active = activeResultSet.getInt(1);
//                    Check to show if a customer is active or not
                    if (active == 1) {
//
                        ResourceBundle rb = ResourceBundle.getBundle("DBManager", Locale.getDefault());
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle(rb.getString("error"));
                        alert.setHeaderText(rb.getString("errorAddingCustomer"));
                        alert.setContentText(rb.getString("errorCustomerAlreadyExists"));
                        alert.showAndWait();
                    } else if (active == 0) {
                        setCustomerToActive(customerName, addressId);

                    } else {
                        addCustomer(customerName, addressId);
                    }
                }
            }
        } catch (SQLException e) {
//            Catch block to create an alert notifying the user that an error occurred. This is a catch all but will fire if there is no connection
            ResourceBundle rb = ResourceBundle.getBundle("DBManager", Locale.getDefault());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(rb.getString("error"));
            alert.setHeaderText(rb.getString("errorAddingCustomer"));
            alert.setContentText(rb.getString("errorRequiresDatabase"));
            alert.showAndWait();

        }


    }
    /**
    * Returns the ID if it already exists. Will create a new one if it doesn't
    * */
    public static int calculateCountryId(String country) {
//        Try to connect to DB
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement stmt = conn.createStatement()) {
            ResultSet countryIdCheck = stmt.executeQuery("SELECT countryId FROM country WHERE country = '" + country + "'");
//            Check to see if already exists. Will return the country ID if it does
            if (countryIdCheck.next()) {
                int countryId = countryIdCheck.getInt(1);
                countryIdCheck.close();
                return countryId;
            } else {
                countryIdCheck.close();
                int countryId;
                ResultSet allCountryId = stmt.executeQuery("SELECT countryId FROM country ORDER BY countryId");
//                Adding +1 to the country ID count
                if (allCountryId.last()) {
                    countryId = allCountryId.getInt(1) + 1;
                    allCountryId.close();
                } else {
//                    If none found then it will start the values and close the connection
                    allCountryId.close();
                    countryId = 1;
                }
//                Update databse with the new countryId value
                stmt.executeUpdate("INSERT INTO country VALUES (" + countryId + ", '" + country + "', CURRENT_DATE, " +
                        "'" + currentUser + "', CURRENT_TIMESTAMP, '" + currentUser + "')");
                return countryId;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
    * Method to check or add the CityId
    * */
    public static int calculateCityId(String city, int countryId) {
//        Try to connect to DB
        try (Connection conn = DriverManager.getConnection(url,user,pass);
             Statement stmt = conn.createStatement()) {
            ResultSet cityIdCheck = stmt.executeQuery("SELECT cityId FROM city WHERE city = '" + city + "' AND countryid = " + countryId);
//            Add or check to see if city exists
            if (cityIdCheck.next()) {
                int cityId = cityIdCheck.getInt(1);
                cityIdCheck.close();
                return cityId;
            } else {
                cityIdCheck.close();
                int cityId;
                ResultSet allCityId = stmt.executeQuery("SELECT cityId FROM city ORDER BY cityId");
                // Check last cityId value and add one to it for new cityId value
                if (allCityId.last()) {
                    cityId = allCityId.getInt(1) + 1;
                    allCityId.close();
                }
                // If no values present, set countryId to beginning value of 1
                else {
                    allCityId.close();
                    cityId = 1;
                }
                // Create new entry with new cityId value
                stmt.executeUpdate("INSERT INTO city VALUES (" + cityId + ", '" + city + "', " + countryId + ", CURRENT_DATE, " +
                        "'" + currentUser + "', CURRENT_TIMESTAMP, '" + currentUser + "')");
                return cityId;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }












}