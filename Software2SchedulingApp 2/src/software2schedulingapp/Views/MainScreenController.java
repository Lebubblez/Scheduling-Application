/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software2schedulingapp.Views;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import software2schedulingapp.Models.Appointment;
import software2schedulingapp.Models.AppointmentDB;

/**
 *
 * @author ledaerlandson
 */
public class MainScreenController implements Initializable {

    @FXML
    public BorderPane mainScreen;
    @FXML
    public Button appointmentButton;
    @FXML
    public Button customerButton;
    @FXML
    public Button reportsButton;
    @FXML
    public Label menuLabelText;
    @FXML
    public Button logFileButton;
    @FXML
    public Menu menuFileButton;
    @FXML
    public MenuItem closeUser;
    @FXML
    private MenuItem logoutUser;


    //shows the user logout-button from file dropdown
    @FXML
    public void handleMenuLogout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Logout");
        alert.setHeaderText("Are you sure you want to logout?");
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)

                .ifPresent((ButtonType response) -> {
                    Stage stage = new Stage();
                    Parent root = null;
                    try {
                        root = FXMLLoader.load(getClass().getResource("LoginScreen.fxml"));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();

                });
    }

    //shows the appointment screen
    @FXML
    public void handleMenuAppointments(ActionEvent event) throws IOException {
        Stage stage = new Stage();

        Parent root = FXMLLoader.load(getClass().getResource("AppointmentScreen.fxml"));

        try {
            root = FXMLLoader.load(getClass().getResource("AppointmentScreen.fxml"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(
                ((Node) event.getSource()).getScene().getWindow());
        stage.show();
    }

    //shows the customers screen
    @FXML
    public void handleMenuCustomers(ActionEvent event) throws IOException {

        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("CustomerScreen.fxml"));
        try {
            root = FXMLLoader.load(getClass().getResource("CustomerScreen.fxml"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(
                ((Node) event.getSource()).getScene().getWindow());
        stage.show();
    }

    //shows the reports screen
    @FXML
    public void handleMenuReports(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("ReportsScreen.fxml"));
        try {
            root = FXMLLoader.load(getClass().getResource("ReportsScreen.fxml"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(
                ((Node) event.getSource()).getScene().getWindow());
        stage.show();

    }

    //prints the logFile to view timeStamps of logins from login.txt button
    @FXML
    public void handleMenuLogFile(ActionEvent event) {
        try {
            try (BufferedReader reader = new BufferedReader(new FileReader(new File("recordOfLogins.txt")))) {

                String line;
                while ((line = reader.readLine()) != null)
                    System.out.println(line);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Initializes reminders within 15min of login
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Appointment appointment = AppointmentDB.setReminderFifteen();
        if(appointment != null) {
            String reminder = String.format("You have a '%s' appointment at %s",
                    appointment.getType(),
                    appointment.get15Time());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Reminder of Upcoming Appointment");
            alert.setHeaderText("You Have an Appointment Within 15 Minutes");
            alert.setContentText(reminder);
            alert.showAndWait();
        }
        
    }
}