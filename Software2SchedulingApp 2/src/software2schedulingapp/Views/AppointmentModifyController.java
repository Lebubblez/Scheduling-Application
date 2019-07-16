/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software2schedulingapp.Views;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import software2schedulingapp.Models.Appointment;
import software2schedulingapp.Models.AppointmentDB;
import software2schedulingapp.Models.Customer;
import software2schedulingapp.Models.CustomerDB;

/**
 *
 * @author ledaerlandson
 */
public class AppointmentModifyController extends AppointmentDB implements Initializable {
    @FXML
    private TextField locationField;
    @FXML
    private ComboBox<String> contactComboBox;
    @FXML
    private Label appointmentLabel;
    @FXML
    private TextField titleField;
    @FXML
    public ComboBox<String> startComboBox;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<String> typeComboBox;
    @FXML
    private Button appointmentSaveButton;
    @FXML
    private Button appointmentCancelButton;
    @FXML
    private TableColumn<Customer, String> customerNameAppointmentColumn;
    @FXML
    private Button closeButton;
    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private AnchorPane appointmentScreen;
    @FXML
    public Customer customerSelect;

    private Window dialogStage;

    private final ObservableList<String> typeList = FXCollections.observableArrayList();
    private final ObservableList<String> contacts = FXCollections.observableArrayList();
    private final ObservableList<String> times = FXCollections.observableArrayList();
    private final DateTimeFormatter timeDTF = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
    private boolean okClicked;

    //handles customer table selection of customer & retrieves its information

    /**
     *
     * @param mouseEvent
     */
    
