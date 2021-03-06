package scheduler.util;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import scheduler.model.Appointment;
import scheduler.model.AppointmentList;
import scheduler.model.Customer;
import scheduler.model.CustomerList;
import scheduler.view.AppointmentViewScreenController;
import scheduler.view.LoginController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
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
 * This will probably be the most complex part of the project since I have to figure
 * out how to properly interact with the SQL database.
 */

public class DatabaseConnection {
//    Created finals to frequently reference SQL information provided by UCertify
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String db = "U04zW0";
    private static final String url = "jdbc:mysql://52.206.157.109/" + db;
    private static final String user = "U04zW0";
    private static final String pass = "53688393088";
    private static String currentUser;
    private static int openCount = 0;

    /**
     * Sets the current user for public access
     * Used by checkLogInInputs()
     */
    private static void setCurrentUser(String userName) {
        currentUser = userName;
    }

    /**
     * Method to validate if the user is a valid user
     */
    public static boolean checkLogInInputs(String userName, String password) {
        int userId = getUserId(userName);
        boolean correctPassword = checkPassword(userId, password);
        if (correctPassword) {
            setCurrentUser(userName);
            try {
                Path path = Paths.get("LogIns.txt");
                Files.write(path, Collections.singletonList("Consultant, " + currentUser + " logged in at " + Date.from(Instant.now()).toString() + "."),
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
     * Method to reference the UserNames from the SQL Database
     * Used by checkLogInInputs()
     */
    private static int getUserId(String userName) {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        int userId = -1;
        try (Connection connection = DriverManager.getConnection(url, user, pass);
             Statement statement = connection.createStatement()) {
            ResultSet userIdSet = statement.executeQuery("SELECT userId FROM user WHERE userName = '" + userName + "'");
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
     * Used by checkLogInInputs()
     */
    private static boolean checkPassword(int userId, String password) {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try (Connection connection = DriverManager.getConnection(url, user, pass);
             Statement statement = connection.createStatement()) {

            ResultSet passwordSet = statement.executeQuery("SELECT password FROM user WHERE userId = " + userId);
            String dbPassword;
            if (passwordSet.next()) {
                dbPassword = passwordSet.getString("password");
            } else {
                return false;
            }
            passwordSet.close();
            return dbPassword.equals(password);
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
            ObservableList<Appointment> consultantAppointments = FXCollections.observableArrayList();
            for (Appointment appointment : AppointmentList.getAppointmentList()) {
                if (appointment.getCreatedBy().equals(currentUser)) {
                    consultantAppointments.add(appointment);
                }
            }
//            For loop to cycle through appointments to see if any for the user are within 15 minutess
            for (Appointment appointment : consultantAppointments) {
//                Creating ability to check for 15 minutes from appointment time
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(Date.from(Instant.now()));
                calendar.add(Calendar.MINUTE, 15);
                Date notificationCutoff = calendar.getTime();
//                Generates message for the notification. Calls on methods from appointment class
                if (appointment.getStartDate().before(notificationCutoff)) {
                    ResourceBundle rb = ResourceBundle.getBundle("resources/MainScreen", Locale.getDefault());
                    Alert alertAppointmentWithinFifteen = new Alert(Alert.AlertType.INFORMATION);
                    alertAppointmentWithinFifteen.setTitle(rb.getString("upcomingNotificationTitle"));
                    alertAppointmentWithinFifteen.setContentText(rb.getString("upcomingNotificationText") + "\n" + rb.getString("title")
                            + ": " + appointment.getTitle() + "\n" + rb.getString("description") + ": " + appointment.getTitle() +
                            "\n" + rb.getString("contact") + ": " + appointment.getContact() + "\n" +
                            rb.getString("date") + ": " + appointment.getDateString() + "\n" + rb.getString("startTime") + ": " +
                            appointment.getStartString() + "\n" + rb.getString("endTime") + ": " + appointment.getEndString());
                    alertAppointmentWithinFifteen.showAndWait();
                }
            }
//            Counter is to make sure that the notification only shows up once during login
            openCount++;
        }
    }

    /**
     * Method to update the customer roster when the database changes
     */
    public static void updateCustomerList() {
        ResourceBundle rb = ResourceBundle.getBundle("resources/databaseConnection", Locale.getDefault());
//        Try to connect to DB
        try (Connection connection = DriverManager.getConnection(url, user, pass);
             Statement statement = connection.createStatement()) {
//            Query the database and clear
            ObservableList<Customer> customerList = CustomerList.getCustomerList();
//            Clear method removes everything from an arrayList. We need to clear in order to update the information
            customerList.clear();
//            Queries database to create an INT list of customerId's
            ResultSet customerIdResultSet = statement.executeQuery("SELECT customerId FROM customer WHERE active = 1");
            ArrayList<Integer> customerIdList = new ArrayList<>();
            while (customerIdResultSet.next()) {
                customerIdList.add(customerIdResultSet.getInt(1));
            }
//            for loop to create a customer object for each customerId in the list, then adds Customer to customerList
            for (int customerId : customerIdList) {
//                Creates a customer instance
                Customer customer = new Customer();
//                Query database for instance
                ResultSet customerResultSet = statement.executeQuery("SELECT customerName, active, addressId FROM customer WHERE customerId = " + customerId);
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
                ResultSet addressResultSet = statement.executeQuery("SELECT address, address2, postalCode, phone, cityId FROM address WHERE addressId = " + addressId);
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
                ResultSet cityResultSet = statement.executeQuery("SELECT city, countryId FROM city WHERE cityId = " + cityId);
                cityResultSet.next();
                String city = cityResultSet.getString(1);
                int countryId = cityResultSet.getInt(2);
//                sets City information using methods from Customer class
                customer.setCity(city);
                customer.setCountryId(countryId);
//                Queries DB again for Country information
                ResultSet countryResultSet = statement.executeQuery("SELECT country FROM country WHERE countryId = " + countryId);
                countryResultSet.next();
                String country = countryResultSet.getString(1);
//                sets Country information using methods from Customer class
                customer.setCountry(country);
//                Adds the new Customer object based on the information to the customerList instance
                customerList.add(customer);
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rb.getString("errorTitle"));
            alert.setHeaderText(rb.getString("noDatabaseConnectionHeader"));
            alert.setContentText(rb.getString("noDatabaseConnectionContent"));
            alert.show();
        } catch (Exception e) {
//            Catches other failures
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setTitle(rb.getString("errorTitle"));
            alert1.setHeaderText(rb.getString("nonDatabaseErrorHeader"));
            alert1.setContentText(rb.getString("nonDatabaseErrorText"));
            alert1.showAndWait();
        }
    }

    /**
     * Method to add a customer to the Database. Contains a check to make sure that the user does not already exist.
     */
    public static void addNewCustomer(String customerName, String address, String address2,
                                      String city, String country, String postalCode, String phone) {
        ResourceBundle rb = ResourceBundle.getBundle("resources/databaseConnection", Locale.getDefault());
//        Try to get the address information
        try {
            int countryId = calculateCountryId(country);
            int cityId = calculateCityId(city, countryId);
            int addressId = calculateAddressId(address, address2, postalCode, phone, cityId);
//            Check if customer is already in the database
            if (checkIfCustomerExists(customerName, addressId)) {
//                if true, then it will query DB
                try (Connection connection = DriverManager.getConnection(url, user, pass);
                     Statement statement = connection.createStatement()) {
                    ResultSet activeResultSet = statement.executeQuery("SELECT active FROM customer WHERE " +
                            "customerName = '" + customerName + "' AND addressId = " + addressId);
                    activeResultSet.next();
                    int active = activeResultSet.getInt(1);
//                    Check to show if a customer is active or not
                    if (active == 1) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle(rb.getString("errorTitle"));
                        alert.setHeaderText(rb.getString("errorAddingCustomer"));
                        alert.setContentText(rb.getString("errorCustomerAlreadyExists"));
                        alert.showAndWait();
                    } else if (active == 0) {
                        setCustomerToActive(customerName, addressId);
                    }
                }
            } else {
                addCustomer(customerName, addressId);
            }
        } catch (SQLException e) {
//            Catch block to create an alert notifying the user that an error occurred.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rb.getString("errorTitle"));
            alert.setHeaderText(rb.getString("noDatabaseConnectionHeader"));
            alert.setContentText(rb.getString("noDatabaseConnectionContent"));
            alert.showAndWait();
        } catch (Exception e) {
//            Catches other failures
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setTitle(rb.getString("errorTitle"));
            alert1.setHeaderText(rb.getString("nonDatabaseErrorHeader"));
            alert1.setContentText(rb.getString("nonDatabaseErrorText"));
            alert1.showAndWait();
        }
    }


    /**
    * Returns the ID if it already exists. Will create a new one if it doesn't.
     * Used by addNewCustomer()
    * */
    public static int calculateCountryId(String country) {
//        Try to connect to DB
        try (Connection connection = DriverManager.getConnection(url, user, pass);
             Statement statement = connection.createStatement()) {
            ResultSet countryIdCheck = statement.executeQuery("SELECT countryId FROM country WHERE country = '" + country + "'");
//            Check to see if already exists. Will return the country ID if it does
            if (countryIdCheck.next()) {
                int countryId = countryIdCheck.getInt(1);
                countryIdCheck.close();
                return countryId;
            } else {
                countryIdCheck.close();
                int countryId;
                ResultSet allCountryId = statement.executeQuery("SELECT countryId FROM country ORDER BY countryId");
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
                statement.executeUpdate("INSERT INTO country VALUES (" + countryId + ", '" + country + "', CURRENT_DATE, " +
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
        try (Connection connection = DriverManager.getConnection(url,user,pass);
             Statement statement = connection.createStatement()) {
            ResultSet cityIdCheck = statement.executeQuery("SELECT cityId FROM city WHERE city = '"
                    + city + "' AND countryid = " + countryId);
//            Add or check to see if city exists
            if (cityIdCheck.next()) {
                int cityId = cityIdCheck.getInt(1);
                cityIdCheck.close();
                return cityId;
            } else {
                cityIdCheck.close();
                int cityId;
                ResultSet allCityId = statement.executeQuery("SELECT cityId FROM city ORDER BY cityId");
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
                statement.executeUpdate("INSERT INTO city VALUES (" + cityId + ", '" + city + "', " + countryId +
                        ", CURRENT_DATE, " + "'" + currentUser + "', CURRENT_TIMESTAMP, '" + currentUser + "')");
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
        try (Connection connection = DriverManager.getConnection(url,user,pass);
             Statement statement = connection.createStatement()) {
            ResultSet addressIdCheck = statement.executeQuery("SELECT addressId FROM address WHERE address = '" + address + "' AND " +
                    "address2 = '" + address2 + "' AND postalCode = '" + postalCode + "' AND phone = '" + phone + "' AND cityId = " + cityId);
//            If address already exists, else add to ID's
            if (addressIdCheck.next()) {
                int addressId = addressIdCheck.getInt(1);
                addressIdCheck.close();
                return addressId;
            } else {
                addressIdCheck.close();
                int addressId;
                ResultSet allAddressId = statement.executeQuery("SELECT addressId FROM address ORDER BY addressId");
                if (allAddressId.last()) {
                    addressId = allAddressId.getInt(1) + 1;
                    allAddressId.close();
                } else {
                    allAddressId.close();
                    addressId = 1;
                }
//                Create new Entry
                statement.executeUpdate("INSERT INTO address VALUES (" + addressId + ", '" + address + "', '" +address2 + "', " + cityId + ", " +
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
        try (Connection connection = DriverManager.getConnection(url,user,pass);
             Statement statement = connection.createStatement()) {
            ResultSet customerIdCheck = statement.executeQuery("SELECT customerId FROM customer WHERE customerName = '"
                    + customerName + "' " + "AND addressId = " + addressId);
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
     * Used by addNewCustomer() and editCustomer()
    * */
    public static void setCustomerToActive(String customerName, int addressId) {
        try (Connection connection = DriverManager.getConnection(url, user, pass);
             Statement statement = connection.createStatement()) {
            ResourceBundle rb = ResourceBundle.getBundle("resources/databaseConnection", Locale.getDefault());
//            Alert and confirmation before setting a customer to active
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle(rb.getString("errorTitle"));
            alert.setHeaderText(rb.getString("errorEditingCustomer"));
            alert.setContentText(rb.getString("errorSetToActive"));
            Optional<ButtonType> result = alert.showAndWait();
//            If OK button is clicked, then it will update the SQL database
            if (result.get() == ButtonType.OK) {
                statement.executeUpdate("UPDATE customer SET active = 1, lastUpdate = CURRENT_TIMESTAMP, " +
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
        try (Connection connection = DriverManager.getConnection(url,user,pass);
             Statement statement = connection.createStatement()) {
            ResultSet allCustomerId = statement.executeQuery("SELECT customerId FROM customer ORDER BY customerId");
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
            statement.executeUpdate("INSERT INTO customer VALUES (" + customerId + ", '" + customerName + "', " +
                    addressId + ", 1, " + "CURRENT_DATE, '" + currentUser + "', CURRENT_TIMESTAMP, '" + currentUser + "')");
        }
    }

    /**
    * Method to modify existing customer. Verifies that it exists first
    * */
    public static int editCustomer(int customerId, String customerName, String address, String address2,
                                     String city, String country, String postalCode, String phone) {
        ResourceBundle rb = ResourceBundle.getBundle("resources/databaseConnection", Locale.getDefault());
        try {
//            Get Customer's country, city and addressID
            int countryId = calculateCountryId(country);
            int cityId = calculateCityId(city, countryId);
            int addressId = calculateAddressId(address, address2, postalCode, phone, cityId);
//            Check if customer already exists in DB
            if (checkIfCustomerExists(customerName, addressId)) {
                int existingCustomerId = getCustomerId(customerName, addressId);
                return getActiveStatus(existingCustomerId);
            } else {
//                Cleans up database after updating the customer
                updateCustomer(customerId, customerName, addressId);
                cleanDatabase();
                return -1;
            }
        } catch (SQLException e) {
//            Alerts that you need to be connected to the database
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rb.getString("errorTitle"));
            alert.setHeaderText(rb.getString("errorEditingCustomer"));
            alert.setContentText(rb.getString("noDatabaseConnectionContent"));
            alert.showAndWait();
            return -1;
        } catch (Exception e) {
//            Catches other failures
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setTitle(rb.getString("errorTitle"));
            alert1.setHeaderText(rb.getString("nonDatabaseErrorHeader"));
            alert1.setContentText(rb.getString("nonDatabaseErrorText"));
            alert1.showAndWait();
            return -1;
        }
    }

    /**
     * Cleans up the database entries that no longer are paired with customers
     * This is necessary due to the changing of data from Edit appointment
     * Used by editCustomer()
     * */
    private static void cleanDatabase() {
        ResourceBundle rb = ResourceBundle.getBundle("resources/databaseConnection", Locale.getDefault());
//        Connect to DB
        try (Connection connection = DriverManager.getConnection(url,user,pass);
             Statement statement = connection.createStatement()) {
//            Creates list of addressId's in Customer Table
            ResultSet addressIdResultSet = statement.executeQuery("SELECT DISTINCT addressId FROM customer ORDER BY addressId");
            ArrayList<Integer> addressIdListFromCustomer = new ArrayList<>();
            while (addressIdResultSet.next()) {
                addressIdListFromCustomer.add(addressIdResultSet.getInt(1));
            }
//            Creates list of addressId's in Address table
            addressIdResultSet = statement.executeQuery("SELECT DISTINCT addressId FROM address ORDER BY addressId");
            ArrayList<Integer> addressIdListFromAddress = new ArrayList<>();
            while (addressIdResultSet.next()) {
                addressIdListFromAddress.add(addressIdResultSet.getInt(1));
            }
//            Creates list of addressId's that exist in Address table but are not used in Customer table
            for (Integer value : addressIdListFromCustomer) {
                for (int i = 0; i < addressIdListFromAddress.size(); i++) {
                    if (value.equals(addressIdListFromAddress.get(i))) {
                        addressIdListFromAddress.remove(i);
                        i--;
                    }
                }
            }
//            Deletes Address table entries by remaining addressId's, if any remain
            if (!addressIdListFromAddress.isEmpty()) {
                for (int addressId : addressIdListFromAddress) {
                    statement.executeUpdate("DELETE FROM address WHERE addressId = " + addressId);
                }
            }
//            Creates list of cityId's used in Address table
            ResultSet cityIdResultSet = statement.executeQuery("SELECT DISTINCT cityId FROM address ORDER BY cityId");
            ArrayList<Integer> cityIdListFromAddress = new ArrayList<>();
            while (cityIdResultSet.next()) {
                cityIdListFromAddress.add(cityIdResultSet.getInt(1));
            }
//            Creates list of cityId's used in City table
            cityIdResultSet = statement.executeQuery("SELECT DISTINCT cityId FROM city ORDER BY cityId");
            ArrayList<Integer> cityIdListFromCity = new ArrayList<>();
            while (cityIdResultSet.next()) {
                cityIdListFromCity.add(cityIdResultSet.getInt(1));
            }
//            Creates list of cityId's that exist in City table but are not used in Address table
            for (Integer idListFromAddress : cityIdListFromAddress) {
                for (int i = 0; i < cityIdListFromCity.size(); i++) {
                    if (idListFromAddress.equals(cityIdListFromCity.get(i))) {
                        cityIdListFromCity.remove(i);
                        i--;
                    }
                }
            }
//            Delete City table entries by remaining cityId's, if any remain
            if (!cityIdListFromCity.isEmpty()) {
                for (int cityId : cityIdListFromCity) {
                    statement.executeUpdate("DELETE FROM city WHERE cityId = " + cityId);
                }
            }
//            Create list of countryId's used in City table
            ResultSet countryIdResultSet = statement.executeQuery("SELECT DISTINCT countryId FROM city ORDER BY countryId");
            ArrayList<Integer> countryIdListFromCity = new ArrayList<>();
            while (countryIdResultSet.next()) {
                countryIdListFromCity.add(countryIdResultSet.getInt(1));
            }
//            Creates list of countryId's used in Country table
            countryIdResultSet = statement.executeQuery("SELECT DISTINCT countryId FROM country ORDER BY countryId");
            ArrayList<Integer> countryIdListFromCountry = new ArrayList<>();
            while (countryIdResultSet.next()) {
                countryIdListFromCountry.add(countryIdResultSet.getInt(1));
            }
//            Creates list of countryId's that exist in Country table but are not used in City table
            for (Integer integer : countryIdListFromCity) {
                for (int i = 0; i < countryIdListFromCountry.size(); i++) {
                    if (integer.equals(countryIdListFromCountry.get(i))) {
                        countryIdListFromCountry.remove(i);
                        i--;
                    }
                }
            }
//            Deletes Country table entries by remaining countryId's, if any remain
            if (!countryIdListFromCountry.isEmpty()) {
                for (int countryId : countryIdListFromCountry) {
                    statement.executeUpdate("DELETE FROM country WHERE countryId = " + countryId);
                }
            }
        } catch (SQLException e) {
//            Throws error if SQL exception

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rb.getString("errorTitle"));
            alert.setHeaderText(rb.getString("errorAddingAppointment"));
            alert.setContentText(rb.getString("noDatabaseConnectionContent"));
            alert.showAndWait();
        } catch (Exception e) {
//            Catches other failures
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setTitle(rb.getString("errorTitle"));
            alert1.setHeaderText(rb.getString("nonDatabaseErrorHeader"));
            alert1.setContentText(rb.getString("nonDatabaseErrorText"));
            alert1.showAndWait();
        }
    }

    /**
     * Method to grab Customer ID
     * Used by editCustomer()
     * */
    private static int getCustomerId(String customerName, int addressId) throws SQLException {
//        Connect to DB
        try (Connection connection = DriverManager.getConnection(url,user,pass);
             Statement statement = connection.createStatement()) {
            ResultSet customerIdResultSet = statement.executeQuery("SELECT customerId FROM customer WHERE customerName = '"
                    + customerName + "' AND addressId = " + addressId);
            customerIdResultSet.next();
            return customerIdResultSet.getInt(1);
        }
    }

    /**
     * Method to see whether customer is active or not
     * Used by editCustomer()
    * */
    private static int getActiveStatus(int customerId) throws SQLException {
//        Connect to database
        try (Connection connection = DriverManager.getConnection(url,user,pass);
             Statement statement = connection.createStatement()) {
            ResultSet activeResultSet = statement.executeQuery("SELECT active FROM customer WHERE customerId = " + customerId);
            activeResultSet.next();
            return activeResultSet.getInt(1);
        }
    }

    /**
    * Method that updates customer.
     * Used by editCustomer()
    * */
    private static void updateCustomer(int customerId, String customerName, int addressId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url,user,pass);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("UPDATE customer SET customerName = '" + customerName + "', addressId = " + addressId + ", " +
                    "lastUpdate = CURRENT_TIMESTAMP, lastUpdateBy = '" + currentUser + "' WHERE customerId = " + customerId);
        }
    }

    /**
    * Sets a customer to inactive and makes them hidden in the customer list view
    * */
    public static void setCustomerToInactive(Customer customerToDelete) {
        int customerId = customerToDelete.getCustomerId();
        ResourceBundle rb = ResourceBundle.getBundle("resources/databaseConnection", Locale.getDefault());
//        Set Confirmation to delete customer
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle(rb.getString("confirmDeleteCustomerTitle"));
        alert.setHeaderText(rb.getString("confirmDeleteCustomerHeader"));
        alert.setContentText(rb.getString("confirmDeleteCustomerContent"));
        Optional<ButtonType> result = alert.showAndWait();
//        If OK is pressed, then proceed. Catches errors if cannot connect to DB
        if (result.get() == ButtonType.OK) {
            try (Connection connection = DriverManager.getConnection(url,user,pass);
                 Statement statement = connection.createStatement()) {
                statement.executeUpdate("UPDATE customer SET active = 0 WHERE customerId = " + customerId);
            } catch (SQLException e) {
//                If it fails it will throw this database error
                Alert alert2 = new Alert(Alert.AlertType.ERROR);
                alert2.setTitle(rb.getString("errorTitle"));
                alert2.setHeaderText(rb.getString("errorEditingCustomer"));
                alert2.setContentText(rb.getString("noDatabaseConnectionContent"));
                alert2.showAndWait();
            } catch (Exception e) {
//            Catches other failures
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle(rb.getString("errorTitle"));
                alert1.setHeaderText(rb.getString("nonDatabaseErrorHeader"));
                alert1.setContentText(rb.getString("nonDatabaseErrorText"));
                alert1.showAndWait();
            }
//            Will pass the information into the roster
            updateCustomerList();
        }
    }

    /**
     * Will update the appointmentList with future appointments
     * Used by doesAppointmentOverlap(), deleteAppointment(), generateUpcomingMeetingsByCustomer()
     * */
    public static void updateAppointmentList() {
//        DB Connection
        try (Connection connection = DriverManager.getConnection(url, user, pass);
             Statement statement = connection.createStatement()) {
//            Calls getAppointmentList() from AppointmentList
            ObservableList<Appointment> appointmentList = AppointmentList.getAppointmentList();
            appointmentList.clear();
//            Create list of appointmentId's for all appointments that are in the FUTURE. Will not display old appointments
            ResultSet appointmentResultSet = statement.executeQuery("SELECT appointmentId FROM appointment WHERE start >= CURRENT_TIMESTAMP");
            ArrayList<Integer> appointmentIdList = new ArrayList<>();
//            Iterates over each appointment grabbed by the Query and grabs the ID
            while(appointmentResultSet.next()) {
                appointmentIdList.add(appointmentResultSet.getInt(1));
            }
//            For Loop to create Appointment for each appointmentId in list and adds the object to appointmentList
            for (int appointmentId : appointmentIdList) {
//                Queries database for appointment information
                appointmentResultSet = statement.executeQuery("SELECT customerId, title, description, location, " +
                        "contact, url, start, end, createdBy FROM appointment WHERE appointmentId = " + appointmentId);
                appointmentResultSet.next();
                int customerId = appointmentResultSet.getInt(1);
                String title = appointmentResultSet.getString(2);
                String description = appointmentResultSet.getString(3);
                String location = appointmentResultSet.getString(4);
                String contact = appointmentResultSet.getString(5);
                String url = appointmentResultSet.getString(6);
                Timestamp startTimestamp = appointmentResultSet.getTimestamp(7);
                Timestamp endTimestamp = appointmentResultSet.getTimestamp(8);
                System.out.println(startTimestamp);
                String createdBy = appointmentResultSet.getString(9);
//                Changes startTimestamp & endTimestamp to ZonedDateTimes
                DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
//                utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                java.util.Date startDate = utcFormat.parse(startTimestamp.toString());
                java.util.Date endDate = utcFormat.parse(endTimestamp.toString());
//                startDate ends up being incorrect, TimeStamp should actually be 16:00 not 9:00
//                Therefore Timestamp ends up being wrong


//                Adds information to the Appointment instance
                Appointment appointment = new Appointment(appointmentId, customerId, title, description,
                        location, contact, url, startTimestamp, endTimestamp, startDate, endDate, createdBy);
//                Adds instance to appointmentList
                appointmentList.add(appointment);
            }
        } catch (SQLException e) {
//            Catches SQL errors
            ResourceBundle rb = ResourceBundle.getBundle("resources/databaseConnection", Locale.getDefault());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rb.getString("errorTitle"));
            alert.setHeaderText(rb.getString("errorAddingAppointment"));
            alert.setContentText(rb.getString("noDatabaseConnectionContent"));
            alert.showAndWait();
        } catch (Exception e) {
//            If an error occurs that isn't a SQL error, this part catches it
            ResourceBundle rb = ResourceBundle.getBundle("resources/databaseConnection", Locale.getDefault());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rb.getString("errorTitle"));
            alert.setHeaderText(rb.getString("errorAddingAppointment"));
            alert.setContentText(rb.getString("nonDatabaseErrorText"));
            alert.showAndWait();
        }
    }

    /**
     * Adds appointment to database unless it overlaps another
     * */
    public static boolean addNewAppointment(Customer customer, String title, String type,
                                            ZonedDateTime startUTC, ZonedDateTime endUTC) {
//        Change ZonedDateTimes to Timestamps.
        Timestamp startTimeStamp = Timestamp.valueOf(startUTC.toLocalDateTime());
        Timestamp endTimeStamp = Timestamp.valueOf(endUTC.toLocalDateTime());
//        Checks to make sure that added appointment does not overlap with others, then adds it if it doesn't
        if (checkOverlap(startTimeStamp, endTimeStamp)) {
            ResourceBundle rb = ResourceBundle.getBundle("resources/databaseConnection", Locale.getDefault());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rb.getString("errorTitle"));
            alert.setHeaderText(rb.getString("errorAddingAppointment"));
            alert.setContentText(rb.getString("errorAppointmentOverlaps"));
            alert.showAndWait();
            return false;
        } else {
            int customerId = customer.getCustomerId();
            String customerName = customer.getCustomerName();
            addAppointment(customerId, customerName, title, type, startTimeStamp, endTimeStamp);
            return true;
        }
    }

    /**
     * Makes sure that no appointment overlaps as required by the rubric
     * Used by addNewAppointment()
     * */
    private static boolean checkOverlap(Timestamp startTimestamp, Timestamp endTimestamp) {
        updateAppointmentList();
        ObservableList<Appointment> appointmentList = AppointmentList.getAppointmentList();
        for (Appointment appointment: appointmentList) {
//            Calls getStartTimestamp & getEndTimestamp from appointment to get appointment times
            Timestamp existingStartTimestamp = appointment.getStartTimestamp();
            Timestamp existingEndTimestamp = appointment.getEndTimestamp();
//            Checks various conditions for whether it will overlap. Did not put on one line for readability
            if (startTimestamp.after(existingStartTimestamp) && startTimestamp.before(existingEndTimestamp)) {
                return true;
            } else if (endTimestamp.after(existingStartTimestamp) && endTimestamp.before(existingEndTimestamp)) {
                return true;
            } else if (startTimestamp.after(existingStartTimestamp) && endTimestamp.before(existingEndTimestamp)) {
                return true;
            } else if (startTimestamp.before(existingStartTimestamp) && endTimestamp.after(existingEndTimestamp)) {
                return true;
            } else if (startTimestamp.equals(existingStartTimestamp) || endTimestamp.equals(existingEndTimestamp)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates new appointment.
     * Used by addNewAppointment()
     * */
    private static void addAppointment(int customerId, String contact, String title, String type,
                                       Timestamp startTimestamp, Timestamp endTimestamp) {
        ResourceBundle rb = ResourceBundle.getBundle("resources/databaseConnection", Locale.getDefault());
//        Created Null values as they are not required for the project, but is useful for updating the DB
        String location = null;
        String url = null;
//        Connects to DB. Uses a fuller path since I'm creating a url variable as a parameter
        try (Connection connection = DriverManager.getConnection(DatabaseConnection.url,user,pass);
             Statement statement = connection.createStatement()) {
            ResultSet allAppointmentId = statement.executeQuery("SELECT appointmentId FROM appointment ORDER BY appointmentId");
            int appointmentId;
            if (allAppointmentId.last()) {
                appointmentId = allAppointmentId.getInt(1) + 1;
                allAppointmentId.close();
            } else {
                allAppointmentId.close();
                appointmentId = 1;
            }
//            Creates the new entry
            statement.executeUpdate("INSERT INTO appointment VALUES (" + appointmentId +", " + customerId + ", '" + title + "', '" +
                    type + "', '" + location + "', '" + contact + "', '" + url + "', '" + startTimestamp + "', '" + endTimestamp + "', " +
                    "CURRENT_DATE, '" + currentUser + "', CURRENT_TIMESTAMP, '" + currentUser + "')");
        } catch (SQLException e) {
//            Database failure alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rb.getString("errorTitle"));
            alert.setHeaderText(rb.getString("errorAddingAppointment"));
            alert.setContentText(rb.getString("noDatabaseConnectionContent"));
            alert.showAndWait();
        } catch (Exception e) {
//            Catches other failures
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setTitle(rb.getString("errorTitle"));
            alert1.setHeaderText(rb.getString("nonDatabaseErrorHeader"));
            alert1.setContentText(rb.getString("nonDatabaseErrorText"));
            alert1.showAndWait();
        }
    }

    /**
     * Modifies an appointment
     * */
    public static boolean editAppointment(int appointmentId, Customer customer, String title, String type, String contact, ZonedDateTime startUTC, ZonedDateTime endUTC) {
        try {
//            ZonedDateTimes to Timestamps
            String startUTCString = startUTC.toString();
            startUTCString = startUTCString.substring(0, 10) + " " + startUTCString.substring(11, 16) + ":00";
            Timestamp startTimestamp = Timestamp.valueOf(startUTCString);
            String endUTCString = endUTC.toString();
            endUTCString = endUTCString.substring(0, 10) + " " + endUTCString.substring(11, 16) + ":00";
            Timestamp endTimestamp = Timestamp.valueOf(endUTCString);
//            Checks to see if appointment overlaps another one
            if (checkEditOverlap(startTimestamp, endTimestamp)) {
                ResourceBundle rb = ResourceBundle.getBundle("resources/databaseConnection", Locale.getDefault());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(rb.getString("errorTitle"));
                alert.setHeaderText(rb.getString("errorEditingAppointment"));
                alert.setContentText(rb.getString("errorAppointmentOverlaps"));
                alert.showAndWait();
                return false;
            } else {
//                Else update the appointment and return true
                int customerId = customer.getCustomerId();
                updateAppointmentInDB(appointmentId, customerId, title, type, contact, startTimestamp, endTimestamp);
                return true;
            }
        } catch (SQLException e) {
//            Database failure alert
            ResourceBundle rb = ResourceBundle.getBundle("resources/databaseConnection", Locale.getDefault());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rb.getString("errorTitle"));
            alert.setHeaderText(rb.getString("errorAddingAppointment"));
            alert.setContentText(rb.getString("noDatabaseConnectionContent"));
            alert.showAndWait();
            return false;
        } catch (Exception e) {
//            Catches other failures
            ResourceBundle rb = ResourceBundle.getBundle("resources/databaseConnection", Locale.getDefault());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rb.getString("errorTitle"));
            alert.setHeaderText(rb.getString("errorAddingAppointment"));
            alert.setContentText(rb.getString("nonDatabaseErrorText"));
            alert.showAndWait();
            return false;
        }
    }

    /**
     * Updates appointment to the Database after a check
     * Used by editAppointment()
     * */
    private static void updateAppointmentInDB(int appointmentId, int customerId, String title, String type, String contact,
                                          Timestamp startTimestamp, Timestamp endTimestamp) throws SQLException {
        String location = null;
//        Connects to DB. Uses full url path since we're using url as a parameter
        try (Connection connection = DriverManager.getConnection(DatabaseConnection.url,user,pass);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("UPDATE appointment SET customerId = " + customerId + ", title = '" + title + "', " +
                    "location = '" + location + "', contact = '" + contact +  "', description = '" + type + "', start = '"
                    + startTimestamp + "', end = '" + endTimestamp + "', " +
                    "lastUpdate = CURRENT_TIMESTAMP, lastUpdateBy = '" + currentUser + "' WHERE appointmentId = " + appointmentId);
        }
    }

    /**
     * A secondary overlap check made for modifying existing appointments
     * Used by editAppointment()
     * */
    private static boolean checkEditOverlap(Timestamp startTimestamp, Timestamp endTimestamp) throws SQLException, ParseException {
        int appointmentIndexToRemove = AppointmentViewScreenController.getAppointmentIndexToEdit();
        ObservableList<Appointment> appointmentList = AppointmentList.getAppointmentList();
//        Calls remove method from appointmentList
        appointmentList.remove(appointmentIndexToRemove);
        for (Appointment appointment: appointmentList) {
            Timestamp existingStartTimestamp = appointment.getStartTimestamp();
            Timestamp existingEndTimestamp = appointment.getEndTimestamp();
//            Check to see if appointments overlap. Did not create on one line for readability.
            if (startTimestamp.after(existingStartTimestamp) && startTimestamp.before(existingEndTimestamp)) {
                return true;
            } else if (endTimestamp.after(existingStartTimestamp) && endTimestamp.before(existingEndTimestamp)) {
                return true;
            } else if (startTimestamp.after(existingStartTimestamp) && endTimestamp.before(existingEndTimestamp)) {
                return true;
            } else if (startTimestamp.before(existingStartTimestamp) && endTimestamp.after(existingEndTimestamp)) {
                return true;
            } else if (startTimestamp.equals(existingStartTimestamp) || endTimestamp.equals(existingEndTimestamp))  {
                return true;
            }
        }
        return false;
    }

    /**
     * Deletes an appointment base on the appointmentId
     * */
    public static void deleteAppointment(Appointment appointmentToDelete) {
        int appointmentId = appointmentToDelete.getAppointmentId();
        ResourceBundle rb = ResourceBundle.getBundle("resources/databaseConnection", Locale.getDefault());
//        Delete confirmation alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle(rb.getString("confirmDelete"));
        alert.setHeaderText(rb.getString("confirmDeleteAppointment"));
        alert.setContentText(rb.getString("confirmDeleteAppointmentMessage"));
        Optional<ButtonType> result = alert.showAndWait();
//        Action if Confirmed with OK button
        if (result.get() == ButtonType.OK) {
            try (Connection connection = DriverManager.getConnection(url,user,pass);
                 Statement statement = connection.createStatement()) {
                statement.executeUpdate("DELETE FROM appointment WHERE appointmentId =" + appointmentId);
            }
            catch (SQLException e) {
//                Alert for SQL failure
                Alert alert2 = new Alert(Alert.AlertType.ERROR);
                alert2.setTitle(rb.getString("errorTitle"));
                alert2.setHeaderText(rb.getString("errorEditingAppointment"));
                alert2.setContentText(rb.getString("noDatabaseConnectionContent"));
                alert2.showAndWait();
            } catch (Exception e) {
//            Catches other failures
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle(rb.getString("errorTitle"));
                alert1.setHeaderText(rb.getString("errorAddingAppointment"));
                alert1.setContentText(rb.getString("nonDatabaseErrorText"));
                alert1.showAndWait();
            }
//            Calls updateAppointmentList
            updateAppointmentList();
        }
    }
}