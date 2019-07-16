/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software2schedulingapp.Views;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import software2schedulingapp.Models.Customer;
import software2schedulingapp.Models.CustomerDB;

/**
 *
 * @author ledaerlandson
 */
public class CustomerScreenController implements Initializable {
    @FXML
    public TextField nameField;
    @FXML
    public TextField addressField;
    @FXML
    public TextField address2Field;
    @FXML
    public TextField postalCodeField;
    @FXML
    public TextField phoneField;
    @FXML
    public TextField countryField;
    @FXML
    public TextField customerIdField;
    @FXML
    public ComboBox<String> cityComboBox;
    @FXML
    public ButtonBar saveCancelButtonBar;
    @FXML
    public Button closeButton;
    @FXML
    public ButtonBar newEditDeleteButtonBar;
    @FXML
    public TableView<Customer> customerTable;
    @FXML
    public TableColumn<Customer, String> customerNameColumn;
    @FXML
    public TableColumn<Customer, String> phoneColumn;

    private Customer customerSelect;
    private final ObservableList<String> cityList = FXCollections.observableArrayList();
    private boolean okClicked;
    private boolean clickEdit = false;
    private Window dialogStage;

    //handle cancel button for customer screen
    @FXML
    public void handleCancelCustomer(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Cancel");
        alert.setHeaderText("Are you sure you want to Cancel?");
        alert.showAndWait();
        Stage stage = (Stage) saveCancelButtonBar.getScene().getWindow();
        stage.close();
    }

    //handle new customer button
    @FXML
    public void handleNewCustomer(ActionEvent event) {
        editCustomerDetails();
        saveCancelButtonBar.setDisable(false);
        customerTable.setDisable(true);
        customerIdField.setText("Auto ID Generation");
        newEditDeleteButtonBar.setDisable(true);
        clickEdit = false;
    }


    //displays editable details of the customer
    private void editCustomerDetails() {
        nameField.setEditable(true);
        addressField.setEditable(true);
        address2Field.setEditable(true);
        cityComboBox.setEditable(true);
        postalCodeField.setEditable(true);
        phoneField.setEditable(true);
    }

    //disables customer details to be edited
    private void disableCustomerDetails() {
        nameField.setEditable(false);
        addressField.setEditable(false);
        address2Field.setEditable(false);
        postalCodeField.setEditable(false);
        phoneField.setEditable(false);
    }


    //handles the save button from both the edit, and new customer option
    @FXML
    public void handleSaveCustomer(ActionEvent event) throws SQLException {
        String custName = nameField.getText();
        String custAdd = addressField.getText();
        String custAdd2 = address2Field.getText();
        //included the +1, since index naturally starts at 0
        int custCity = cityComboBox.getSelectionModel().getSelectedIndex()+1;
        String custPostal = postalCodeField.getText();
        String custPhone = phoneField.getText();

        //checks for validating customer
        if (addCustomerValidation()) {
            if (!clickEdit) {
                System.out.println(CustomerDB.saveCustomer(custName, custAdd, custAdd2, custCity, custPostal, custPhone));
            } else {
                //else includes editId parameter to show customer selection data in textFields
                int editId = customerTable.getSelectionModel().getSelectedItem().getCustomerID();
                System.out.println(CustomerDB.editCustomer(editId, custName, custAdd, custAdd2, custCity, custPostal, custPhone));
            }
        }

        System.out.println(custCity);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Save");
        alert.setHeaderText("Are you sure you want to Save?");
        alert.showAndWait();
        Stage stage = (Stage) saveCancelButtonBar.getScene().getWindow();
        stage.close();

    }
    public boolean isOkClicked() {
        return okClicked;
    }

    public void setOkClicked(boolean okClicked) {
        this.okClicked = okClicked;
    }

    //handles the edit customer button
    @FXML
    public void handleEditCustomer(ActionEvent event) {

        //clickEdit allows for differentiation between 'saving' for new customer, & an edited customer information
        clickEdit = true;
        boolean bool = true;
        customerSelect = customerTable.getSelectionModel().getSelectedItem();
        setCustomerScreen(customerSelect);
        customerTable.setDisable(bool);
        newEditDeleteButtonBar.setDisable(bool);

    }

