package com.example.gui.controllers;

import com.example.client.clients.EventsClient;
import com.example.client.model.Event;
import com.example.client.model.repetition.*;
import com.example.gui.components.DailyRepetitionComponent;
import com.example.gui.components.MonthlyRepetitionComponent;
import com.example.gui.components.WeeklyRepetitionComponent;
import com.example.gui.components.YearlyRepetitionComponent;
import com.example.gui.data_items.EventTypeItem;
import com.example.gui.data_items.RepetitionTypeItem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
import java.util.List;

/**
 * Controller for "PlanItAddEvent.fxml"
 */
public class PlanItAddEventController implements Initializable {
    private final Integer ERROR_HBOX_WIDTH = 476;
    private final Integer ERROR_HBOX_HEIGTH = 30;
    private final Integer ERROR_FIRST_HBOX_WIDTH = 215;

    private final Integer idUser;
    private final EventsClient eventsClient;
    private final PlanItMainWindowController planItMainWindowController;

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
    private DatePicker repetitionStartField;

    @FXML
    private DatePicker repetitionEndField;

    @FXML
    private ChoiceBox<RepetitionTypeItem> repetitionTypeSelector;

    @FXML
    private Button repetitionButton;

    private boolean repetitionButtonClicked = false;

    // error fields
    private HBox titleErrorField = new HBox();
    private HBox startErrorField = new HBox();
    private HBox endErrorField = new HBox();
    private HBox alertErrorField = new HBox();
    private HBox repetitionStartErrorField = new HBox();
    private HBox repetitionEndErrorField = new HBox();

    // repetition components
    private DailyRepetitionComponent selectedRepetitionComponent;
    private DailyRepetitionComponent dailyRepetitionComponent;
    private WeeklyRepetitionComponent weeklyRepetitionComponent;
    private MonthlyRepetitionComponent monthlyRepetitionComponent;
    private YearlyRepetitionComponent yearlyRepetitionComponent;

    private LocalDate initDate;
    private ResourceBundle resourceBundle;
    private Event event;

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

        repetitionBox.setVisible(false);

        initializeEventTypeSelector();
        initializeRepetitionTypeSelector();

        initErrorFields();
        addListenersOnFields();

        if (event == null) { // add event
            setInitValues();
        } else { // show detail of already created event
            if (event.getRepetition() != null) {
                repetitionButton.setText(resourceBundle.getString("editRepetitionButton"));
            }

            showDetail();
        }

        addHandlers();
    }

    private void setInitValues() {
        setTimeAndDateValues();
        typeSelector.getSelectionModel().selectFirst();
    }

    private void setRepetitionComponentsInitValues(DailyRepetitionComponent... components) {
        for (DailyRepetitionComponent component : components) {
            component.setInitValues(initDate);
        }
    }

    private void setTimeAndDateValues() {
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
    }

    private void addListenersOnFields() {
        titleField.textProperty().addListener((observable, oldValue, newValue) -> {
            if ("".equals(newValue)) {
                createErrorMessage(titleRow, titleErrorField, "titleErrorLabel");
            } else {
                removeErrorMessage(titleErrorField);
            }
        });

        // TODO refactoring - extract to method
        startsField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                LocalTime.parse(newValue);
                removeErrorMessage(startErrorField);
            } catch (DateTimeException e) {
                createErrorMessage(startsRow, startErrorField, "timeErrorLabel");
            }
        });

        endsField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                LocalTime.parse(newValue);
                removeErrorMessage(endErrorField);
            } catch (DateTimeException e) {
                createErrorMessage(endsRow, endErrorField, "timeErrorLabel");
            }
        });

        alertField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                LocalTime.parse(newValue);
                removeErrorMessage(alertErrorField);
            } catch (DateTimeException e) {
                createErrorMessage(alertRow, alertErrorField, "timeErrorLabel");
            }
        });

