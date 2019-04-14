package scheduler.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import scheduler.MySchedulingApp;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import static scheduler.util.DatabaseConnection.checkLogInCredentials;

public class LoginController {

//    FXML section
    @FXML
    public Label loginTitle;

    @FXML
    public Label usernameLabel;

    @FXML
    public Label passwordLabel;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginSignInButton;

    @FXML
    private Button exitButton;

    @FXML
    private Label errorLabel;

    private MySchedulingApp mainApp;


// variable for whether an error should be thrown
    public static int databaseError = 0;

//    method for increasing Database error number
    public static void incrementDatabaseError() {
        databaseError++;
    }

    // This Method will be in every class to set the names if of the labels
    private void setLanguage() {
        ResourceBundle rb = ResourceBundle.getBundle("LogIn", Locale.getDefault());
        loginTitle.setText(rb.getString("title"));
        usernameLabel.setText(rb.getString("username"));
        passwordLabel.setText(rb.getString("password"));
        loginSignInButton.setText(rb.getString("signin"));
        exitButton.setText(rb.getString("cancel"));
    }

//    Handlers

    /**
     * Exit Confirmation
     * Quits App
     * */
    @FXML
    void exitButtonHandler(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Exit");
        alert.setHeaderText("Exiting Program!");
        alert.setContentText("Press OK to exit the Program");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.exit(0);
        } else {
            System.out.println("Cancelled Exit");
        }
    }

    /**
     * Load main menu
     * Add details to log file
     * */
    @FXML
    void loginSignInButtonHandler(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
//        Access resourceBundle
        ResourceBundle rb = ResourceBundle.getBundle("LogIn", Locale.getDefault());
        if(username.length()==0 || password.length()==0) {
            errorLabel.setText(rb.getString("emptylogin"));
//            Returns because rest of code does not need to finish
            return;
        }
//        Variable to easily query the database
        boolean correctCredentials = checkLogInCredentials(username, password);
        if (correctCredentials) {
            try {
//                Loads main screen
                Parent mainScreenParent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene mainScreenScene = new Scene(mainScreenParent);
                Stage mainScreenStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                mainScreenStage.setScene(mainScreenScene);
                mainScreenStage.show();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
//        Check for Database Error
        else if (databaseError > 0) {
            // Show connection error message
            lblLogInErrorMessage.setText(rb.getString("lblConnectionError"));
        } else {
//            Calls the error message saying username or password incorrect
            lblLogInErrorMessage.setText(rb.getString("lblWrongUserPass"));
        }
    }



    // Initialize screen elements
    /**
     * Inititialize the screen
     * */
    @FXML
    public void initialize() {
//        Calls setLanguage() to see text in proper language
        setLanguage();
        // Assign action to button
        loginSignInButton.setOnAction(event -> loginSignInButtonHandler(event));
    }



}





























/*
Create a log-in form that can determine the user’s location
 and translate log-in and error control messages
(e.g., “The username and password did not match.”)
into two languages.

F. Write exception controls to prevent each of the following. You may use the same mechanism
 of exception control more than once, but you must incorporate at least two different mechanisms of exception control.

• scheduling an appointment outside business hours

• scheduling overlapping appointments

• entering nonexistent or invalid customer data

• entering an incorrect username and password

H. Write code to provide an alert if there is an appointment
 within 15 minutes of the user’s log-in.

 J. Provide the ability to track user activity by recording timestamps for user log-ins in a .txt file.
 Each new record should be appended to the log file, if the file already exists.
*/