    //handles delete customer button
    @FXML
    public void handleDeleteCustomer(ActionEvent event) {

        //retrieves customer infomration, based on the customer selected in the table
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();

        //creates an alert if delete is clicked
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        alert.setHeaderText("Delete Customer Record");
        alert.setContentText("Delete Customer?");
        alert.showAndWait().ifPresent((new Consumer<ButtonType>() {
            @Override
            public void accept(ButtonType response) {
                if (response == ButtonType.OK) {
                    try {
                        CustomerDB.deleteCustomer(selectedCustomer.getCustomerID());
                    } catch (SQLException e) {
                    }
                    //re-adds the customer information to the table, with added deletions made
                    customerTable.setItems(CustomerDB.getItemsToAdd());
                }
            }
        }));
    }

    //handles the close screen button
    @FXML
    public void handleCloseButton(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }


    //handles the country based on which city is selected in the city combo-box
    @FXML
    public void handleCountry() {
        String city = cityComboBox.getSelectionModel().getSelectedItem();
        switch (city) {
            case "Phoenix":
                countryField.setText("United States");
                break;
            case "London":
                countryField.setText("England");
                break;
            case "New York":
                countryField.setText("United States");
                break;
            default:
                break;
        }
        System.out.println(city);
    }

    //initializes the customer table by retrieving the data for customer name, and phone number column
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //initializing customer table of customer names
        customerTable.setItems(CustomerDB.getItemsToAdd());

        customerNameColumn.setCellValueFactory(cellData -> {
            return cellData.getValue().customerNameProperty();
            });

        phoneColumn.setCellValueFactory(cellData -> {
            return cellData.getValue().phoneProperty();
            });

        //sets up list of cities in city combo-box
        cityList.addAll("Phoenix", "London", "New York");
        cityComboBox.setItems(cityList);

    }

    //validation for appointment submission
    private boolean addCustomerValidation() {
        String customerName = nameField.getText();
        String customerAddress = addressField.getText();
        String customerAddress2 = address2Field.getText();
        Object customerCity = cityComboBox.getValue();
        String customerPostal = postalCodeField.getText();
        String customerPhoneNumber = phoneField.getText();

        String alertText = "";
        if (customerName == null || customerName.length() == 0) {
            alertText += "Please Enter a Customer Name.\n";
        }
        if (customerAddress == null) {
            alertText += "Please Enter an Customer Address.\n";
        }
        if (customerAddress2 == null) {
            alertText += "Please Enter an Second Customer Address.\n";
        }
        if (customerCity == null) {
            alertText += "Please Select a City. \n";
        }
        if (customerPhoneNumber ==  null) {
            alertText += "Please Enter a Valid Phone Number.\n";
        } else if (customerPhoneNumber.length() < 10 || customerPhoneNumber.length() > 15 ){
            alertText += "Please enter a valid phone number (including Area Code).\n";
        }
        if (customerPostal == null) {
            alertText += "Please Enter a Valid Postal Code.\n";
        } else if (customerPostal.length() > 10 || customerPostal.length() < 5){
            alertText += "Please enter a valid Postal Code.\n";
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

    //Initializes the details for the customer in text fields
    @FXML
    private void setCustomerScreen(Customer customerSelect) {
        okClicked = true;

        customerIdField.setText(String.valueOf(customerSelect.getCustomerID()));
        nameField.setText(customerSelect.getCustomerName());
        addressField.setText(customerSelect.getAddress());
        address2Field.setText(customerSelect.getAddress2());
        cityComboBox.setValue(customerSelect.getCity(cityList));
        postalCodeField.setText(customerSelect.getPostalCode());
        phoneField.setText(customerSelect.getPhoneNumber());
    }

    //clickEdit allows for differentiation between 'saving' for new customer, & an edited customer information
    public boolean isClickEdit() {
        return clickEdit;
    }

    public void setClickEdit(boolean clickEdit) {
        this.clickEdit = clickEdit;
    }

}
