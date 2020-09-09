package com.example.gui.controllers;

import com.example.client.clients.EventsClient;
import com.example.client.model.Event;
import com.example.client.model.repetition.RepetitionType;
import com.example.gui.components.DailyRepetitionComponent;
import com.example.gui.components.MonthlyRepetitionComponent;
import com.example.gui.components.WeeklyRepetitionComponent;
import com.example.gui.data_items.EventTypeItem;
import com.example.gui.data_items.RepetitionTypeItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Controller for "PlanItAddEvent.fxml"
 */
public class PlanItAddEventController implements Initializable {
    private final Integer ERROR_HBOX_WIDTH = 476;
    private final Integer ERROR_HBOX_HEIGTH = 30;
    private final Integer ERROR_FIRST_HBOX_WIDTH = 215;
    private final Integer DATE_ERROR_FIRST_HBOX_WIDTH = 445;

    private final Integer idUser;
    private final EventsClient eventsClient;
    private final PlanItMainWindowController planItMainWindowController;

    private final List<HBox> errorBoxes = new ArrayList<HBox>();

    @FXML
    private AnchorPane ap;

    @FXML
    private VBox detailBox;

    @FXML
    private HBox titleRow;

    @FXML
    private HBox startsRow;

    @FXML
    private HBox endsRow;

    @FXML
    private HBox alertRow;

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
    private ChoiceBox<EventTypeItem> typeSelector;

    @FXML
    private DatePicker startsDateField;

    @FXML
    private DatePicker endsDateField;

    @FXML
    private DatePicker alertDateField;

    @FXML
    private VBox repetitionBox;

    @FXML
    private ChoiceBox<RepetitionTypeItem> repetitionTypeSelector;

    private DailyRepetitionComponent dailyRepetitionComponent;
    private VBox weeklyRepetitionComponent;
    private VBox monthlyRepetitionComponent;
    private VBox yearlyRepetitionComponent;

    @FXML
    private Button repetitionButton;

    private LocalDate initDate;
    private ResourceBundle resourceBundle;
    private Event event;
    private long repetitionButtonClickCount = 0;

    public PlanItAddEventController(int idUser, LocalDate initDate, EventsClient eventsClient, PlanItMainWindowController planItMainWindowController) {
        this.idUser = idUser;
        this.initDate = initDate;
        this.eventsClient = eventsClient;
        this.planItMainWindowController = planItMainWindowController;
    }

    public PlanItAddEventController(int idUser, Event event, EventsClient eventsClient, PlanItMainWindowController planItMainWindowController) {
        this.idUser = idUser;
        this.event = event;
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

        dailyRepetitionComponent = new DailyRepetitionComponent(resourceBundle);
        weeklyRepetitionComponent = new WeeklyRepetitionComponent(resourceBundle);
        monthlyRepetitionComponent = new MonthlyRepetitionComponent(resourceBundle, initDate);

        repetitionBox.setVisible(false);

        initializeTypeSelector();
        initializeRepetitionTypeSelector();

        if (event == null) { // add event
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
            if(event.getRepetition() != null) {
                repetitionButton.setText("Edit repetition");
            }
            showDetail();
        }

        addHandlers();
    }

    private void initializeTypeSelector() {
        for(Event.Type type : Event.Type.values()) {
            typeSelector.getItems().add(new EventTypeItem(type, resourceBundle));
        }

        typeSelector.getSelectionModel().selectFirst();
    }

    private void initializeRepetitionTypeSelector() {
        for(RepetitionType repetitionType : RepetitionType.values()) {
            repetitionTypeSelector.getItems().add(new RepetitionTypeItem(repetitionType, resourceBundle));
        }

        repetitionTypeSelector.getSelectionModel().selectFirst();
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
        repetitionButton.setOnAction(e -> repetitionButtonClickedHandler());
        saveButton.setOnAction(e -> save(e));
        deleteButton.setOnAction(e -> delete(e));
    }

    private void repetitionButtonClickedHandler() {
        repetitionBox.setVisible(!repetitionBox.isVisible());

        if(repetitionButtonClickCount == 0) {
            if(event != null && event.getRepetition() != null) {
                showRepetitionDetail();
            } else {
//                repetitionBox.getChildren().add(2, dailyRepetitionComponent);
//                repetitionBox.getChildren().add(2, weeklyRepetitionComponent);
                repetitionBox.getChildren().add(2, monthlyRepetitionComponent);
            }
        }

        repetitionButtonClickCount++;
    }

