package com.example.gui.controllers;

import com.example.client.clients.EventsClient;
import com.example.client.model.Event;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for "PlanItAddEvent.fxml"
 */
public class PlanItAddEventController implements Initializable {
    private final Integer idUser;
    private final EventsClient eventsClient;
    private final PlanItMainWindowController planItMainWindowController;

    @FXML
    private AnchorPane ap;

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
    private ChoiceBox<String> typeSelector;

    @FXML
    private DatePicker startsDateField;

    @FXML
    private DatePicker endsDateField;

    @FXML
    private DatePicker alertDateField;

    private Integer idEvent;
    private LocalDate initDate;
    private ResourceBundle resourceBundle;
    private Event event;

    public PlanItAddEventController(int idUser, LocalDate initDate, EventsClient eventsClient, PlanItMainWindowController planItMainWindowController) {
        this.idUser = idUser;
        this.initDate = initDate;
        this.eventsClient = eventsClient;
        this.planItMainWindowController = planItMainWindowController;
    }

    public PlanItAddEventController(int idUser, int idEvent, EventsClient eventsClient, PlanItMainWindowController planItMainWindowController) {
        this.idUser = idUser;
        this.idEvent = idEvent;
        this.eventsClient = eventsClient;
        this.planItMainWindowController = planItMainWindowController;
    }


    /**
     * Setting all pre-prepared times (date and time when new event starts, ends and when the event should be notified).
     * User enters data as title, location, description of the event and also changes dates and times of the event.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        initializeTypeSelector();

        if (idEvent == null) { // add event
            // set init times to time fields
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime nextHalfHour = countNextHourUnit(LocalTime.now(), 30);
            LocalTime endsInitTime = nextHalfHour.plusMinutes(30);
            LocalTime alertInitTime = nextHalfHour.minusMinutes(15);
            LocalDate endsDate;
            if (endsInitTime.isBefore(nextHalfHour)) { // if ends time oversteps midnight and starts time don't, update ends date to next day
                endsDate = initDate.plusDays(1);
            } else {
                endsDate = initDate;
            }
            LocalDate alertDate;
            if (nextHalfHour.isBefore(alertInitTime)) {
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

        addHandlers();
    }

    private void addHandlers() {
        // add handlers to increment decrement buttons of time text fields
        final int size = 3;  // constant size of arrays
        Button[] incrementButtons = {startsIncrement, endsIncrement, alertsIncrement};
        Button[] decrementButtons = {startsDecrement, endsDecrement, alertsDecrement};
        TextField[] textFields = {startsField, endsField, alertField};

        for (int i = 0; i < size; i++) {
            TextField textField = textFields[i];
            incrementButtons[i].setOnAction(e -> incrementTimeTextField(textField, 30));
            decrementButtons[i].setOnAction(e -> decrementTimeTextField(textField, 30));
        }

        // add handlers to buttons
        saveButton.setOnAction(e -> save(e));
        deleteButton.setOnAction(e -> delete(e));
    }

    private void initializeTypeSelector() {
        typeSelector.getItems().add(Event.Type.FREE_TIME.toString());
        typeSelector.getItems().add(Event.Type.WORK.toString());
        typeSelector.getItems().add(Event.Type.SCHOOL.toString());
        typeSelector.getItems().add(Event.Type.OTHERS.toString());

        typeSelector.setValue(Event.Type.FREE_TIME.toString());
    }

    /**
     * If event is already created, event detail is shown.
     */
    public void showDetail() {
        event = eventsClient.getEvent(idUser, idEvent, resourceBundle);

        if (event != null) {
            titleField.setText(event.getTitle());
            locationField.setText(event.getLocation());
            typeSelector.setValue(event.getType().toString());
            startsDateField.setValue(event.getDate());
            startsField.setText(String.valueOf(event.getStarts()));
            endsDateField.setValue(event.getEndsDate());
            endsField.setText(String.valueOf(event.getEnds()));
            alertDateField.setValue(event.getAlertDate());
            alertField.setText(String.valueOf(event.getAlert()));
            descriptionField.setText(event.getDescription());
        }
    }

    /**
     * Method for calculate init starts time and is used by increment buttons as well.
     * Currently it calculates nearest half hour from actual time e.g 14:00, 18:30 etc.
     *
     * @param actualTime time, from which is method calculate next unit
     * @param unit       currently used with value 30 - half hour
     * @return
     */
    public LocalTime countNextHourUnit(LocalTime actualTime, int unit) {
        int minutes = actualTime.getMinute();
        LocalTime nextHalfHour = actualTime.plusMinutes(unit - minutes % unit);

        return nextHalfHour;
    }

    /**
     * Similar method as countNextHourUnit, only it calculates previous hour unit
     *
     * @param actualTime
     * @param unit
     * @return
     */
    public LocalTime countPreviousHourUnit(LocalTime actualTime, int unit) {
        int minutes = actualTime.getMinute();
        LocalTime previousHalfHour = actualTime.minusMinutes(minutes % unit);

        return previousHalfHour;
    }


