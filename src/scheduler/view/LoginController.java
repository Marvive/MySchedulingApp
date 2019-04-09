package scheduler.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import scheduler.MySchedulingApp;
import scheduler.model.Appointment;
import scheduler.model.User;

import java.util.Locale;
import java.util.ResourceBundle;

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




//  This gets the Locale of the user setting the default language
//    rb grabs the information from Resource Bundle
    Locale currentLocale = Locale.getDefault();
    ResourceBundle rb = ResourceBundle.getBundle("login", currentLocale);

    public static void incrementDatabaseError() {
    }

    // This Method will be in every class to set the names if of the labels
    private void setLanguage() {
        loginTitle.setText(rb.getString("title"));
        usernameLabel.setText(rb.getString("username"));
        passwordLabel.setText(rb.getString("password"));
        loginSignInButton.setText(rb.getString("signin"));
        exitButton.setText(rb.getString("cancel"));
    }

//    Handlers
    @FXML
    void exitButtonHandler(ActionEvent event) {
/*
* Exit Confirmation
* Quits App
* */
    }

    @FXML
    void loginSignInButtonHandler(ActionEvent event) {
        /*
         * Load main menu
         * Add details to log file
         * */
        String username = usernameField.getText();
        String password = passwordField.getText();
        if(username.length()==0 || password.length()==0) {
            errorLabel.setText(rb.getString("emptylogin"));
        }
        boolean validate = validateLogin(username, password);
        if
        }
    }


























//    References the main application


//    ResourceBundle rb = ResourceBundle.getBundle("login", currentLocale);

    ObservableList<Appointment> reminderList;




    //    Translation
//    @Override
//    public void initialize(URL url, ResourceBundle rb) {
//        Translate words to spanish
//        if (currentLocale.getDisplayLanguage().equals("English")) {
//            loginTitle.setText("Iniciar Sesión");
//            usernameLabel.setText("Usuario");
//            passwordLabel.setText("Código");
//            loginSignInButton.setText("Entrar");
//            exitButton.setText("Salida");
//
//        }
//        errorLabel.setText("");
//



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