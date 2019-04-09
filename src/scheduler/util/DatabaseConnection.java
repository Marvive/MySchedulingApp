package scheduler.util;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import scheduler.model.Appointment;
import scheduler.model.AppointmentList;
import scheduler.view.LoginController;

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
     * */
    private static void setCurrentUser(String userName) {
        currentUser = userName;
    }

    /**
    * Method to validate if the user is a valid user
    * */
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
    * */
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
    * */
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
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    /**
    * Method to create a notification upon logging in
    * */
    public static void loginAppointmentNotification() {
        if (openCount == 0) {
            ObservableList<Appointment> userAppointments = FXCollections.observableArrayList();
            for (Appointment appointment : AppointmentList.getAppointmentList()) {
                if (appointment.getCreatedBy().equals(currentUser)) {
                    userAppointments.add(appointment);
                }
            }
            for (Appointment appointment : userAppointments) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(Date.from(Instant.now()));
                calendar.add(Calendar.MINUTE, 15);
                Date notificationCutoff = calendar.getTime();

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
            openCount++;
        }
    }

}


















