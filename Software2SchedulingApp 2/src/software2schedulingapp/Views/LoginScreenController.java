/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software2schedulingapp.Views;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import software2schedulingapp.Models.User;
import static software2schedulingapp.Models.UserDB.login;
import software2schedulingapp.SchedulerApp;

/**
 *
 * @author ledaerlandson
 */
    
    public class LoginScreenController implements Initializable {
    @FXML
    private Text titleText;
    @FXML
    private Button cancelText;
    @FXML
    private Button signinText;
    @FXML
    private Label errorMessage;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Text passwordText;
    @FXML
    private TextField usernameField;
    @FXML
    private Text usernameText;
    @FXML
    private BorderPane mainScreen;
    private JFXPanel primaryStage;
    private User currentUser;
    private SchedulerApp schedulerApp;
    private ResourceBundle resourceBundle;
    private Locale locale;
    private String errorMessageString;
    private static final DateTimeFormatter timeDTF = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);

        //handle sign-in button action
        @FXML
        public void handleSignInAction (ActionEvent event) throws IOException {
            String username = usernameField.getText();
            String password = passwordField.getText();
            boolean validUser = login(username, password);
            if (validUser) {
                ((Node) (event.getSource())).getScene().getWindow().hide();
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Oops! An error occured");
                alert.setContentText("Username or password is incorrect");
                alert.showAndWait();
            }

        }


        //handles cancel button
        @FXML
        public void handleCancelAction (ActionEvent event){

        }


        //initializes the login screen for language based on location
        @Override
        public void initialize (URL location, ResourceBundle resources){
            Locale locale = Locale.getDefault();
//        Locale locale  = Locale.FRANCE;
            resources = ResourceBundle.getBundle("Language.login", locale);
            usernameText.setText(resources.getString("userName"));
            passwordText.setText(resources.getString("password"));
            titleText.setText(resources.getString("title"));
            cancelText.setText(resources.getString("cancel"));
            errorMessageString = resources.getString("incorrect");
            signinText.setText(resources.getString("sign-in"));
        }
}


