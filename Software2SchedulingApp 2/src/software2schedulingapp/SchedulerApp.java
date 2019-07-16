/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software2schedulingapp;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import software2schedulingapp.Utilities.DataBase;

/**
 *
 * @author ledaerlandson
 */
public class SchedulerApp extends Application {

    private SchedulerApp topApp;
    private BorderPane mainScreen;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("Views/LoginScreen.fxml"));
        primaryStage.setTitle("Login - Scheduling Application");
        primaryStage.setScene(new Scene(root, 650, 450));
        primaryStage.show();

    }


    public static void main(String[] args) {
        DataBase.connect();
        launch(args);
        DataBase.closeConn();
    }

}
