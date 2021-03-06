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

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import static scheduler.util.DatabaseConnection.checkLogInInputs;

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

//    Variable to store whether there should be an error
    private static int databaseError = 0;

//    A method to increase the error
    public static void incrementDatabaseError() {
        databaseError++;
    }

//    Setting the language for this Login page
    private void setLanguage() {
        ResourceBundle rb = ResourceBundle.getBundle("resources/login", Locale.getDefault());
        loginTitle.setText(rb.getString("title"));
        usernameLabel.setText(rb.getString("username"));
        passwordLabel.setText(rb.getString("password"));
        loginSignInButton.setText(rb.getString("signIn"));
        exitButton.setText(rb.getString("cancel"));
    }

//    Handlers
    /**
     * Exit Confirmation
     * Quits App
     */
    @FXML
    void exitButtonHandler(ActionEvent event) {
        ResourceBundle rb = ResourceBundle.getBundle("resources/login", Locale.getDefault());
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle(rb.getString("titleExitAlert"));
        alert.setHeaderText(rb.getString("headerExitAlert"));
        alert.setContentText(rb.getString("contentExitAlert"));
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    /**
     * Load main menu
     * Add details to log file
     */
    @FXML
    void loginSignInButtonHandler(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
//        Access resourceBundle
        ResourceBundle rb = ResourceBundle.getBundle("resources/login", Locale.getDefault());
        if (username.length() == 0 || password.length() == 0) {
            errorLabel.setText(rb.getString("emptyLogin"));
//            Returns because rest of code does not need to finish
            return;
        }
//        Variable to easily query the database
        boolean correctInputs = checkLogInInputs(username, password);
        if (correctInputs) {
            try {
//                Loads main screen
                Parent mainScreenParent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene mainScreenScene = new Scene(mainScreenParent);
                Stage mainScreenStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                mainScreenStage.setScene(mainScreenScene);
                mainScreenStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (databaseError > 0) {
//            Checks the error variable to throw an error or not
            errorLabel.setText(rb.getString("dbConnectionError"));
        } else {
//            Calls the error message saying username or password incorrect
            errorLabel.setText(rb.getString("wrongUserPassword"));
        }
    }

    /**
     * Initialize the screen
     */
    @FXML
    public void initialize() {
//        Calls setLanguage() to see text in proper language
        setLanguage();
//        Lambda to assign action to login button. Easier than traditional way.
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