    @FXML
    public void handleCustomerClick(MouseEvent mouseEvent) {
        customerSelect = customerTable.getSelectionModel().getSelectedItem();
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    public void setOkClicked(boolean okClicked) {
        this.okClicked = okClicked;
    }
    
    //handle save button for both edit-save, and new-save
    @FXML
    public void handleSave(ActionEvent event) throws SQLException {

        LocalDate localDate = datePicker.getValue();
        int apptCustomer = customerTable.getSelectionModel().getSelectedItem().getCustomerID();
        int apptType = typeComboBox.getSelectionModel().getSelectedIndex();
        int apptTime = startComboBox.getSelectionModel().getSelectedIndex();
        int apptContact = contactComboBox.getSelectionModel().getSelectedIndex();
        String apptLocation = locationField.getText();
        String apptTitle = titleField.getText();
        int apptId = AppointmentScreenController.selectedApptID;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Save");
        alert.setHeaderText("Are you sure you want to Save?");
        alert.showAndWait();
        Stage stage = (Stage) appointmentSaveButton.getScene().getWindow();
        stage.close();

        //checks validation for saving customer information
        if (addAppointmentValidation()) {
            if (AppointmentDB.appointmentConflicts(-1,  times.get(apptTime), localDate.toString(), apptLocation)) {
                Alert alert2 = new Alert(Alert.AlertType.WARNING);
                alert2.setTitle("Conflicting Appointments");
                alert2.setHeaderText("There Are Time Conflicts With An Existing Appointment");
                alert2.showAndWait();
                Stage stage2 = (Stage) appointmentSaveButton.getScene().getWindow();
                stage2.close();
                return;
            }

            //saves for edit-save if edit button is clicked
            if (isOkClicked()) {
                updateAppointment(apptCustomer, apptTitle, typeList.get(apptType), contacts.get(apptContact), apptLocation, localDate.toString(), times.get(apptTime), apptId);
            }

            //else, saves for new-save when new button is clicked
            else {
                saveAppointment(apptCustomer, apptTitle, typeList.get(apptType), contacts.get(apptContact), apptLocation, localDate.toString(), times.get(apptTime));
            }

        }
    }

    //handles the city combo box selection
    @FXML
    public void handleLocation() {
        String contact = contactComboBox.getSelectionModel().getSelectedItem();
        switch (contact) {
            case "Plankton":
                locationField.setText("Phoenix");
                break;
            case "Patrick":
                locationField.setText("London");
                break;
            case "Sandy":
                locationField.setText("New York");
                break;
        }
    }

    //handles the cancel button
    @FXML
    public void handleCancel(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Cancel");
        alert.setHeaderText("Are you sure you want to Cancel?");
        alert.showAndWait();
        Stage stage = (Stage) appointmentCancelButton.getScene().getWindow();
        stage.close();
    }

    //handles the close screen button
    @FXML
    public void handleCloseButton(ActionEvent event) {
        // handle to the stage
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    //initializes combo-box's, customer table, & setting info into text-fields
    @Override
    public void initialize(URL url, ResourceBundle resources) {

        //initializing customer table of customer names
        customerTable.setItems(CustomerDB.getItemsToAdd());

        customerNameAppointmentColumn.setCellValueFactory(cellData ->
                cellData.getValue().customerNameProperty());

        if (customerTable.getSelectionModel().getSelectedItem() != null) {
            customerSelect = customerTable.getSelectionModel().getSelectedItem();
        } else {
            customerSelect = null;
        }

        //initializes the start time combobox based on business hours
        times.addAll("9:00 AM", "9:15 AM","9:30 AM", "9:45 AM", "10:00 AM", "10:15 AM", "10:30 AM", "10:45 AM", "11:00 AM", "11:15 AM", "11:30 AM", "11:45 AM", "12:00 PM", "12:15 PM","12:30 PM", "12:45 PM", "1:00 PM", "1:15 PM","1:30 PM", "1:45 PM", "2:00 PM", "2:15 PM", "2:30 PM", "2:45 PM", "3:00 PM", "3:15 PM","3:30 PM", "3:45 PM", "4:00 PM");
        startComboBox.setItems(times);
        startComboBox.setValue("9:00 AM");

        //initializes the contact combobox
        contacts.addAll("Sandy", "Patrick", "Plankton");
        contactComboBox.setItems(contacts);

        //TODO: Try utilizing customer this way
        //initializes the type combo box for selection
        typeList.addAll("Consultation", "New Account", "Follow Up", "Close Account");
        typeComboBox.setItems(typeList);
        typeComboBox.setValue("New Account");

        //initializes the start and end times
        datePicker.setValue(LocalDate.now());

    }

    //validation for appointment submission
    private boolean addAppointmentValidation() {
        String titleFieldText = titleField.getText();
        Object typeComboBoxValue = typeComboBox.getValue();
        int customerID = customerTable.getSelectionModel().getSelectedItem().getCustomerID();
        LocalDate datePickerValue = datePicker.getValue();
        Object startComboBoxValue = startComboBox.getValue();
        Object contactComboBoxValue = contactComboBox.getValue();
        String locationFieldText = locationField.getText();

        String alertText = "";
        //creates and error message for all null inputs
        if (customerID == 0) {
            alertText += "Please Select a Customer.\n";
        }
        if (titleFieldText == null || titleFieldText.length() == 0) {
            alertText += "Please enter an Appointment title.\n";
        }
        if (typeComboBoxValue == null) {
            alertText += "Please select an Appointment type.\n";
        }
        if (datePickerValue == null) {
            alertText += "Please Select a Customer.\n";
        }
        if (contactComboBoxValue == null) {
            alertText += "Please Select a Contact. \n";
        }
        if (locationFieldText == null || locationFieldText.length() == 0) {
            alertText += "Please enter a Location.\n";
        }
        if (startComboBoxValue == null) {
            alertText += "Please select a Start time.\n";
        }
        if (alertText.length() == 0) {
            return true;
        } else {
            // alert for all incorrect inputs
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Incorrect Inputs");
            alert.setHeaderText("Please correct Errors");
            alert.setContentText(alertText);

            alert.showAndWait();

            return false;
    }

}

    //sets appointment screen
    void setAppointmentScreen(Appointment appointment) {
        okClicked = true;

        appointmentLabel.setText("Edit Appointment");
        titleField.setText(appointment.getTitle());
        typeComboBox.setValue(appointment.getType());
        datePicker.setValue(appointment.getDateOnly());
        contactComboBox.setValue(appointment.getContact());
        handleLocation();
        customerTable.getSelectionModel().select(appointment.getAppointmentID());
        System.out.println("appintment.getstart: "+appointment.getStart());
        startComboBox.setValue(appointment.getTimeOnly(timeDTF));
    }


}

