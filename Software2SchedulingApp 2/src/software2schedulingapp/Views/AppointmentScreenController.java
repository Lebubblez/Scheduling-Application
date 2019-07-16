/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software2schedulingapp.Views;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import software2schedulingapp.Models.Appointment;
import software2schedulingapp.Models.AppointmentDB;
import software2schedulingapp.Models.Customer;
import software2schedulingapp.Models.User;
import software2schedulingapp.SchedulerApp;

/**
 *
 * @author ledaerlandson
 */
public class AppointmentScreenController implements Initializable {
    static int selectedApptID;
    @FXML
    public Button closeButton;
    @FXML
    public Button resetFilterButton;
    @FXML
    private RadioButton monthRadioButton;
    @FXML
    private RadioButton weekRadioButton;
    @FXML
    private TableColumn<Appointment, String> contactAppointmentColumn;
    @FXML
    private TableColumn<Appointment, String> customerAppointmentColumn;
    @FXML
    private TableColumn<Appointment, String> typeAppointmentColumn;
    @FXML
    private TableColumn<Appointment, String> titleAppointmentColumn;
    @FXML
    private TableColumn<Appointment, String> endAppointmentColumn;
    @FXML
    private TableColumn<Appointment, String> startAppointmentColumn;
    @FXML
    private TableView<Appointment> appointmentTableView;
    @FXML
    public TableView<Customer> customerTable;
    @FXML
    private ToggleGroup appointmentToggleGroup;
    @FXML
    private ObservableList<Appointment> appointmentList;

    private final DateTimeFormatter timeDTF = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
    private SchedulerApp mainApp;
    private User currentUser;
    private Customer selectedCustomer;
    private Window primaryStage;
    


    //handles the customer selection click
    @FXML
    public void handleCustomerClick(MouseEvent event) {
        selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        int customerID = selectedCustomer.getCustomerID();
    }

    //handles the new appointment screen
    @FXML
    public void handleNewAppointment(ActionEvent event) throws IOException  {
        Stage stage = new Stage();

        Parent root = FXMLLoader.load(getClass().getResource("AppointmentModify.fxml"));

        try {
            root = FXMLLoader.load(getClass().getResource("AppointmentModify.fxml"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(
                ((Node)event.getSource()).getScene().getWindow() );
        stage.show();
    }
    //handles the edit appointment screen

    /**
     *
     * @param event
     * @throws IOException
     */
    @FXML
    public void handleEditAppointment(ActionEvent event) throws IOException {

        //retrieves the information of selected customer
        Appointment selectAppointment = appointmentTableView.getSelectionModel().getSelectedItem();


        if (selectAppointment != null) {
        try {
            //retrieves the selected appointments appointmentID
            selectedApptID = selectAppointment.getAppointmentID();
            System.out.println(selectedApptID + " :Appt ID");
            System.out.println(selectAppointment.getStart());

            //modify appointment screen is shown using fxml file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainScreenController.class.getResource("AppointmentModify.fxml"));
            AnchorPane editApptScreen = loader.load();

            //separate dialog stage is shown for window
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Appointment");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(editApptScreen);
            dialogStage.setScene(scene);

            //controller is given access
            AppointmentModifyController controller = loader.getController();
            controller.setAppointmentScreen(selectAppointment);

            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }

        }

        //alert if no appointment is selected while clicking edit button
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error Editing Appointment");
            alert.setContentText("Please Select an Appointment to Edit!");
            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    alert.close();
                }
            }));
        }
    }

    //handles the delete appointment button
    @FXML
    public void handleDeleteAppointment(ActionEvent event) {
        //retrieves the information of selected appointment
        Appointment selectedAppointment = appointmentTableView.getSelectionModel().getSelectedItem();

        //creates an alert if delete is clicked
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        alert.setHeaderText("Delete Appointment Record");
        alert.setContentText("Delete Appointment?");
        alert.showAndWait().ifPresent((response -> {
            if (response == ButtonType.OK) {
                AppointmentDB.deleteAppointment(selectedAppointment.getAppointmentID());
                appointmentTableView.setItems(AppointmentDB.getItemsToAdd());
            }
        }));
    }

    //handles the appointment week radio button
    @FXML
    public void handleAppointmentWeek(ActionEvent event) {

        LocalDate currentDate = LocalDate.now();
        LocalDate oneWeekFromCurrent = currentDate.plusDays(7);
        FilteredList<Appointment> appointmentFilteredList = new FilteredList<>(AppointmentDB.allAppointments);
        appointmentFilteredList.setPredicate(row -> {
            LocalDate dateOnly = row.getDateOnly();

            return dateOnly.isBefore(oneWeekFromCurrent) && dateOnly.isAfter(currentDate.minusDays(1));
        });
        appointmentTableView.setItems(appointmentFilteredList);
        System.out.println(appointmentFilteredList);
    }

    //handles the appointment month radio button
    @FXML
    public void handleAppointmentMonth(ActionEvent event) {

        LocalDate currentDate = LocalDate.now();
        LocalDate oneMonthFromCurrent = currentDate.plusDays(30);
        FilteredList<Appointment> appointmentFilteredList = new FilteredList<>(AppointmentDB.allAppointments);
        appointmentFilteredList.setPredicate(row -> {
            LocalDate dateOnly = row.getDateOnly();

            return dateOnly.isBefore(oneMonthFromCurrent) && dateOnly.isAfter(currentDate.minusDays(1));
        });
        appointmentTableView.setItems(appointmentFilteredList);
        System.out.println(appointmentFilteredList);
    }


    //handles the appointment close button
    @FXML
    public void handleAppointmentCloseButton(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    //initializes the appointment screen table view & week/month toggle button
    @Override
    public void initialize(URL url, ResourceBundle resources) {
        appointmentTableView.setItems(AppointmentDB.getItemsToAdd());

        startAppointmentColumn.setCellValueFactory(cellData -> {
            return cellData.getValue().startProperty();
        });
        endAppointmentColumn.setCellValueFactory(cellData -> {
            return cellData.getValue().endProperty();
        });
       titleAppointmentColumn.setCellValueFactory(cellData -> {
            return cellData.getValue().titleProperty();
        });
        typeAppointmentColumn.setCellValueFactory(cellData -> {
            return cellData.getValue().typeProperty();
        });
        customerAppointmentColumn.setCellValueFactory(cellData -> {
            return cellData.getValue().descriptionProperty();
        });
        contactAppointmentColumn.setCellValueFactory(cellData -> {
            return cellData.getValue().contactProperty();
        });

        //toggle for week and month radio button
        appointmentToggleGroup = new ToggleGroup();
        this.weekRadioButton.setToggleGroup(appointmentToggleGroup);
        this.monthRadioButton.setToggleGroup(appointmentToggleGroup);


    }

    //button to reset/refresh the appointment table view
    @FXML
    public void handleResetFilter(ActionEvent event) throws IOException {
         appointmentTableView.setItems(AppointmentDB.getItemsToAdd());
    }
}
