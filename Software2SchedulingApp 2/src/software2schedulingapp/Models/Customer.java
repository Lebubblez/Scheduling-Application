/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software2schedulingapp.Models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

/**
 *
 * @author ledaerlandson
 */
public final class Customer {

    //declarations
    private final SimpleIntegerProperty customerID = new SimpleIntegerProperty();
    private final SimpleStringProperty customerName = new SimpleStringProperty();
    private final SimpleStringProperty address = new SimpleStringProperty();
    private final SimpleStringProperty city = new SimpleStringProperty();
    private final SimpleStringProperty postalCode = new SimpleStringProperty();
    private final SimpleStringProperty phoneNumber = new SimpleStringProperty();
    private final SimpleStringProperty address2 = new SimpleStringProperty();

    /**
     *
     * @param customerID
     * @param customerName
     * @param address
     * @param address2
     * @param city
     * @param postalCode
     * @param phoneNumber
     */

    //Initializes parameters within the constructor
    //creates a customer object where TableView can observe and automatically refresh
    //due to using simpleIntegerProperty and the observableList table
    public Customer(int customerID, String customerName,
                    String address, String address2, String city, String postalCode, String phoneNumber) {
        setCustomerID(customerID);
        setCustomerName(customerName);
        setAddress(address);
        setAddress2(address2);
        setCity(city);
        setPhoneNumber(phoneNumber);
        setPostalCode(postalCode);
    }

    public Customer(){}

    public Customer(String city, String count) {
    }

    //getters and setters for customer
    public int getCustomerID() {
        return customerID.get();
    }

    public IntegerProperty customerIDProperty() {
        return customerID;
    }
    private void setCustomerID(int customerID) {
        this.customerID.set(customerID);
    }

    public String getCustomerName() {
        return customerName.get();
    }

    public StringProperty customerNameProperty() {
        return customerName;}

    public StringProperty phoneProperty() {
        return phoneNumber;}

    private void setCustomerName(String customerName) {
        this.customerName.set(customerName);
    }

    public String getAddress() {
        return address.get();
    }

    private void setAddress(String address) {
        this.address.set(address);
    }

    public String getCity(ObservableList<String> cityList) {
        return city.get();
    }

    private void setCity(String city) {
        this.city.set(city);
    }

    public String getPostalCode() {
        return postalCode.get();
    }

    private void setPostalCode(String postalCode) {
        this.postalCode.set(postalCode);
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    private void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    public String getAddress2() {
        return address2.get();
    }

    private void setAddress2(String address2) {
        this.address2.set(address2);
    }
}