    private void showRepetitionDetail() {
        // TODO
    }

    /**
     * If event is already created, event detail is shown.
     */
    public void showDetail() {
        titleField.setText(event.getTitle());
        locationField.setText(event.getLocation());
        typeSelector.getSelectionModel().select(getEventTypeItem(event.getType()));
        startsDateField.setValue(event.getDate());
        startsField.setText(String.valueOf(event.getStarts()));
        endsDateField.setValue(event.getEndsDate());
        endsField.setText(String.valueOf(event.getEnds()));
        alertDateField.setValue(event.getAlertDate());
        alertField.setText(String.valueOf(event.getAlert()));
        descriptionField.setText(event.getDescription());
    }

    private EventTypeItem getEventTypeItem(Event.Type type) {
        for(EventTypeItem eventTypeItem : typeSelector.getItems()) {
            if(eventTypeItem.getType().equals(type)) {
                return eventTypeItem;
            }
        }

        return null;
    }

    private RepetitionTypeItem getRepetitionTypeItem(Event.Type type) {
        for(RepetitionTypeItem repetitionTypeItem : repetitionTypeSelector.getItems()) {
            if(repetitionTypeItem.getType().equals(type)) {
                return repetitionTypeItem;
            }
        }

        return null;
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
        Event.Type type = typeSelector.getValue().getType(); // TODO fix this to be multilanguage
        LocalDate date = startsDateField.getValue();
        LocalTime starts = null;
        LocalDate endsDate = endsDateField.getValue();
        LocalTime ends = null;
        LocalDate alertDate = alertDateField.getValue();
        LocalTime alert = null;
        String description = descriptionField.getText();

        boolean checkFlag = validateTitle(title);
        HBox timeErrorHBox = null;
        try {
            starts = LocalTime.parse(startsField.getText());
        } catch (DateTimeException dte) {
            timeErrorHBox = createErrorHBox("timeErrorLabel", ERROR_FIRST_HBOX_WIDTH);
            int index = detailBox.getChildren().indexOf(startsRow);
            detailBox.getChildren().add(index+1, timeErrorHBox);
            checkFlag = false;
        }

        if(!validateDate(date, timeErrorHBox)) {
            checkFlag = false;
        }

        timeErrorHBox = null;
        try {
            ends = LocalTime.parse(endsField.getText());
        } catch (DateTimeException dte) {
            timeErrorHBox = createErrorHBox("timeErrorLabel", ERROR_FIRST_HBOX_WIDTH);
            int index = detailBox.getChildren().indexOf(endsRow);
            detailBox.getChildren().add(index+1, timeErrorHBox);
            checkFlag = false;
        }

        if(!validateDate(endsDate, timeErrorHBox)) {
            checkFlag = false;
        }

        timeErrorHBox = null;
        try {
            alert = LocalTime.parse(alertField.getText());
        } catch (DateTimeException dte) {
            timeErrorHBox = createErrorHBox("timeErrorLabel", ERROR_FIRST_HBOX_WIDTH);
            int index = detailBox.getChildren().indexOf(alertRow);
            detailBox.getChildren().add(index+1, timeErrorHBox);
            checkFlag = false;
        }

        if(!validateDate(alertDate, timeErrorHBox)) {
            checkFlag = false;
        }

        if (checkFlag) {
            if (event == null) {
                addEvent(ev, title, location, type, description, date, starts, endsDate, ends, alertDate, alert);
            } else {
                updateEvent(ev, title, location, type, description, date, starts, endsDate, ends, alertDate, alert);
            }
        }
    }

    private boolean validateTitle(String title) {
        if (title.equals("")) {
            HBox titleErrorHBox = createErrorHBox("titleErrorLabel", ERROR_FIRST_HBOX_WIDTH);
            int index = detailBox.getChildren().indexOf(titleRow);
            detailBox.getChildren().add(index+1, titleErrorHBox);
            return false;
        }

        return true;
    }

