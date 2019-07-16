/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software2schedulingapp.Views;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import software2schedulingapp.Models.Appointment;
import software2schedulingapp.Utilities.DataBase;

/**
 *
 * @author ledaerlandson
 */
public class ReportsScreenController implements Initializable {
    @FXML
    public TabPane tabPane;
    @FXML
    public Tab scheduleTab;
    @FXML
    public Tab appointmentTab;
    @FXML
    public Tab customerTab;
    @FXML
    public BarChart barChart;
    @FXML
    public NumberAxis yAxis;
    @FXML
    public Button closeButton;
    @FXML
    public TableColumn<Appointment, String> monthColumn;
    @FXML
    public TableColumn<Appointment, String> typeColumn;
    @FXML
    public TableColumn<Appointment, String> typeAmount;
    @FXML
    public TableColumn<Appointment, String> scheduleConsultantColumn;
    @FXML
    public TableColumn<Appointment, String> scheduleAppointmentColumn;
    @FXML
    public TableColumn<Appointment, String> scheduleCustomerColumn;
    @FXML
    public TableColumn<Appointment, String> scheduleStartColumn;
    @FXML
    public TableColumn<Appointment, String> scheduleEndColumn;
    @FXML
    public javafx.scene.control.TableView<Appointment> scheduleTable;
    @FXML
    public TableView<Appointment> appointmentTable;
    @FXML
    public CategoryAxis xAxis;

    private static ObservableList<Appointment> scheduleTableList = FXCollections.observableArrayList();
    private static ObservableList<Appointment> monthTableList = FXCollections.observableArrayList();

    //handles close button for report screen
    @FXML
    public void closeReportScreen(ActionEvent event) {
        // handle to the stage
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    //sets up the schedule for each consultants table view
    public ObservableList<Appointment> setScheduleTableView() {
        scheduleTableList.removeAll(scheduleTableList);
        try {
            Statement statement = DataBase.getConn().createStatement();
            String query = "SELECT appointment.appointmentId, appointment.customerId, appointment.title, appointment.contact, appointment.type, "
                    + "appointment.`start`, appointment.`end`, customer.customerId, customer.customerName, appointment.createdBy "
                    + "FROM appointment, customer "
                    + "WHERE appointment.customerId = customer.customerId "
                    + "ORDER BY `contact`";
            ResultSet results = statement.executeQuery(query);

            while(results.next()) {
                scheduleTableList.add(new Appointment(
                        results.getInt("appointmentId"),
                        results.getInt("customerId"),
                        results.getString("start"),
                        results.getString("end"),
                        results.getString("title"),
                        results.getString("customerName"),
                        results.getString("createdBy"),
                        results.getString("contact"),
                        results.getString("type")
                ));
            }
            statement.close();
            return scheduleTableList;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return null;
    }

    //sets a color for consultants for easy legibility
    private void colorConsultantTable(TableColumn<Appointment, String> calltypel) {
        calltypel.setCellFactory(column -> {
            return new TableCell<Appointment, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    setText(empty ? "" : getItem());
                    setGraphic(null);

                    TableRow currentRow = getTableRow();

                    if (!isEmpty()) {

                        if(item.equals("Patrick"))
                            currentRow.setStyle("-fx-background-color:lightblue");
                        else if (item.equals("Sandy"))
                            currentRow.setStyle("-fx-background-color:lightgreen");
                        else
                            currentRow.setStyle("-fx-background-color:lightyellow");
                    }
                }
            };
        });
    }

    // sets up the number of appointment types by month table view
    private ObservableList<Appointment> setMonthTableList() {

        monthTableList.removeAll(monthTableList);

        Statement statement;
        try {
            statement = DataBase.getConn().createStatement();
            String query = "SELECT MONTHNAME(`start`) AS 'Month', type, COUNT(*) as 'Amount' FROM appointment "
                    + "GROUP BY MONTHNAME(`start`), type";
            ResultSet result = statement.executeQuery(query);

            while (result.next()) {
                //WORKING ON SETTING UP COUNTING THE NUMBER OF TYPES
                monthTableList.add(new Appointment(
                        result.getString("Month"),
                        result.getString("type"),
                        result.getString("Amount")
                ));
            }
            statement.close();
            System.out.println(monthTableList);
            return monthTableList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
            }


    //sets up the bar-chart for viewing the number of customers by city location
    private void setUpBarChart() {
        barChart.setStyle("-fx-bar-fill: blue;");
        ObservableList<XYChart.Data<String, Integer>> arrayList = FXCollections.observableArrayList();
        XYChart.Series<String, Integer> integerSeries = new XYChart.Series<>();

            //retrieves queried SQL for bar chart
        try { PreparedStatement statement = DataBase.getConn().prepareStatement(
                "SELECT city.city, COUNT(city) "
                        + "FROM customer, address, city "
                        + "WHERE customer.addressId = address.addressId "
                        + "AND address.cityId = city.cityId "
                        + "GROUP BY city");

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String cityColumn = resultSet.getString("city");
                Integer countColumn = resultSet.getInt("COUNT(city)");
                arrayList.add(new XYChart.Data<>(cityColumn, countColumn));
            }

            //handles error catching for sql and others
        } catch (SQLException sqe) {
            System.out.println("SQL is incorrect");
            sqe.printStackTrace();
        } catch (Exception e) {
            System.out.println("Something went wrong unrelated to the SQL");
        }

        integerSeries.getData().addAll(arrayList);
        barChart.getData().add(integerSeries);
    }


    //initializes the table views by retrieving appropriate DB columns, & call functions
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Consultant Schedule
        scheduleAppointmentColumn.setCellValueFactory(
                cellData -> cellData.getValue().titleProperty()
        );
        scheduleConsultantColumn.setCellValueFactory(
                cellData -> cellData.getValue().contactProperty()
        );
        scheduleCustomerColumn.setCellValueFactory(
                cellData -> cellData.getValue().descriptionProperty()
        );
        scheduleEndColumn.setCellValueFactory(
                cellData -> cellData.getValue().endProperty()
        );
        scheduleStartColumn.setCellValueFactory(
                cellData -> cellData.getValue().startProperty()
        );

        scheduleTable.setItems(scheduleTableList);
        scheduleTable.setItems(setScheduleTableView());
        colorConsultantTable(scheduleConsultantColumn);

        //Appointment type by month schedule
        monthColumn.setCellValueFactory(
                cellData -> cellData.getValue().startProperty()
        );
        typeColumn.setCellValueFactory(
                cellData -> cellData.getValue().typeProperty()
        );
        typeAmount.setCellValueFactory(
                cellData -> cellData.getValue().typeAmountProperty()
        );

        setUpBarChart();
        appointmentTable.setItems(monthTableList);
        appointmentTable.setItems(setMonthTableList());
    }
}
