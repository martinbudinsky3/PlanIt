package com.example.gui.controllers;

import com.example.client.clients.EventsClient;
import com.example.client.model.Event;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.net.URL;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;


public class PlanItAddEventController implements Initializable {
    @FXML
    private Button startsDecrement;

    @FXML
    private Button endsDecrement;

    @FXML
    private Button alertsDecrement;

    @FXML
    private Button startsIncrement;

    @FXML
    private Button endsIncrement;

    @FXML
    private Button alertsIncrement;

    @FXML
    private Button saveButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Label titleError;

    @FXML
    private Label dateError;

    @FXML
    private Label startsError;

    @FXML
    private Label endsError;

    @FXML
    private Label alertsError;

    @FXML
    private TextArea titleField;

    @FXML
    private TextField locationField;

    @FXML
    private TextField startsField;

    @FXML
    private TextField endsField;

    @FXML
    private TextField alertField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private DatePicker startsDateField;

    @FXML
    private DatePicker endsDateField;

    @FXML
    private DatePicker alertDateField;

    private Integer idUser;
    private Integer idEvent;
    private LocalDate initDate;
    private EventsClient eventsClient;
    private PlanItMainWindowController planItMainWindowController;
    private Event event;

    public PlanItAddEventController(int idUser, LocalDate initDate, EventsClient eventsClient, PlanItMainWindowController planItMainWindowController){
        this.idUser = idUser;
        this.initDate = initDate;
        this.eventsClient = eventsClient;
        this.planItMainWindowController = planItMainWindowController;
    }

    public PlanItAddEventController(int idUser, int idEvent, EventsClient eventsClient, PlanItMainWindowController planItMainWindowController){
        this.idUser = idUser;
        this.idEvent = idEvent;
        this.eventsClient = eventsClient;
        this.planItMainWindowController = planItMainWindowController;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(idEvent == null) { // add event
            // set text to time fields
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime nextHalfHour = countNextHourUnit(LocalTime.now(), 30);
            LocalTime endsInitTime = nextHalfHour.plusMinutes(30);
            LocalTime alertInitTime = nextHalfHour.minusMinutes(15);
            LocalDate endsDate = null;
            if(endsInitTime.isBefore(nextHalfHour)){
                endsDate = initDate.plusDays(1);
            } else {
                endsDate = initDate;
            }
            LocalDate alertDate = null;
            if(nextHalfHour.isBefore(alertInitTime)){
                alertDate = initDate.minusDays(1);
            } else {
                alertDate = initDate;
            }

            startsField.setText(nextHalfHour.format(dtf));
            endsField.setText(endsInitTime.format(dtf));
            alertField.setText(alertInitTime.format(dtf));
            startsDateField.setValue(initDate);
            endsDateField.setValue(endsDate);
            alertDateField.setValue(alertDate);
        } else { // show detail of already created event
           showDetail();
        }

        // add handlers to increment decrement buttons of time text fields
        final int size = 3;  // constant size of arrays
        Button [] incrementButtons = {startsIncrement, endsIncrement, alertsIncrement};
        Button [] decrementButtons = {startsDecrement, endsDecrement, alertsDecrement};
        TextField [] textFields = {startsField, endsField, alertField};

        for(int i = 0; i < size; i++){
            TextField textField = textFields[i];
            incrementButtons[i].setOnAction(e -> incrementTimeTextField(textField, 30));
            decrementButtons[i].setOnAction(e -> decrementTimeTextField(textField, 30));
        }

        saveButton.setOnAction(e -> save(e));
        deleteButton.setOnAction(e -> delete(e));
    }

