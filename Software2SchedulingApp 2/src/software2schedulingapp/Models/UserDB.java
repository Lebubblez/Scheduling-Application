/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software2schedulingapp.Models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import software2schedulingapp.Utilities.DataBase;

/**
 *
 * @author ledaerlandson
 */
public class UserDB {

    static User getLoggedInUser() {
        return loggedInUser;
    }

    private static User loggedInUser;


    //handles loging in with user and password retrieval from the database
    public static boolean login(String user, String pass) {
        try {
            Statement statement = DataBase.getConn().createStatement();
            String query = "SELECT * FROM user WHERE password='" + pass + "' AND userName='" + user + "'";
            ResultSet resultSet = statement.executeQuery(query);
            if(resultSet.next()) {
                loggedInUser = new User();
                loggedInUser.setUser(resultSet.getString("userName"));
                statement.close();
                software2schedulingapp.Utilities.Logger.log(user, true);
                return true;

            } else {
                software2schedulingapp.Utilities.Logger.log(user, false);

                return false;
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return false;
        }
    }
}
