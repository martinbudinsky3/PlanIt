package com.example.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;
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
    private Button save;

    @FXML
    private TextField titleField;

    @FXML
    private TextField locationField;

    @FXML
    private TextField startsField;

    @FXML
    private TextField endsField;

    @FXML
    private TextField alertsField;

    @FXML
    private DatePicker date;

    private int idUser;

    public PlanItAddEventController(int idUser){
        this.idUser = idUser;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // set text to time fields
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime nextHalfHour = countNextHourUnit(LocalTime.now(), 30);
        LocalTime endsInitTime = nextHalfHour.plusMinutes(30);
        LocalTime alertsInitTime = nextHalfHour.minusMinutes(15);
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
}