    public void showDetail(){
        try {
            event = eventsClient.getEvent(idUser, idEvent);

            titleField.setText(event.getTitle());
            locationField.setText(event.getLocation());
            startsDateField.setValue(event.getDate());
            startsField.setText(String.valueOf(event.getStarts()));
            endsDateField.setValue(event.getEndsDate());
            endsField.setText(String.valueOf(event.getEnds()));
            alertDateField.setValue(event.getAlertDate());
            alertField.setText(String.valueOf(event.getAlert()));
            descriptionField.setText(event.getDescription());
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public LocalTime countNextHourUnit(LocalTime actualTime, int unit){
        int minutes = actualTime.getMinute();
        LocalTime nextHalfHour = actualTime.plusMinutes(unit - minutes % unit);

        return nextHalfHour;
    }

    public LocalTime countPreviousHourUnit(LocalTime actualTime, int unit){
        int minutes = actualTime.getMinute();
        LocalTime previousHalfHour = actualTime.minusMinutes(minutes % unit);

        return previousHalfHour;
    }

    public void incrementTimeTextField(TextField textField, int minutes){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime actualTime = null;
        try {
            actualTime = LocalTime.parse(textField.getText());
        } catch(DateTimeException dte) {
            actualTime = LocalTime.of(0,0);
        }
        if(actualTime.getMinute() % 30 != 0) {
            LocalTime nextHalfHour = countNextHourUnit(actualTime, 30);
            textField.setText(nextHalfHour.format(dtf));
        } else {
            LocalTime time = actualTime;
            time = time.plusMinutes(minutes);
            textField.setText(time.format(dtf));
        }
    }

    public void decrementTimeTextField(TextField textField, int minutes){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime actualTime = null;
        try {
            actualTime = LocalTime.parse(textField.getText());
        } catch(DateTimeException dte) {
            actualTime = LocalTime.of(0,0);
        }
        if(actualTime.getMinute() % 30 != 0){
            LocalTime previousHalfHour = countPreviousHourUnit(actualTime, 30);
            textField.setText(previousHalfHour.format(dtf));
        } else {
            LocalTime time = actualTime;
            time = time.minusMinutes(minutes);
            textField.setText(time.format(dtf));
        }
    }

    public void save(ActionEvent ev) {
        hideErrorLabels();

        String title = titleField.getText();
        String location = locationField.getText();
        LocalDate date = startsDateField.getValue();
        LocalTime starts = null;
        LocalDate endsDate = endsDateField.getValue();
        LocalTime ends = null;
        LocalDate alertDate = alertDateField.getValue();
        LocalTime alert = null;
        String description = descriptionField.getText();

        boolean checkFlag = true;
        if(title.equals("")){
            titleError.setVisible(true);
            checkFlag = false;
        }

        try {
            starts = LocalTime.parse(startsField.getText());
        } catch(DateTimeException dte){
            startsError.setVisible(true);
            checkFlag = false;
        }

        try{
            ends = LocalTime.parse(endsField.getText());
        } catch(DateTimeException dte){
            endsError.setVisible(true);
            checkFlag = false;
        }

        try{
            alert = LocalTime.parse(alertField.getText());
        } catch(DateTimeException dte) {
            alertsError.setVisible(true);
            checkFlag = false;
        }

        if(checkFlag){
            if(idEvent == null) {
                addEvent(ev, title, location, description, date, starts, endsDate, ends, alertDate, alert);
            } else {
                updateEvent(ev, title, location, description, date, starts, endsDate, ends, alertDate, alert);
            }
        }
    }

    public void addEvent(ActionEvent ev, String title, String location, String description, LocalDate date,
                         LocalTime starts, LocalDate endsDate, LocalTime ends, LocalDate alertDate, LocalTime alert){

        Event event = new Event(title, location, description, date, starts, endsDate, ends, alertDate, alert, idUser);
        try {
            Integer id = eventsClient.addEvent(event);
            if (id != null) {
                updateCalendarDisplay(date);
                Stage stage = (Stage) ((Node) ev.getSource()).getScene().getWindow();
                stage.close();
            } else {
                // TO DO - some error message
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateEvent(ActionEvent ev, String title, String location, String description, LocalDate date,
                            LocalTime starts, LocalDate endsDate, LocalTime ends, LocalDate alertDate, LocalTime alert) {

        Event event = new Event(title, location, description, date, starts, endsDate, ends, alertDate, alert, idUser);
        try {
            eventsClient.updateEvent(event, idEvent);
            updateCalendarDisplay(date);
            Stage stage = (Stage) ((Node) ev.getSource()).getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateCalendarDisplay(LocalDate date){
        // update calendar display
        planItMainWindowController.setSelectedYear(date.getYear());
        planItMainWindowController.setSelectedMonth(date.getMonth().getValue());
        planItMainWindowController.initializeCalendar();
        planItMainWindowController.showEventsInCalendar();
    }

    public void hideErrorLabels(){
        titleError.setVisible(false);
        startsError.setVisible(false);
        endsError.setVisible(false);
        alertsError.setVisible(false);
    }

    private void delete(ActionEvent ev) {
        if(idEvent != null && event != null) {
            try {
                System.out.println(idEvent);
                eventsClient.deleteEvent(idUser, idEvent);
            } catch (Exception e){
                System.out.println(e.getMessage());
            } finally {
                LocalDate date = event.getDate();
                updateCalendarDisplay(date);
            }
        }

        Stage stage = (Stage) ((Node) ev.getSource()).getScene().getWindow();
        stage.close();
    }
}
