/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software2schedulingapp.Models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import software2schedulingapp.Utilities.DataBase;

/**
 *
 * @author ledaerlandson
 */
public class CustomerDB {
    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

    //adds customer details from database to customer table
    public static ObservableList<Customer> getItemsToAdd() {
        try {
            allCustomers.clear();
            Statement statement = (Statement) DataBase.getConn().createStatement();
            String query = "SELECT customer.customerId, customer.customerName, address.address, address.address2, address.phone, address.postalCode, city.city"
                    + " FROM customer INNER JOIN address ON customer.addressId = address.addressId "
                    + "INNER JOIN city ON address.cityId = city.cityId ";
            ResultSet results = statement.executeQuery(query);
            while (results.next()) {
                allCustomers.add(new Customer(
                        results.getInt("customerId"),
                        results.getString("customerName"),
                        results.getString("address"),
                        results.getString("address2"),
                        results.getString("city"),
                        results.getString("postalCode"),
                        results.getString("phone"))
                );

            }
            statement.close();
            return allCustomers;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }

    }

    //handles delete customer, but removing necessary details from database
    public static void deleteCustomer(int id) throws SQLException {
        Statement statement = (Statement) DataBase.getConn().createStatement();
        String queryOne = "DELETE FROM customer WHERE addressId= " + id;
        String query = "DELETE FROM address WHERE addressId= " + id;
        String queryTwo = "DELETE FROM appointment WHERE customerId=" + id;
        String queryThree = "DELETE FROM customer where customerId=" + id;
        int updateTwo = statement.executeUpdate(queryTwo);
        int updateOne = statement.executeUpdate(queryOne);
        int update = statement.executeUpdate(query);
        int updateThree = statement.executeUpdate(queryThree);
    }

    //handles save customer to database
    public static boolean saveCustomer(String customerName, String address, String address2, int cityId, String postalCode, String phoneNumber) throws SQLException {
        String query;
        Statement statement = (Statement) DataBase.getConn().createStatement();

        //auto-increments the primary customerID key
        int maxCustId = 0;
        try{
            query = "SELECT MAX(customerId) AS custId FROM customer ";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                maxCustId = rs.getInt("custId");
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }

        //adds 1 to customerID for each new appointment created
        maxCustId++;

        try {
            int addressId = allCustomers.size() + 1;
            System.out.println(addressId);
            System.out.println(cityId);
            String queryOne = "INSERT INTO address SET address='" + address + "', address2='" + address2 + "', phone='" + phoneNumber + "', createdBy=' ', lastUpdateBy=' ', createDate=NOW(), postalCode='" + postalCode + "', addressId=" + addressId + ", cityId=" + cityId;
            int updateOne = statement.executeUpdate(queryOne);
            if(updateOne == 1) {
                String queryTwo = "INSERT INTO customer SET customerId=" + maxCustId + ", customerName='" + customerName + "', active=1, createDate=NOW(), createdBy=' ', lastUpdateBy=' ', lastUpdate=NOW(), addressId=" + addressId;
                int updateTwo = statement.executeUpdate(queryTwo);
                if(updateTwo == 1) {
                    System.out.println(queryTwo);
                    System.out.println("Successfull Save!");
                    return true;
                }
                else {
                    System.out.println("Failed to save!");
                }
            }
            System.out.println(queryOne);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }

        return false;
    }

    //handles edit, by retrieving data needed to pre-fill text-boxes with customer information
    public static boolean editCustomer(int editId, String customerName, String address, String address2, int cityId, String postalCode, String phoneNumber) {
        try {
            Statement statement = (Statement) DataBase.getConn().createStatement();
            String queryOne = "UPDATE address SET address='" + address + "', address2='" + address2 + "', cityId=" + cityId + ", postalCode='" + postalCode + "', phone='" + phoneNumber + "' "
                    + "WHERE addressId=" + editId;
            int updateOne = statement.executeUpdate(queryOne);
            if(updateOne == 1) {
                String queryTwo = "UPDATE customer SET customerName='" + customerName + "', addressId=" + editId + " WHERE customerId=" + editId;
                int updateTwo = statement.executeUpdate(queryTwo);
                if(updateTwo == 1) {
                    return true;
                }
            }
        } catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return false;
    }

}
