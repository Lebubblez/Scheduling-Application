/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software2schedulingapp.Models;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author ledaerlandson
 */
public class Appointment {

    private SimpleStringProperty typeAmount;

    private StringProperty type;
    private Customer customer;

    public void setType(String type) {this.type.set(type);}

    //getters and setters used to allow for manipulation of dataObservableList later
    public void setAppointmentID(int appointmentID) {
        this.appointmentID.set(appointmentID);
    }

    private IntegerProperty appointmentID;

    public void setAppointmentCustomerID(int appointmentCustomerID) {
        this.appointmentCustomerID.set(appointmentCustomerID);
    }

    public void setStart(String start) {
        this.start.set(start);
    }

    private IntegerProperty appointmentCustomerID;
    private final StringProperty start;

    public void setEnd(String end) {
        this.end.set(end);
    }

    private StringProperty end;

    public void setTitle(String title) {
        this.title.set(title);
    }

    private StringProperty title;

    public void setDescription(String description) {
        this.description.set(description);
    }

    private StringProperty description;

    public void setCountry(String country) {
        this.country.set(country);
    }

    private StringProperty country;

    public void setContact(String contact) {
        this.contact.set(contact);
    }

    private StringProperty contact;



    /**
     *
     * @param appointmentID
     * @param appointmentCustomerID
     * @param start
     * @param end
     * @param title
     * @param description
     * @param country
     * @param contact
     * @param type
     */

    //creates an appointment object where TableView can observe and automatically refresh
    //due to using simpleIntegerProperty and the observableList table
    public Appointment(int appointmentID, int appointmentCustomerID, String start,
                       String end, String title, String description, String country,
                       String contact, String type) {
        this.appointmentID = new SimpleIntegerProperty(appointmentID);
        this.appointmentCustomerID = new SimpleIntegerProperty(appointmentCustomerID);
        this.start = new SimpleStringProperty(start);
        this.end = new SimpleStringProperty(end);
        this.title = new SimpleStringProperty(title);
        this.description = new SimpleStringProperty(description);
        this.country = new SimpleStringProperty(country);
        this.contact = new SimpleStringProperty(contact);
        this.type = new SimpleStringProperty(type);
    }

    public Appointment(String start, String type, String typeAmount) {
        this.start = new SimpleStringProperty(start);
        this.type = new SimpleStringProperty(type);
        this.typeAmount = new SimpleStringProperty(typeAmount);
    }


    public int getAppointmentID() {
        return appointmentID.get();
    }

    public IntegerProperty appointmentIDProperty() {
        return appointmentID;
    }

    public int getAppointmentCustomerID() {
        return appointmentCustomerID.get();
    }

    public IntegerProperty appointmentCustomerIDProperty() {
        return appointmentCustomerID;
    }

    public String getStart() {
        return start.get();
    }

    public StringProperty startProperty() {
        return start;
    }

    public String getEnd() {
        return end.get();
    }

    public StringProperty endProperty() {
        return end;
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() { return description; }

    public String getType() {return type.get();}

    public StringProperty typeProperty() {
        return type;
    }

    public String getCountry() {
        return country.get();
    }

    public StringProperty countryProperty() {
        return country;
    }

    public String getContact() {
        return contact.get();
    }

    public StringProperty contactProperty() {
        return contact;
    }

    //TODO: EDIT!!
    public LocalDate getDateOnly() {
        Timestamp timestamp = Timestamp.valueOf(this.start.get());
        ZonedDateTime dateTime;
        ZoneId zoneId;
        LocalDate localDate;
        if(this.country.get().equals("New York")) {
            zoneId = ZoneId.of("America/New_York");
        } else if(this.country.get().equals("Phoenix")) {
            zoneId = ZoneId.of("America/Phoenix");
        } else {
            zoneId = ZoneId.of("Europe/London");
        }
        dateTime = timestamp.toLocalDateTime().atZone(zoneId);
        localDate = dateTime.toLocalDate();
        return localDate;
    }

    //TODO: EDIT!!
    public String getTimeOnly(DateTimeFormatter timeDTF) {
        Timestamp timestamp = Timestamp.valueOf(this.start.get());
        ZonedDateTime dateTime;
        ZoneId zoneId;
        LocalTime localTime;


        if(this.country.get().equals("New York")) {
            zoneId = ZoneId.of("America/New_York");
            dateTime = timestamp.toLocalDateTime().atZone(zoneId);
            localTime = dateTime.toLocalTime().minusHours(4); //4
            System.out.println("New York: " + localTime);
            System.out.println("datetime: "+ dateTime);
        } else if(this.country.get().equals("Phoenix")) {
            zoneId = ZoneId.of("America/Phoenix");
            dateTime = timestamp.toLocalDateTime().atZone(zoneId);
            localTime = dateTime.toLocalTime().minusHours(7); //7
            System.out.println("datetime: "+ dateTime);
            System.out.println("Phoenix: " + localTime);
        } else {
            zoneId = ZoneId.of("Europe/London");
            dateTime = timestamp.toLocalDateTime().atZone(zoneId);
            localTime = dateTime.toLocalTime().minusHours(10); //1
            System.out.println("datetime: "+ dateTime);
            System.out.println("London: " + localTime);
        }

        int hourTime = Integer.parseInt(localTime.toString().split(":")[0]);
        int minTime = Integer.parseInt(localTime.toString().substring(3,5));

        if(hourTime > 12) {
            hourTime = hourTime - 12;
        }
        String dayNight;
        //below 9 is from 12pm-4pm business hours
        if(hourTime < 9 || hourTime == 12) {
            dayNight = "PM";
        } else {
            dayNight = "AM";
        }
        String min = "";
        if(minTime == 0) {
            min = "00";
        }
        else {
            min = String.valueOf(minTime);
        }

        String time = hourTime + ":" + min + dayNight;
        System.out.println("timeStamp after: " + localTime);
        System.out.println("mintime after: " + min);
        System.out.println("hourtime after: " + hourTime);
        System.out.println("TIME: " + time);
        return time;
    }

    public String getTypeAmount() {
        return typeAmount.get();
    }

    public SimpleStringProperty typeAmountProperty() {
        return typeAmount;
    }

    public void setTypeAmount(String typeAmount) {
        this.typeAmount.set(typeAmount);
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    //sets up the format for the alert of a 15min appointment
    public String get15Time() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.n");

        LocalDateTime localDateTime = LocalDateTime.parse(this.start.getValue(), dateTimeFormatter);

        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("UTC"));

        ZoneId zoneId = ZoneId.systemDefault();

        ZonedDateTime zoneSameInstant = zonedDateTime.withZoneSameInstant(zoneId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        LocalTime localTime = LocalTime.parse(zoneSameInstant.toString().substring(11,16), formatter);

        System.out.println(" get 15 localtime: "+localTime);
        System.out.println("zone same instant "+zoneSameInstant);

        return localTime.toString();
    }
}
