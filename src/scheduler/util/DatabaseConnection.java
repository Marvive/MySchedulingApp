package scheduler.util;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import scheduler.model.Appointment;
import scheduler.model.AppointmentList;
import scheduler.model.Customer;
import scheduler.model.CustomerRoster;
import scheduler.view.LoginController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZonedDateTime;
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
     * Used by checkLogInCredentials()
     */
    private static void setCurrentUser(String userName) {
        currentUser = userName;
    }

    /**
     * Method to validate if the user is a valid user
     */
    public static boolean checkLogInCredentials(String userName, String password) {
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
     * Used by checkLogInCredentials()
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
     * Method to reference the password from the SQL Database.
     * Used by checkLogInCredentials()
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
//                Generates message for the notification. Calls on methods from appointment class
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
    * Returns the ID if it already exists. Will create a new one if it doesn't.
     * Used by addNewCustomer()
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
    * Method to check or add the CityId.
     * Used by addNewCustomer()
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
//                If the city exists, then add +1 to it
                if (allCityId.last()) {
                    cityId = allCityId.getInt(1) + 1;
                    allCityId.close();
                } else {
//                    else set the ID to 1
                    allCityId.close();
                    cityId = 1;
                }
//                Create the new item if it doesn't exist
                stmt.executeUpdate("INSERT INTO city VALUES (" + cityId + ", '" + city + "', " + countryId + ", CURRENT_DATE, " +
                        "'" + currentUser + "', CURRENT_TIMESTAMP, '" + currentUser + "')");
                return cityId;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
    * Method used to check or add AddressID
     * Used by addNewCustomer()
    * */
    public static int calculateAddressId(String address, String address2, String postalCode, String phone, int cityId) {
//        Try to connect to database
        try (Connection conn = DriverManager.getConnection(url,user,pass);
             Statement stmt = conn.createStatement()) {
            ResultSet addressIdCheck = stmt.executeQuery("SELECT addressId FROM address WHERE address = '" + address + "' AND " +
                    "address2 = '" + address2 + "' AND postalCode = '" + postalCode + "' AND phone = '" + phone + "' AND cityId = " + cityId);
//            If address already exists, else add to ID's
            if (addressIdCheck.next()) {
                int addressId = addressIdCheck.getInt(1);
                addressIdCheck.close();
                return addressId;
            } else {
                addressIdCheck.close();
                int addressId;
                ResultSet allAddressId = stmt.executeQuery("SELECT addressId FROM address ORDER BY addressId");
                if (allAddressId.last()) {
                    addressId = allAddressId.getInt(1) + 1;
                    allAddressId.close();
                } else {
                    allAddressId.close();
                    addressId = 1;
                }
//                Create new Entry
                stmt.executeUpdate("INSERT INTO address VALUES (" + addressId + ", '" + address + "', '" +address2 + "', " + cityId + ", " +
                        "'" + postalCode + "', '" + phone + "', CURRENT_DATE, '" + currentUser + "', CURRENT_TIMESTAMP, '" + currentUser + "')");
                return addressId;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
    * Simple method to see if the customer exists.
     * Used by addNewCustomer().
    * */
    private static boolean checkIfCustomerExists(String customerName, int addressId) throws SQLException {
//        Connect to DB
        try (Connection conn = DriverManager.getConnection(url,user,pass);
             Statement stmt = conn.createStatement()) {
            ResultSet customerIdCheck = stmt.executeQuery("SELECT customerId FROM customer WHERE customerName = '" + customerName + "' " +
                    "AND addressId = " + addressId);
            if (customerIdCheck.next()) {
                customerIdCheck.close();
                return true;
            } else {
                customerIdCheck.close();
                return false;
            }
        }
    }

    /**
    * Method to make a customer active
     * Used by addNewCustomer() and modifyCustomer()
    * */
    public static void setCustomerToActive(String customerName, int addressId) {
        // Try-with-resources block for database connection
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement stmt = conn.createStatement()) {
            ResourceBundle rb = ResourceBundle.getBundle("DBManager", Locale.getDefault());
//            Alert and confirmation before setting a customer to active
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle(rb.getString("error"));
            alert.setHeaderText(rb.getString("errorModifyingCustomer"));
            alert.setContentText(rb.getString("errorSetToActive"));
            Optional<ButtonType> result = alert.showAndWait();
//            If OK button is clicked, then it will update the SQL database
            if (result.get() == ButtonType.OK) {
                stmt.executeUpdate("UPDATE customer SET active = 1, lastUpdate = CURRENT_TIMESTAMP, " +
                        "lastUpdateBy = '" + currentUser + "' WHERE customerName = '" + customerName + "' AND addressId = " + addressId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
    * Method to add customer used by addNewCustomer()
    * */
    private static void addCustomer(String customerName, int addressId) throws SQLException {
//        Connect to DB
        try (Connection conn = DriverManager.getConnection(url,user,pass);
             Statement stmt = conn.createStatement()) {
            ResultSet allCustomerId = stmt.executeQuery("SELECT customerId FROM customer ORDER BY customerId");
            int customerId;
//            If ID exists, add +1 to last value, else set to 1
            if (allCustomerId.last()) {
                customerId = allCustomerId.getInt(1) + 1;
                allCustomerId.close();
            } else {
                allCustomerId.close();
                customerId = 1;
            }
//            Creates entry to customerId
            stmt.executeUpdate("INSERT INTO customer VALUES (" + customerId + ", '" + customerName + "', " + addressId + ", 1, " +
                    "CURRENT_DATE, '" + currentUser + "', CURRENT_TIMESTAMP, '" + currentUser + "')");
        }
    }

    /**
    * Method to modify existing customer. Verifies that it exsists first
    * */
    public static int modifyCustomer(int customerId, String customerName, String address, String address2,
                                     String city, String country, String postalCode, String phone) {
        try {
            // Find customer's country, city and addressId's
            int countryId = calculateCountryId(country);
            int cityId = calculateCityId(city, countryId);
            int addressId = calculateAddressId(address, address2, postalCode, phone, cityId);
            // Check if customer already exists in the database
            if (checkIfCustomerExists(customerName, addressId)) {
                int existingCustomerId = getCustomerId(customerName, addressId);
                int activeStatus = getActiveStatus(existingCustomerId);
                return activeStatus;
            } else {
//                Cleans up database after updating the customer
                updateCustomer(customerId, customerName, addressId);
                cleanDatabase();
                return -1;
            }
        } catch (SQLException e) {
//            Alerts that you need to be connected to the database
            ResourceBundle rb = ResourceBundle.getBundle("DBManager", Locale.getDefault());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(rb.getString("error"));
            alert.setHeaderText(rb.getString("errorModifyingCustomer"));
            alert.setContentText(rb.getString("errorRequiresDatabase"));
            alert.showAndWait();
            return -1;
        }
    }

    /**
     * Method to grab Customer ID
     * Used by modifyCustomer()
     * */
    private static int getCustomerId(String customerName, int addressId) throws SQLException {
//        Connect to DB
        try (Connection conn = DriverManager.getConnection(url,user,pass);
             Statement stmt = conn.createStatement()) {
            ResultSet customerIdResultSet = stmt.executeQuery("SELECT customerId FROM customer WHERE customerName = '" + customerName + "' AND addressId = " + addressId);
            customerIdResultSet.next();
            int customerId = customerIdResultSet.getInt(1);
            return customerId;
        }
    }

    /**
     * Method to see whether customer is active or not
     * Used by modifyCustomer()
    * */
    private static int getActiveStatus(int customerId) throws SQLException {
//        Connect to database
        try (Connection conn = DriverManager.getConnection(url,user,pass);
             Statement stmt = conn.createStatement()) {
            ResultSet activeResultSet = stmt.executeQuery("SELECT active FROM customer WHERE customerId = " + customerId);
            activeResultSet.next();
            int active = activeResultSet.getInt(1);
            return active;
        }
    }

    /**
    * Method that updates customer.
     * Used by modifyCustomer()
    * */
    private static void updateCustomer(int customerId, String customerName, int addressId) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url,user,pass);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("UPDATE customer SET customerName = '" + customerName + "', addressId = " + addressId + ", " +
                    "lastUpdate = CURRENT_TIMESTAMP, lastUpdateBy = '" + currentUser + "' WHERE customerId = " + customerId);
        }
    }

    /**
    * Sets a customer to inactive and makes them hidden in the customer list view
    * */
    public static void setCustomerToInactive(Customer customerToRemove) {
        int customerId = customerToRemove.getCustomerId();
        ResourceBundle rb = ResourceBundle.getBundle("DBManager", Locale.getDefault());
//        Set Confirmation to delete customer
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle(rb.getString("confirmRemove"));
        alert.setHeaderText(rb.getString("confirmRemovingCustomer"));
        alert.setContentText(rb.getString("confirmRemovingCustomerMessage"));
        Optional<ButtonType> result = alert.showAndWait();
//        If OK is pressed, then proceed. Catches errors if cannot connect to DB
        if (result.get() == ButtonType.OK) {
            try (Connection conn = DriverManager.getConnection(url,user,pass);
                 Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("UPDATE customer SET active = 0 WHERE customerId = " + customerId);
            } catch (SQLException e) {
//                If it fails it will throw this database error
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                alert2.setTitle(rb.getString("error"));
                alert2.setHeaderText(rb.getString("errorModifyingCustomer"));
                alert2.setContentText(rb.getString("errorRequiresDatabase"));
                alert2.showAndWait();
            }
//            Will pass the information into the roster
            updateCustomerRoster();
        }
    }



    /**
     * Will update the appointmentList with future appointments
     * */
    public static void updateAppointmentList() {
//        DB Connection
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement stmt = conn.createStatement()) {
//            Calls getAppointmentList() from AppointmentList
            ObservableList<Appointment> appointmentList = AppointmentList.getAppointmentList();
            appointmentList.clear();
//            Create list of appointmentId's for all appointments that are in the future
            ResultSet appointmentResultSet = stmt.executeQuery("SELECT appointmentId FROM appointment WHERE start >= CURRENT_TIMESTAMP");
            ArrayList<Integer> appointmentIdList = new ArrayList<>();
            while(appointmentResultSet.next()) {
                appointmentIdList.add(appointmentResultSet.getInt(1));
            }
//            For Loop to create Appoinment for each appointmendId in list and adds the object to appointmentList
            for (int appointmentId : appointmentIdList) {
//                Queries database for appointment information
                appointmentResultSet = stmt.executeQuery("SELECT customerId, title, description, location, contact, url, start, end, createdBy FROM appointment WHERE appointmentId = " + appointmentId);
                appointmentResultSet.next();
                int customerId = appointmentResultSet.getInt(1);
                String title = appointmentResultSet.getString(2);
                String description = appointmentResultSet.getString(3);
                String location = appointmentResultSet.getString(4);
                String contact = appointmentResultSet.getString(5);
                String url = appointmentResultSet.getString(6);
                Timestamp startTimestamp = appointmentResultSet.getTimestamp(7);
                Timestamp endTimestamp = appointmentResultSet.getTimestamp(8);
                String createdBy = appointmentResultSet.getString(9);
//                Changes startTimestamp & endTimestamp to ZonedDateTimes
                DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
                utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                java.util.Date startDate = utcFormat.parse(startTimestamp.toString());
                java.util.Date endDate = utcFormat.parse(endTimestamp.toString());
//                Adds information to the Appointment instance
                Appointment appointment = new Appointment(appointmentId, customerId, title, description, location, contact, url, startTimestamp, endTimestamp, startDate, endDate, createdBy);
//                Adds instance to appointmentList
                appointmentList.add(appointment);
            }
        } catch (SQLException e) {
//            Catches SQL errors
            ResourceBundle rb = ResourceBundle.getBundle("DBManager", Locale.getDefault());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(rb.getString("error"));
            alert.setHeaderText(rb.getString("errorAddingAppointment"));
            alert.setContentText(rb.getString("errorRequiresDatabase"));
            alert.showAndWait();
        } catch (Exception e) {
//            If an error occurs that isn't a SQL error, this part catches it
//            TODO add error2, errorAddingAppointment2, errorRequiresDatabase2 to ResourceBundle
            ResourceBundle rb = ResourceBundle.getBundle("DBManager", Locale.getDefault());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(rb.getString("error2"));
            alert.setHeaderText(rb.getString("errorAddingAppointment2"));
            alert.setContentText(rb.getString("errorRequiresDatabase2"));
            alert.showAndWait();
        }
    }

    // Add appointment to database if entry does not already exist
    /**
     * Adds appointment to database unless it exists
     * */
    public static boolean addNewAppointment(Customer customer, String title, String description, String location, String contact, String url, ZonedDateTime startUTC, ZonedDateTime endUTC) {
//        Change ZonedDateTimes to Timestamps
        String startUTCString = startUTC.toString();
        startUTCString = startUTCString.substring(0,10) + " " + startUTCString.substring(11,16) + ":00";
        Timestamp startTimestamp = Timestamp.valueOf(startUTCString);
        String endUTCString = endUTC.toString();
        endUTCString = endUTCString.substring(0,10) + " " + endUTCString.substring(11,16) + ":00";
        Timestamp endTimestamp = Timestamp.valueOf(endUTCString);
//        Checks to make sure that added appointment does not overlap with others, then adds it if it doesn't
        if (doesAppointmentOverlap(startTimestamp, endTimestamp)) {
            ResourceBundle rb = ResourceBundle.getBundle("DBManager", Locale.getDefault());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(rb.getString("error"));
            alert.setHeaderText(rb.getString("errorAddingAppointment"));
            alert.setContentText(rb.getString("errorAppointmentOverlaps"));
            alert.showAndWait();
            return false;
        } else {
            int customerId = customer.getCustomerId();
            addAppointment(customerId, title, description, location, contact, url, startTimestamp, endTimestamp);
            return true;
        }
    }





}