//        startsDateField.valueProperty().addListener((observable, oldValue, newValue) -> {
//            repetitionStartField.setValue(newValue);
//        });
    }

    private void removeErrorMessage(HBox errorField) {
        errorField.getChildren().clear();
        detailBox.getChildren().remove(errorField);
    }

    private void initializeEventTypeSelector() {
        for (Event.Type type : Event.Type.values()) {
            typeSelector.getItems().add(new EventTypeItem(type, resourceBundle));
        }
    }

    private void initializeRepetitionTypeSelector() {
        for (RepetitionType repetitionType : RepetitionType.values()) {
            repetitionTypeSelector.getItems().add(new RepetitionTypeItem(repetitionType, resourceBundle));
        }

        repetitionTypeSelector.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            RepetitionType newType = newValue.getType();
            if (repetitionBox.getChildren().size() > 3) {
                repetitionBox.getChildren().remove(2);
            }

            switch (newType) {
                case DAILY:
                    repetitionBox.getChildren().add(2, dailyRepetitionComponent);
                    selectedRepetitionComponent = dailyRepetitionComponent;
                    break;
                case WEEKLY:
                    repetitionBox.getChildren().add(2, weeklyRepetitionComponent);
                    selectedRepetitionComponent = weeklyRepetitionComponent;
                    break;
                case MONTHLY:
                    repetitionBox.getChildren().add(2, monthlyRepetitionComponent);
                    selectedRepetitionComponent = monthlyRepetitionComponent;
                    break;
                case YEARLY:
                    repetitionBox.getChildren().add(2, yearlyRepetitionComponent);
                    selectedRepetitionComponent = yearlyRepetitionComponent;
                    break;
            }
        });
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
        saveButton.setOnAction(e -> save());
        deleteButton.setOnAction(e -> delete());
    }

    private void repetitionButtonClickedHandler() {
        // initialization of repetition fields after first click on button
        if (!repetitionButtonClicked) {
            dailyRepetitionComponent = new DailyRepetitionComponent(resourceBundle);
            weeklyRepetitionComponent = new WeeklyRepetitionComponent(resourceBundle);
            monthlyRepetitionComponent = new MonthlyRepetitionComponent(resourceBundle);
            yearlyRepetitionComponent = new YearlyRepetitionComponent(resourceBundle);

            if (event != null && event.getRepetition() != null) {
                showRepetitionDetail(event.getRepetition());
            } else {
                initDate = startsDateField.getValue();
                setRepetitionComponentsInitValues(dailyRepetitionComponent, weeklyRepetitionComponent, monthlyRepetitionComponent,
                        yearlyRepetitionComponent);
                repetitionTypeSelector.getSelectionModel().select(1);
            }

            repetitionButtonClicked = true;
        }

        if (!repetitionBox.isVisible()) {
            if (event != null && event.getRepetition() != null && !event.getDate().equals(startsDateField.getValue())) {
                startsDateField.setValue(event.getDate());
            }

            startsDateField.setDisable(true);
            saveButton.setText(resourceBundle.getString("saveAllButton"));
            deleteButton.setText(resourceBundle.getString("deleteAllButton"));
        } else {
            startsDateField.setDisable(false);
            saveButton.setText(resourceBundle.getString("saveButton"));
            deleteButton.setText(resourceBundle.getString("deleteButton"));
        }

        repetitionBox.setVisible(!repetitionBox.isVisible());
    }

    private void showRepetitionDetail(Repetition repetition) {
        repetitionStartField.setValue(repetition.getStart());
        repetitionEndField.setValue(repetition.getEnd());

        if (repetition instanceof YearlyRepetition) {
            repetitionTypeSelector.setValue(repetitionTypeSelector.getItems().get(3));
            yearlyRepetitionComponent.showRepetitionDetail(event.getRepetition());
            setRepetitionComponentsInitValues(dailyRepetitionComponent, weeklyRepetitionComponent, monthlyRepetitionComponent);
        } else if (repetition instanceof MonthlyRepetition) {
            repetitionTypeSelector.setValue(repetitionTypeSelector.getItems().get(2));
            monthlyRepetitionComponent.showRepetitionDetail(event.getRepetition());
            setRepetitionComponentsInitValues(dailyRepetitionComponent, weeklyRepetitionComponent, yearlyRepetitionComponent);
        } else if (repetition instanceof WeeklyRepetition) {
            repetitionTypeSelector.setValue(repetitionTypeSelector.getItems().get(1));
            weeklyRepetitionComponent.showRepetitionDetail(event.getRepetition());
            setRepetitionComponentsInitValues(dailyRepetitionComponent, monthlyRepetitionComponent, yearlyRepetitionComponent);
        } else {
            repetitionTypeSelector.setValue(repetitionTypeSelector.getItems().get(0));
            dailyRepetitionComponent.showRepetitionDetail(event.getRepetition());
            setRepetitionComponentsInitValues(weeklyRepetitionComponent, monthlyRepetitionComponent, yearlyRepetitionComponent);
        }
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
        for (EventTypeItem eventTypeItem : typeSelector.getItems()) {
            if (eventTypeItem.getType().equals(type)) {
                return eventTypeItem;
            }
        }

        return null;
    }

    private RepetitionTypeItem getRepetitionTypeItem(Event.Type type) {
        for (RepetitionTypeItem repetitionTypeItem : repetitionTypeSelector.getItems()) {
            if (repetitionTypeItem.getType().equals(type)) {
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
    public void save() {
        if (checkInput()) {
            Event event = readEventInput();
            if (repetitionBox.isVisible()) { // save repetition too
                Repetition repetition = selectedRepetitionComponent.readInput();

                if (repetition != null) {
                    LocalDate repetitionStart = repetitionStartField.getValue();
                    LocalDate repetitionEnd = repetitionEndField.getValue();

                    repetition.setStart(repetitionStart);
                    repetition.setEnd(repetitionEnd);

                    event.setRepetition(repetition);
                } else {
                    return;
                }
            }

            if (this.event == null) {
                addEvent(event);
            } else {
                updateEvent(event);
            }
        }
    }

    private Event readEventInput() {
        String title = titleField.getText();
        String location = locationField.getText();
        Event.Type type = typeSelector.getValue().getType();
        LocalDate date = startsDateField.getValue();
        LocalTime starts = LocalTime.parse(startsField.getText());
        LocalDate endsDate = endsDateField.getValue();
        LocalTime ends = LocalTime.parse(endsField.getText());
        LocalDate alertDate = alertDateField.getValue();
        LocalTime alert = LocalTime.parse(alertField.getText());
        String description = descriptionField.getText();

       return new Event(title, location, type, description, date, starts, endsDate, ends, alertDate, alert, idUser);
    }

    private boolean checkInput() {
        if ("".equals(titleField.getText())) {
            createErrorMessage(titleRow, titleErrorField, "titleErrorLabel");
            return false;
        }

        if (!startErrorField.getChildren().isEmpty()) {
            return false;
        }

        if (!endErrorField.getChildren().isEmpty()) {
            return false;
        }

        if (!alertErrorField.getChildren().isEmpty()) {
            return false;
        }

        return true;
    }

    /**
     * Adding new event. (When event does not have ID yet.)
     * After succesful insert modal window is closed and calendar is displayed with just cretaed event
     */
    public void addEvent(Event event) {
        Long id = eventsClient.addEvent(event, resourceBundle);

        if (id != null) {
            if (newDateIsInCalendarDisplay(event.getDate())) {
                planItMainWindowController.createEventInCalendar(event.getDate());
            } else {
                updateCalendarDisplay(event.getDate());
            }

            Stage stage = (Stage) ap.getScene().getWindow();
            stage.close();
        }
    }

    /**
     * Updating existing event. (When ID of the event already exists.)
     * After successful update modal window is closed and calendar is displayed with just created event
     */
    public void updateEvent(Event event) {
        boolean success ;
        if(event.getRepetition() == null) {
            success = eventsClient.updateEvent(event, idUser, this.event.getIdEvent(), resourceBundle);
        } else {
            // TODO call api endpoint that updates repetition too
            success = true;
        }

        if (!success) {
            return;
        }

        if (!newEventLabelIsEqualWithOld(event)) {
            if (newDateIsInCalendarDisplay(event.getDate())) {
                event.setIdEvent(this.event.getIdEvent());
                planItMainWindowController.updateEventInCalendar(event, this.event.getDate(), this.event.getStarts());
            } else {
                updateCalendarDisplay(event.getDate());
            }
        }

        Stage stage = (Stage) ap.getScene().getWindow();
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
     * The functionality of the delete button.
     */
    private void delete() {
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
                    Stage stage = (Stage) ap.getScene().getWindow();
                    stage.close();
                }
            } else {
                alert.close();
            }
        }
    }

    private void createErrorMessage(HBox field, HBox errorField, String label) {
        if (errorField.getChildren().isEmpty()) {
            Label errorLabel = new Label(resourceBundle.getString(label));
            errorLabel.getStyleClass().add("error-label");

            HBox.setMargin(errorLabel, new Insets(0, 0, 0, ERROR_FIRST_HBOX_WIDTH));
            errorField.getChildren().add(errorLabel);

            int index = detailBox.getChildren().indexOf(field);
            detailBox.getChildren().add(index + 1, errorField);
        }
    }

    private void initErrorFields() {
        titleErrorField.setPrefSize(ERROR_HBOX_WIDTH, ERROR_HBOX_HEIGTH);
        startErrorField.setPrefSize(ERROR_HBOX_WIDTH, ERROR_HBOX_HEIGTH);
        endErrorField.setPrefSize(ERROR_HBOX_WIDTH, ERROR_HBOX_HEIGTH);
        alertErrorField.setPrefSize(ERROR_HBOX_WIDTH, ERROR_HBOX_HEIGTH);
        repetitionStartErrorField.setPrefSize(ERROR_HBOX_WIDTH, ERROR_HBOX_HEIGTH);
        repetitionEndErrorField.setPrefSize(ERROR_HBOX_WIDTH, ERROR_HBOX_HEIGTH);
    }
}
