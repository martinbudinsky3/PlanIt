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
    private TextField alertsField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private DatePicker dateField;

    private int idUser;
    private LocalDate initDate;
    private EventsClient eventsClient;
    private PlanItMainWindowController planItMainWindowController;

    public PlanItAddEventController(int idUser, LocalDate initDate, EventsClient eventsClient, PlanItMainWindowController planItMainWindowController){
        this.idUser = idUser;
        this.initDate = initDate;
        this.eventsClient = eventsClient;
        this.planItMainWindowController = planItMainWindowController;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // set text to time fields
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime nextHalfHour = countNextHourUnit(LocalTime.now(), 30);
        LocalTime endsInitTime = nextHalfHour.plusMinutes(30);
        LocalTime alertsInitTime = nextHalfHour.minusMinutes(15);
        dateField.setValue(initDate);
        startsField.setText(nextHalfHour.format(dtf));
        endsField.setText(endsInitTime.format(dtf));
        alertsField.setText(alertsInitTime.format(dtf));

        // add handlers to increment decrement buttons of time text fields
        final int size = 3;  // constant size of arrays
        Button [] incrementButtons = {startsIncrement, endsIncrement, alertsIncrement};
        Button [] decrementButtons = {startsDecrement, endsDecrement, alertsDecrement};
        TextField [] textFields = {startsField, endsField, alertsField};

        for(int i = 0; i < size; i++){
            TextField textField = textFields[i];
            incrementButtons[i].setOnAction(e -> incrementTimeTextField(textField, 30));
            decrementButtons[i].setOnAction(e -> decrementTimeTextField(textField, 30));
        }

        saveButton.setOnAction(e -> save(e));
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
        LocalTime actualTime = LocalTime.parse(textField.getText());
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
        LocalTime actualTime = LocalTime.parse(textField.getText());
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
        String title = titleField.getText();
        String location = locationField.getText();
        LocalDate dateValue = dateField.getValue();
        LocalTime starts = null;
        LocalTime ends = null;
        LocalTime alert = null;
        String description = descriptionField.getText();

        boolean checkFlag = true;
        if(title == null){
            titleError.setVisible(true);
            checkFlag = false;
        }

        if(dateValue == null){
            dateError.setVisible(true);
            checkFlag = false;
        }

        if(startsField.getText() == null){
            startsError.setVisible(true);
            checkFlag = false;
        } else {
            starts = LocalTime.parse(startsField.getText());
        }

        if(endsField.getText() == null){
            endsError.setVisible(true);
            checkFlag = false;
        } else {
            ends = LocalTime.parse(endsField.getText());
        }

        if(alertsField.getText() == null){
            alertsError.setVisible(true);
            checkFlag = false;
        } else {
            alert = LocalTime.parse(alertsField.getText());
        }

        if(checkFlag){
            Event event = new Event(title, location, description, dateValue, starts, ends, alert, idUser);
            try {
                Integer id = eventsClient.addEvent(event);
                if(id != null) {
                    // update calendar display
                    planItMainWindowController.setSelectedYear(dateValue.getYear());
                    planItMainWindowController.setSelectedMonth(dateValue.getMonth().getValue());
                    planItMainWindowController.initializeCalendar();
                    planItMainWindowController.showEventsInCalendar();
                    Stage stage = (Stage)((Node) ev.getSource()).getScene().getWindow();
                    stage.close();
                } else {
                    // TO DO - some error message
                }
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void hideErrorLabels(){
        titleError.setVisible(false);
        dateError.setVisible(false);
        startsError.setVisible(false);
        endsError.setVisible(false);
        alertsError.setVisible(false);
    }
}
