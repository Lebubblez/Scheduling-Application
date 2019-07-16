/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software2schedulingapp.Utilities;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author ledaerlandson
 */
public class DataBase {

    private static Connection connectDataBase;

    public DataBase(){}

    //Connection is initiated
    public static Connection getConn(){

        return connectDataBase;
    }

    //Connection is closed
    public static void closeConn(){
        try{
            connectDataBase.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally{
            System.out.println("Disconnecting from Database. Goodbye");
        }
    }

    //allows for the connection to the database
    public static void connect(){

        System.out.println("Database Connection In Progress");
        try{
            Class.forName("com.mysql.jdbc.Driver");

            //User: U05UfR 
            //PassWord: 53688608108
            //Host: 52.206.157.109:3306
            
            connectDataBase = DriverManager.getConnection("jdbc:mysql://52.206.157.109:3306/U05UfR", "U05UfR", "53688608108");
        }catch (ClassNotFoundException ce){
            System.out.println("Correct class not found");
            ce.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