    private boolean validateDate(LocalDate date, HBox errorHBox) {
        if(date == null) {
            if(errorHBox == null) {
                errorHBox = createErrorHBox("dateErrorLabel", DATE_ERROR_FIRST_HBOX_WIDTH);
                int index = detailBox.getChildren().indexOf(startsRow);
                detailBox.getChildren().add(index + 1, errorHBox);
            } else {
                Label dateErrorLabel = new Label(resourceBundle.getString("dateErrorLabel"));
                dateErrorLabel.getStyleClass().add("error-label");
                errorHBox.getChildren().add(dateErrorLabel);
            }
           return false;
        }

        return true;
    }



    /**
     * Adding new event. (When event does not have ID yet.)
     * After succesful insert modal window is closed and calendar is displayed with just cretaed event
     */
    public void addEvent(ActionEvent ev, String title, String location, Event.Type type, String description, LocalDate date,
                         LocalTime starts, LocalDate endsDate, LocalTime ends, LocalDate alertDate, LocalTime alert) {

        Event event = new Event(title, location, type, description, date, starts, endsDate, ends, alertDate, alert, idUser);
        Long id = eventsClient.addEvent(event, resourceBundle);

        if (id != null) {
            if (newDateIsInCalendarDisplay(date)) {
                planItMainWindowController.createEventInCalendar(date);
            } else {
                updateCalendarDisplay(date);
            }

            Stage stage = (Stage) ((Node) ev.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    /**
     * Updating existing event. (When ID of the event already exists.)
     * After successful update modal window is closed and calendar is displayed with just created event
     */
    public void updateEvent(ActionEvent ev, String title, String location, Event.Type type, String description, LocalDate date,
                            LocalTime starts, LocalDate endsDate, LocalTime ends, LocalDate alertDate, LocalTime alert) {

        Event event = new Event(title, location, type, description, date, starts, endsDate, ends, alertDate, alert, idUser);
        boolean success = eventsClient.updateEvent(event, this.event.getIdEvent(), resourceBundle);

        if (!success) {
            return;
        }

        if (!newEventLabelIsEqualWithOld(event)) {
            if (newDateIsInCalendarDisplay(date)) {
                planItMainWindowController.updateEventInCalendar(event, this.event.getDate(), this.event.getStarts());
            } else {
                updateCalendarDisplay(date);
            }
        }

        Stage stage = (Stage) ((Node) ev.getSource()).getScene().getWindow();
        stage.close();
    }

    private boolean newEventLabelIsEqualWithOld(Event updatedEvent) {
        return updatedEvent.getDate().equals(this.event.getDate()) && updatedEvent.getStarts().equals(this.event.getStarts()) &&
                updatedEvent.getTitle().equals(this.event.getTitle());
    }

    private boolean newDateIsInCalendarDisplay(LocalDate newDate) {
        int newYear = newDate.getYear();
        int newMonth = newDate.getMonthValue();

        return newYear == planItMainWindowController.getSelectedYear() &&
                newMonth == planItMainWindowController.getSelectedMonth();
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
        detailBox.getChildren().removeAll(errorBoxes);
    }

    /**
     * The functionality of the delete button.
     */
    private void delete(ActionEvent ev) {
        if (event != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(resourceBundle.getString("deleteConfirmationTitle"));
            alert.setHeaderText(resourceBundle.getString("deleteConfirmationHeaderText") + event.getTitle());
            alert.setContentText(resourceBundle.getString("deleteConfirmationQuestion"));

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                boolean success = eventsClient.deleteEvent(idUser, this.event.getIdEvent(), resourceBundle);
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

    private HBox createErrorHBox(String label, double width) {
        HBox errorHBox = new HBox();
        errorHBox.setPrefSize(ERROR_HBOX_WIDTH, ERROR_HBOX_HEIGTH);

        HBox firstHBox = new HBox();
        firstHBox.setPrefWidth(width);

        HBox secondHBox = new HBox();
        secondHBox.setPrefWidth(DATE_ERROR_FIRST_HBOX_WIDTH - ERROR_FIRST_HBOX_WIDTH);

        Label errorLabel = new Label(resourceBundle.getString(label));
        errorLabel.getStyleClass().add("error-label");
        secondHBox.getChildren().add(errorLabel);

        errorHBox.getChildren().addAll(firstHBox, secondHBox);
        errorBoxes.add(errorHBox);

        return errorHBox;
    }
}
