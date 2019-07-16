/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software2schedulingapp.Models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import software2schedulingapp.Utilities.DataBase;

/**
 *
 * @author ledaerlandson
 */
public class AppointmentDB {

    public static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    private static final ObservableList<Appointment> reminderFifteenList = FXCollections.observableArrayList();
    private final DateTimeFormatter timeDTF = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);

    //Creates the visual table for the Appointment Screen from database
    public static ObservableList<Appointment> getItemsToAdd() {
        try {
            allAppointments.removeAll(allAppointments);
            Statement statement = (Statement) DataBase.getConn().createStatement();
            String query = "SELECT appointment.appointmentId, appointment.customerId, appointment.title, appointment.contact, appointment.type, "
                    + "appointment.`start`, appointment.`end`, customer.customerId, customer.customerName, appointment.createdBy "
                    + "FROM appointment, customer "
                    + "WHERE appointment.customerId = customer.customerId ORDER BY start";
            ResultSet results = statement.executeQuery(query);
            while (results.next()) {
                allAppointments.add(new Appointment(
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
            return allAppointments;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }

    }

    //reminder for 15min appointment
    public static Appointment setReminderFifteen() {
        Appointment appointment;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:00");
        LocalDateTime now = LocalDateTime.now();
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = now.atZone(zoneId);
        Timestamp localDateTime = Timestamp.valueOf(zonedDateTime.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());

        String formatLocal = localDateTime.toLocalDateTime().format(dateTimeFormatter);

        Timestamp localDateTime2 = Timestamp.valueOf(zonedDateTime.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime().plusMinutes(15));
        String formatLocal2 = localDateTime2.toLocalDateTime().format(dateTimeFormatter);

        try {
            System.out.println("reminder before: " + reminderFifteenList);
            java.sql.Statement statement = DataBase.getConn().createStatement();
            String query = "SELECT * FROM appointment WHERE start BETWEEN '" + formatLocal + "' AND '" + formatLocal2 + "' AND " +
                    "userId = userId";
            System.out.println(query);
            ResultSet results = statement.executeQuery(query);
            if (results.next()) {
                appointment = new Appointment(
                        results.getInt("appointmentId"),
                        results.getInt("customerId"),
                        results.getString("start"),
                        results.getString("end"),
                        results.getString("title"),
                        results.getString("description"),
                        results.getString("createdBy"),
                        results.getString("contact"),
                        results.getString("type"));
                System.out.println(appointment);
                System.out.println("reminder after: " + reminderFifteenList);
                return appointment;
            }
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //saves an updated appointment to the database
    protected static void updateAppointment(int custId, String title, String type, String contact, String location, String date, String time, int apptId) throws SQLException {

        //creates a timestamp for start and end times
        String timeStart = timeStamp(time, date, location, true);
        String timeEnd = timeStamp(time, date, location, false);
        String description = type;
        String user = UserDB.getLoggedInUser().getUser();

        String query = null;
        Statement statement = (Statement) DataBase.getConn().createStatement();
        System.out.println(apptId);
        try {
            query = "UPDATE appointment SET customerId='" + custId + "', title='" + title + "', type='" +
                    type + "', description='" + description + " ', contact='" + contact + "', location='" + location + "', start='" + timeStart + "', end='" +
                    timeEnd + "', url='N/A', createDate=NOW(), createdBy='" + user + "', lastUpdate=NOW(), lastUpdateBy='" + user + "' WHERE appointment.appointmentId=" + apptId + "";

            int update = statement.executeUpdate(query);
            if (update == 1) {
                System.out.println("New Appointment Saved Successfully!");
            } else {
                System.out.println("New Appointment Saved Miserably!");
            }
        } catch (SQLIntegrityConstraintViolationException b) {
            System.out.println("Integrity violation " + b.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        System.out.println(query);

    }

    //saves a new appointment to the database
    protected static void saveAppointment(int customerId, String title, String type, String contact, String location, String date, String time) throws SQLException {

        //creates a timestamp for start and end times
        String timeStart = timeStamp(time, date, location, false);
        String timeEnd = timeStamp(time, date, location, true);
        String description = type;
        String query = null;
        String user = UserDB.getLoggedInUser().getUser();
        Statement statement = (Statement) DataBase.getConn().createStatement();

        //auto-increments the primary appointmentID key
        int maxApptId = 0;
        try{
            query = "SELECT MAX(appointmentId) AS apptId FROM appointment ";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                maxApptId = rs.getInt("apptId");
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }

        //adds 1 to appointmentID for each new appointment created
        maxApptId++;

        //Saves an appointment into the database
        try {
            query = "INSERT INTO appointment SET appointmentId= " + maxApptId + ", customerId='" + customerId + "', title='" + title + "', type='" +
                    type + "', description='" + description + " ', contact='" + contact + "', location='" + location + "', start='" + timeStart + "', end='" +
                    timeEnd + "', url='N/A', createDate=NOW(), createdBy='" + user + "', lastUpdate=NOW(), lastUpdateBy='" + user + "'";
            int update = statement.executeUpdate(query);

            if (update == 1) {
                System.out.println("New Appointment Saved Successfully!");
            } else {
                System.out.println("New Appointment Saved Miserably!");
            }
        } catch (SQLIntegrityConstraintViolationException b) {
            System.out.println("Integrity violation " + b.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        System.out.println(query);
    }

    //Deletes an appointment
    public static void deleteAppointment(int appointmentID) {
        try {
            Statement statement = (Statement) DataBase.getConn().createStatement();
            String query = "DELETE FROM appointment WHERE appointmentId = " + appointmentID;
            int update = statement.executeUpdate(query);
            if(update == 1) {
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    //checks for appointment conflicts
    public static boolean appointmentConflicts(int apptId, String time, String date, String location) {

        String timeStart = timeStamp(time, date, location, true);

        try {
            Statement statement = (Statement) DataBase.getConn().createStatement();
            String query = "SELECT * FROM appointment WHERE start = '" + timeStart + "' AND location = '" + location + "'";
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                if (resultSet.getInt("appointmentId") == apptId) {
                    statement.close();
                    return false;
                }
                statement.close();
                return true;
            } else {
                statement.close();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    //Creates a timestamp using the selected time, date, and location
    private static String timeStamp(String time, String date, String location, boolean endTime) {
       //giving time as a single number
        String[] hour = time.split(":");

        //hour and min time parsed
           int hourTime = Integer.parseInt(hour[0]);
           int minTime = Integer.parseInt(hour[1].substring(0,2));

           //startTime + 1 hour will be the endtime
           if (!endTime) {
               minTime += 15;
               //makes sure min is set back to 0 when hitting 60min, not 60
               if (minTime > 45) {
                    minTime = 0;
                    hourTime += 1;
               }
           }

           //if start time or endtime less than 9am, 12 hours will be added
           if (hourTime < 9) {
               hourTime += 12;
           }

        System.out.println("Min Time: " + minTime);
        System.out.println("Hour Time: " + hourTime);
        String min = "";

        //makes sure min at time 0 is set to :00
        if(minTime == 0) {
            min = "00";
        }
        else {
            min = String.valueOf(minTime);
        }
        //format for saving the dateTime
           String dateTime = String.format("%s %02d:%s", date, hourTime, min);
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm");
            LocalDateTime localDateTime = LocalDateTime.parse(dateTime, dateTimeFormatter);

        //sets up time by providing UTC
           ZoneId zoneId = ZoneId.systemDefault();
           ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
           ZonedDateTime utcDate = zonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));
           localDateTime = utcDate.toLocalDateTime();
           Timestamp timestamp = Timestamp.valueOf(localDateTime);


        System.out.println("zonedatetime "+zonedDateTime);
        System.out.println("utcdate "+utcDate);
        System.out.println("localdatetime "+localDateTime);
        System.out.println("Database savetime: "+timestamp.toString());
           return timestamp.toString();
    }


}