    /**
     * Functionality of button for incrementing time. Currently increase of half an hour.
     *
     * @param minutes   current minute increment - 30
     * @param textField TextField where time should be incremented
     */
    public void incrementTimeTextField(TextField textField, int minutes) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime actualTime = null;
        try {
            actualTime = LocalTime.parse(textField.getText());
        } catch (DateTimeException dte) {
            actualTime = LocalTime.of(0, 0);
        }
        if (actualTime.getMinute() % 30 != 0) {
            LocalTime nextHalfHour = countNextHourUnit(actualTime, 30);
            textField.setText(nextHalfHour.format(dtf));
        } else {
            LocalTime time = actualTime;
            time = time.plusMinutes(minutes);
            textField.setText(time.format(dtf));
        }
    }

    /**
     * Functionality of button for decrementing time. Currently decrease of half an hour.
     *
     * @param minutes   current minute decrement - 30
     * @param textField TextField where time should be decremented
     */
    public void decrementTimeTextField(TextField textField, int minutes) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime actualTime = null;
        try {
            actualTime = LocalTime.parse(textField.getText());
        } catch (DateTimeException dte) {
            actualTime = LocalTime.of(0, 0);
        }
        if (actualTime.getMinute() % 30 != 0) {
            LocalTime previousHalfHour = countPreviousHourUnit(actualTime, 30);
            textField.setText(previousHalfHour.format(dtf));
        } else {
            LocalTime time = actualTime;
            time = time.minusMinutes(minutes);
            textField.setText(time.format(dtf));
        }
    }

    /**
     * After entering all items, user confirms updating existing/inserting new event.
     * This method get and validate all entered items.
     * If not all required values are entered or values are in wrong format, the user is notified
     */
    public void save(ActionEvent ev) {
        hideErrorLabels();

        String title = titleField.getText();
        String location = locationField.getText();
        Event.Type type = Event.Type.fromString(typeSelector.getValue());
        LocalDate date = startsDateField.getValue();
        LocalTime starts = null;
        LocalDate endsDate = endsDateField.getValue();
        LocalTime ends = null;
        LocalDate alertDate = alertDateField.getValue();
        LocalTime alert = null;
        String description = descriptionField.getText();

        boolean checkFlag = true;
        if (title.equals("")) {
            titleError.setVisible(true);
            checkFlag = false;
        }

        try {
            starts = LocalTime.parse(startsField.getText());
        } catch (DateTimeException dte) {
            startsError.setVisible(true);
            checkFlag = false;
        }

        try {
            ends = LocalTime.parse(endsField.getText());
        } catch (DateTimeException dte) {
            endsError.setVisible(true);
            checkFlag = false;
        }

        try {
            alert = LocalTime.parse(alertField.getText());
        } catch (DateTimeException dte) {
            alertsError.setVisible(true);
            checkFlag = false;
        }

        if (checkFlag) {
            if (idEvent == null) {
                addEvent(ev, title, location, type, description, date, starts, endsDate, ends, alertDate, alert);
            } else {
                updateEvent(ev, title, location, type, description, date, starts, endsDate, ends, alertDate, alert);
            }
        }
    }


    /**
     * Adding new event. (When event does not have ID yet.)
     * After succesful insert modal window is closed and calendar is displayed with just cretaed event
     */
    public void addEvent(ActionEvent ev, String title, String location, Event.Type type, String description, LocalDate date,
                         LocalTime starts, LocalDate endsDate, LocalTime ends, LocalDate alertDate, LocalTime alert) {

        Event event = new Event(title, location, type, description, date, starts, endsDate, ends, alertDate, alert, idUser);
        Integer id = eventsClient.addEvent(event, resourceBundle);
        if (id != null) {
            updateCalendarDisplay(date);
            Stage stage = (Stage) ((Node) ev.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    /**
     * Updating existing event. (When ID of the event already exists.)
     * After succesful update modal window is closed and calendar is displayed with just cretaed event
     */
    public void updateEvent(ActionEvent ev, String title, String location, Event.Type type, String description, LocalDate date,
                            LocalTime starts, LocalDate endsDate, LocalTime ends, LocalDate alertDate, LocalTime alert) {

        Event event = new Event(title, location, type, description, date, starts, endsDate, ends, alertDate, alert, idUser);
        boolean success = eventsClient.updateEvent(event, idEvent, resourceBundle);
        if (success) {
            //updateCalendarDisplay(date);
            planItMainWindowController.updateOrCreateEventInCalendar(idEvent, event.getDate(), this.event.getDate());
            Stage stage = (Stage) ((Node) ev.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    /**
     * After entering new event or updating existing event, main calendar updates its display.
     */
    public void updateCalendarDisplay(LocalDate date) {
        planItMainWindowController.setSelectedYear(date.getYear());
        planItMainWindowController.setSelectedMonth(date.getMonth().getValue());
        planItMainWindowController.initializeCalendar();
        planItMainWindowController.showEventsInCalendar();
        planItMainWindowController.addWeatherToCalendar();
    }

    /**
     * At the beginning of saving event, all error labels are hidden.
     */
    public void hideErrorLabels() {
        titleError.setVisible(false);
        startsError.setVisible(false);
        endsError.setVisible(false);
        alertsError.setVisible(false);
    }

    /**
     * The functionality of the delete button.
     */
    private void delete(ActionEvent ev) {
        if (idEvent != null && event != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(resourceBundle.getString("deleteConfirmationTitle"));
            alert.setHeaderText(resourceBundle.getString("deleteConfirmationHeaderText") + event.getTitle());
            alert.setContentText(resourceBundle.getString("deleteConfirmationQuestion"));

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                boolean success = eventsClient.deleteEvent(idUser, idEvent, resourceBundle);
                if (success) {
                    planItMainWindowController.deleteEventFromCalendar(event.getIdEvent(), event.getDate());
                    Stage stage = (Stage) ((Node) ev.getSource()).getScene().getWindow();
                    stage.close();
                }
            } else {
                alert.close();
            }
        }
    }
}
