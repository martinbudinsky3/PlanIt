package com.example.gui.controllers;

import com.example.client.clients.EventsClient;
import com.example.client.exceptions.AccessDeniedException;
import com.example.client.exceptions.NotFoundException;
import com.example.client.exceptions.UnauthorizedException;
import com.example.model.Event;
import com.example.model.EventType;
import com.example.model.repetition.*;
import com.example.gui.components.DailyRepetitionComponent;
import com.example.gui.components.MonthlyRepetitionComponent;
import com.example.gui.components.WeeklyRepetitionComponent;
import com.example.gui.components.YearlyRepetitionComponent;
import com.example.gui.dataitems.EventTypeItem;
import com.example.gui.dataitems.RepetitionTypeItem;
import com.example.utils.WindowsCreator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Minutes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Controller for "PlanItAddEvent.fxml"
 */
public class PlanItAddEventController implements Initializable {
    private final Integer ERROR_HBOX_WIDTH = 476;
    private final Integer ERROR_HBOX_HEIGTH = 30;
    private final Integer ERROR_FIRST_HBOX_WIDTH = 215;

    static final Logger logger = LoggerFactory.getLogger(PlanItAddEventController.class);
    private final EventsClient eventsClient;
    private final PlanItMainWindowController planItMainWindowController;
    private final WindowsCreator windowsCreator = new WindowsCreator();

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
    private HBox eventButtonsWhenRepetitionHidden;

    @FXML
    private HBox eventButtonsWhenRepetitionShown;

    @FXML
    private HBox repetitionButtonsWhenRepetitionHidden;

    @FXML
    private HBox repetitionButtonsWhenRepetitionShown;

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
    private Button saveAllButton;

    @FXML
    private Button deleteAllButton;

    @FXML
    private Button hideRepetitionButton;

    @FXML
    private Button saveRepetitionButton;

    @FXML
    private Button repetitionButton;

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

    private LocalDate initRepetitionDate;
    private LocalDate initDate;
    private ResourceBundle resourceBundle;
    private Event event;

    public PlanItAddEventController(LocalDate initDate, EventsClient eventsClient, PlanItMainWindowController planItMainWindowController) {
        this.initRepetitionDate = initDate;
        this.initDate = initDate;
        this.eventsClient = eventsClient;
        this.planItMainWindowController = planItMainWindowController;
    }

    public PlanItAddEventController(Event event, EventsClient eventsClient, PlanItMainWindowController planItMainWindowController) {
        this.event = event;
        this.initDate = event.getStartDate();
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
            saveRepetitionButton.setVisible(false);
        } else { // show detail of already created event
            if (event.getRepetition() != null) {
                initRepetitionDate = event.getRepetition().getStart();
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
            component.setInitValues(initRepetitionDate);
        }
    }

    private void setTimeAndDateValues() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime nextHalfHour = countNextHourUnit(LocalTime.now(), 30);
        LocalTime endsInitTime = nextHalfHour.plusMinutes(30);
        LocalTime alertInitTime = nextHalfHour.minusMinutes(15);

        LocalDate endsDate;
        if (endsInitTime.isBefore(nextHalfHour)) { // if ends time oversteps midnight and starts time don't, update ends date to next day
            endsDate = initRepetitionDate.plusDays(1);
        } else {
            endsDate = initRepetitionDate;
        }
        LocalDate alertDate;
        if (nextHalfHour.isBefore(alertInitTime)) {
            alertDate = initRepetitionDate.minusDays(1);
        } else {
            alertDate = initRepetitionDate;
        }

        startsField.setText(nextHalfHour.format(dtf));
        endsField.setText(endsInitTime.format(dtf));
        alertField.setText(alertInitTime.format(dtf));
        startsDateField.setValue(initRepetitionDate);
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

        startsField.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean valid = validateTime(startsRow, startErrorField, newValue);
            if (valid && startsDateField.getValue() != null && endsDateField.getValue() != null) {
                validateDatesAfterStartTimeChange(newValue, oldValue);
            }
        });

        endsField.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean valid = validateTime(endsRow, endErrorField, newValue);
            if (valid && startsDateField.getValue() != null && endsDateField.getValue() != null) {
                validateDatesAfterEndTimeChange(newValue, oldValue);
            }
        });

        alertField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateTime(alertRow, alertErrorField, newValue);
        });

        startsDateField.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if (endsDateField.getValue() != null) {
                validateDatesAfterStartChange(newValue, oldValue);
            }
        }));

        endsDateField.valueProperty().addListener(((observable, oldValue, newValue) -> {
            validateDatesAfterEndChange(newValue, oldValue);
        }));
    }

    private boolean validateTime(HBox row, HBox errorField, String newValue) {
        try {
            LocalTime.parse(newValue);
            removeErrorMessage(errorField);
            return true;
        } catch (DateTimeException e) {
            createErrorMessage(row, errorField, "timeErrorLabel");
            return false;
        }
    }

    private void validateDatesAfterStartChange(LocalDate newValue, LocalDate oldValue) {
        LocalDate endDate = endsDateField.getValue();
        LocalTime startTime;
        LocalTime endTime;

        try {
            startTime = LocalTime.parse(startsField.getText());
        } catch (DateTimeException e) {
            startTime = LocalTime.of(0, 0);
        }

        try {
            endTime = LocalTime.parse(endsField.getText());
        } catch (DateTimeException e) {
            endTime = LocalTime.of(23, 59);
        }

        LocalDateTime start = LocalDateTime.of(newValue, startTime);
        LocalDateTime end = LocalDateTime.of(endDate, endTime);

        if (!start.isBefore(end)) {
            int dayDiff = getDayDiff(oldValue, newValue);
            endsDateField.setValue(endDate.plusDays(dayDiff));
        }
    }

    private void validateDatesAfterEndChange(LocalDate newValue, LocalDate oldValue) {
        LocalDate startDate = startsDateField.getValue();
        LocalTime startTime;
        LocalTime endTime;

        try {
            startTime = LocalTime.parse(startsField.getText());
        } catch (DateTimeException e) {
            startTime = LocalTime.of(0, 0);
        }

        try {
            endTime = LocalTime.parse(endsField.getText());
        } catch (DateTimeException e) {
            endTime = LocalTime.of(23, 59);
        }

        LocalDateTime start = LocalDateTime.of(startDate, startTime);
        LocalDateTime end = LocalDateTime.of(newValue, endTime);

        if (!start.isBefore(end)) {
            int dayDiff = getDayDiff(oldValue, newValue);
            startsDateField.setValue(startDate.plusDays(dayDiff));
        }
    }

    private void validateDatesAfterStartTimeChange(String newValue, String oldValue) {
        LocalTime startTime = LocalTime.parse(newValue);
        LocalDateTime start = LocalDateTime.of(startsDateField.getValue(), startTime);

        LocalTime oldStartTime = getOldTime(oldValue);

        LocalTime endTime;
        try {
            endTime = LocalTime.parse(endsField.getText());
            LocalDateTime end = LocalDateTime.of(endsDateField.getValue(), endTime);

            if (!start.isBefore(end)) {
                if (oldStartTime != null) {
                    LocalDateTime oldStart = LocalDateTime.of(startsDateField.getValue(), oldStartTime);
                    int diff = getMinuteDiff(oldStart, start);
                    end = end.plusMinutes(diff);
                } else {
                    end = start.plusMinutes(30);
                }

                endsDateField.setValue(end.toLocalDate());
                endsField.setText(end.toLocalTime().toString());
            }

        } catch (DateTimeException e) {
            endTime = startTime.plusMinutes(30);
            LocalDateTime end = LocalDateTime.of(endsDateField.getValue(), endTime);

            if (!start.isBefore(end)) {
                end = end.plusDays(1);
            }

            endsDateField.setValue(end.toLocalDate());
            endsField.setText(endTime.toString());
        }
    }

    private void validateDatesAfterEndTimeChange(String newValue, String oldValue) {
        LocalTime endTime = LocalTime.parse(newValue);
        LocalDateTime end = LocalDateTime.of(endsDateField.getValue(), endTime);

        LocalTime oldEndTime = getOldTime(oldValue);

        LocalTime startTime;
        try {
            startTime = LocalTime.parse(startsField.getText());
            LocalDateTime start = LocalDateTime.of(startsDateField.getValue(), startTime);

            if (!start.isBefore(end)) {
                if (oldEndTime != null) {
                    LocalDateTime oldEnd = LocalDateTime.of(startsDateField.getValue(), oldEndTime);
                    int diff = getMinuteDiff(oldEnd, end);
                    start = start.plusMinutes(diff);
                } else {
                    start = end.minusMinutes(30);
                }

                startsDateField.setValue(start.toLocalDate());
                startsField.setText(start.toLocalTime().toString());
            }

        } catch (DateTimeException e) {
            startTime = endTime.minusMinutes(30);
            LocalDateTime start = LocalDateTime.of(startsDateField.getValue(), startTime);

            if (!start.isBefore(end)) {
                start = start.minusDays(1);
            }

            startsDateField.setValue(start.toLocalDate());
            startsField.setText(startTime.toString());
        }
    }

    private LocalTime getOldTime(String oldValue) {
        try {
            return LocalTime.parse(oldValue);
        } catch (DateTimeException e) {
            return null;
        }
    }

    private void removeErrorMessage(HBox errorField) {
        errorField.getChildren().clear();
        detailBox.getChildren().remove(errorField);
    }

    private void initializeEventTypeSelector() {
        for (EventType type : EventType.values()) {
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
                    repetitionEndField.setValue(repetitionStartField.getValue().plusMonths(3));
                    break;
                case WEEKLY:
                    repetitionBox.getChildren().add(2, weeklyRepetitionComponent);
                    selectedRepetitionComponent = weeklyRepetitionComponent;
                    repetitionEndField.setValue(repetitionStartField.getValue().plusWeeks(24));
                    break;
                case MONTHLY:
                    repetitionBox.getChildren().add(2, monthlyRepetitionComponent);
                    selectedRepetitionComponent = monthlyRepetitionComponent;
                    repetitionEndField.setValue(repetitionStartField.getValue().plusYears(1));
                    break;
                case YEARLY:
                    repetitionBox.getChildren().add(2, yearlyRepetitionComponent);
                    selectedRepetitionComponent = yearlyRepetitionComponent;
                    repetitionEndField.setValue(repetitionStartField.getValue().plusYears(20));
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
        hideRepetitionButton.setOnAction(e -> hideRepetitionButtonClickedHandler());
        saveButton.setOnAction(e -> save());
        saveAllButton.setOnAction(e -> updateAllEventsInRepetition());
        deleteButton.setOnAction(e -> delete());
        deleteAllButton.setOnAction(e -> deleteAll());
        saveRepetitionButton.setOnAction(e -> saveRepetition());
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
                initRepetitionDate = startsDateField.getValue();
                repetitionStartField.setValue(initRepetitionDate);
                setRepetitionComponentsInitValues(dailyRepetitionComponent, weeklyRepetitionComponent, monthlyRepetitionComponent,
                        yearlyRepetitionComponent);
                repetitionTypeSelector.getSelectionModel().select(1);
            }

            repetitionButtonClicked = true;
        }

        if (event != null && event.getRepetition() != null) {
            eventButtonsWhenRepetitionShown.setVisible(true);
            eventButtonsWhenRepetitionHidden.setVisible(false);
        }

        repetitionButtonsWhenRepetitionHidden.setVisible(false);
        repetitionButtonsWhenRepetitionShown.setVisible(true);
        repetitionBox.setVisible(true);
    }

    private void hideRepetitionButtonClickedHandler() {
        eventButtonsWhenRepetitionShown.setVisible(false);
        eventButtonsWhenRepetitionHidden.setVisible(true);
        repetitionButtonsWhenRepetitionShown.setVisible(false);
        repetitionButtonsWhenRepetitionHidden.setVisible(true);
        repetitionBox.setVisible(false);
    }

    private void showRepetitionDetail(Repetition repetition) {
        repetitionStartField.setValue(repetition.getStart());
        repetitionEndField.setValue(repetition.getEnd());

        if (repetition instanceof YearlyRepetition) {
            repetitionTypeSelector.setValue(repetitionTypeSelector.getItems().get(3));
            yearlyRepetitionComponent.showRepetitionDetail((YearlyRepetition) event.getRepetition());
            setRepetitionComponentsInitValues(dailyRepetitionComponent, weeklyRepetitionComponent, monthlyRepetitionComponent);
        } else if (repetition instanceof MonthlyRepetition) {
            repetitionTypeSelector.setValue(repetitionTypeSelector.getItems().get(2));
            monthlyRepetitionComponent.showRepetitionDetail((MonthlyRepetition) event.getRepetition());
            setRepetitionComponentsInitValues(dailyRepetitionComponent, weeklyRepetitionComponent, yearlyRepetitionComponent);
        } else if (repetition instanceof WeeklyRepetition) {
            repetitionTypeSelector.setValue(repetitionTypeSelector.getItems().get(1));
            weeklyRepetitionComponent.showRepetitionDetail((WeeklyRepetition) event.getRepetition());
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
        startsDateField.setValue(event.getStartDate());
        startsField.setText(String.valueOf(event.getStartTime()));
        endsDateField.setValue(event.getEndDate());
        endsField.setText(String.valueOf(event.getEndTime()));
        alertDateField.setValue(event.getAlertDate());
        alertField.setText(String.valueOf(event.getAlertTime()));
        descriptionField.setText(event.getDescription());
    }

    private EventTypeItem getEventTypeItem(EventType type) {
        for (EventTypeItem eventTypeItem : typeSelector.getItems()) {
            if (eventTypeItem.getType().equals(type)) {
                return eventTypeItem;
            }
        }

        return null;
    }

    private RepetitionTypeItem getRepetitionTypeItem(EventType type) {
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

            if (userUseWindowToCreateEvent()) { // window is used for creating new event
                if (userWantsToWorkWithRepetition()) { // save repetition too
                    Repetition repetition = readRepetitionData();
                    event.setRepetition(repetition);
                }
                addEvent(event);
            } else {  // window is used for updating existing event
                if (this.event.getRepetition() == null || this.event.isExceptionInRepetition()) {
                    updateSingleEvent(event);
                } else {
                    updateEventInRepetition(event);
                }
            }
        }
    }

    private Repetition readRepetitionData() {
        Repetition repetition = selectedRepetitionComponent.readInput(); // read specific repetition fields

        if (repetitionInputIsOk(repetition)) {
            // read others repetition fields
            LocalDate repetitionStart = repetitionStartField.getValue();
            LocalDate repetitionEnd = repetitionEndField.getValue();

            repetition.setStart(repetitionStart);
            repetition.setEnd(repetitionEnd);

            return repetition;
        }

        // input error in repetition component
        return null;
    }

    private void updateAllEventsInRepetition() {
        Event event = readEventInput();
        try {
            eventsClient.updateAllEventsInRepetition(event, this.event.getRepetition().getId());
        } catch (Exception e) {
            if (e instanceof UnauthorizedException) {
                windowsCreator.createLoginWindow(resourceBundle, (Stage) ap.getScene().getWindow());
            } else if (e instanceof AccessDeniedException) {
                windowsCreator.showErrorAlert(resourceBundle.getString("updateEventAccessDeniedErrorMessage"), resourceBundle);
            } else if (e instanceof NotFoundException) {
                windowsCreator.showErrorAlert(resourceBundle.getString("eventNotFoundErrorMessage"), resourceBundle);
            } else {
                windowsCreator.showErrorAlert(resourceBundle.getString("updateEventErrorMessage"), resourceBundle);
            }
        }
    }

    private void saveRepetition() {
        Repetition repetition = readRepetitionData();
        if (event.getRepetition() == null) {
            event.setRepetition(repetition);
            addRepetition(repetition);
        } else {
            updateRepetition(repetition);
        }
    }

    private void addRepetition(Repetition repetition) {
        try {
            eventsClient.addRepetition(event.getId(), repetition);
            updateCalendarDisplay(event.getRepetition().getStart());
            Stage stage = (Stage) ap.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            if (e instanceof UnauthorizedException) {
                windowsCreator.createLoginWindow(resourceBundle, (Stage) ap.getScene().getWindow());
            } else if (e instanceof AccessDeniedException) {
                windowsCreator.showErrorAlert(resourceBundle.getString("addRepetitionAccessDeniedErrorMessage"), resourceBundle);
            } else if (e instanceof NotFoundException) {
                windowsCreator.showErrorAlert(resourceBundle.getString("repetitionNotFoundErrorMessage"), resourceBundle);
            } else {
                windowsCreator.showErrorAlert(resourceBundle.getString("addRepetitionErrorMessage"), resourceBundle);
            }
        }
    }

    private void updateRepetition(Repetition repetition) {
        try {
            eventsClient.updateRepetition(repetition, this.event.getRepetition().getId());
            updateCalendarDisplay(initDate);
            Stage stage = (Stage) ap.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            if (e instanceof UnauthorizedException) {
                windowsCreator.createLoginWindow(resourceBundle, (Stage) ap.getScene().getWindow());
            } else if (e instanceof AccessDeniedException) {
                windowsCreator.showErrorAlert(resourceBundle.getString("updateRepetitionAccessDeniedErrorMessage"), resourceBundle);
            } else if (e instanceof NotFoundException) {
                windowsCreator.showErrorAlert(resourceBundle.getString("repetitionNotFoundErrorMessage"), resourceBundle);
            } else {
                windowsCreator.showErrorAlert(resourceBundle.getString("updateRepetitionErrorMessage"), resourceBundle);
            }
        }
    }

    private boolean userWantsToWorkWithRepetition() {
        return repetitionBox.isVisible();
    }

    private boolean userUseWindowToCreateEvent() {
        return event == null;
    }

    private boolean repetitionInputIsOk(Repetition repetition) {
        return repetition != null;
    }

    private Event readEventInput() {
        String title = titleField.getText();
        String location = locationField.getText();
        EventType type = typeSelector.getValue().getType();
        LocalDate startDate = startsDateField.getValue();
        LocalTime startTime = LocalTime.parse(startsField.getText());
        LocalDate endDate = endsDateField.getValue();
        LocalTime endTime = LocalTime.parse(endsField.getText());
        LocalDate alertDate = alertDateField.getValue();
        LocalTime alertTime = LocalTime.parse(alertField.getText());
        String description = descriptionField.getText();

        return new Event(title, location, type, description, startDate, startTime, endDate, endTime, alertDate, alertTime);
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
     * After successful insert modal window is closed and calendar is displayed with just created event
     */
    public void addEvent(Event event) {
        try {
            Long id = eventsClient.addEvent(event);

            if (id != null) {
                if (newDateIsInCalendarDisplay(event.getStartDate()) && event.getRepetition() == null) {
                    planItMainWindowController.createEventInCalendar(event.getStartDate());
                } else {
                    updateCalendarDisplay(initDate);
                }

                Stage stage = (Stage) ap.getScene().getWindow();
                stage.close();
            }
        } catch (Exception e) {
            if (e instanceof UnauthorizedException) {
                windowsCreator.createLoginWindow(resourceBundle, (Stage) ap.getScene().getWindow());
            } else {
                windowsCreator.showErrorAlert(resourceBundle.getString("addEventErrorMessage"), resourceBundle);
            }
        }
    }

    private void updateEventInRepetition(Event event) {
        try {
            eventsClient.updateEventInRepetitionAtDate(event, this.event.getRepetition().getId(), initDate);

            updateCalendarDisplay(event.getStartDate());

            Stage stage = (Stage) ap.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            if (e instanceof UnauthorizedException) {
                windowsCreator.createLoginWindow(resourceBundle, (Stage) ap.getScene().getWindow());
            } else if (e instanceof AccessDeniedException) {
                windowsCreator.showErrorAlert(resourceBundle.getString("updateEventAccessDeniedErrorMessage"), resourceBundle);
            } else if (e instanceof NotFoundException) {
                windowsCreator.showErrorAlert(resourceBundle.getString("repetitionNotFoundErrorMessage"), resourceBundle);
            } else {
                windowsCreator.showErrorAlert(resourceBundle.getString("updateEventErrorMessage"), resourceBundle);
            }
        }
    }

    /**
     * Updating existing single event with or without repetition. (When ID of the event already exists.)
     * After successful update modal window is closed and calendar is displayed with just created event
     */
    private void updateSingleEvent(Event event) {
        try {
            eventsClient.updateEvent(event, this.event.getId());

            if (!newEventLabelIsEqualWithOld(event)) {
                if (!newDateIsInCalendarDisplay(event.getStartDate())) {
                    updateCalendarDisplay(event.getStartDate());
                } else {
                    event.setId(this.event.getId());
                    planItMainWindowController.updateEventInCalendar(event, this.event.getStartDate(), this.event.getStartTime());
                }
            }

            Stage stage = (Stage) ap.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            if (e instanceof UnauthorizedException) {
                windowsCreator.createLoginWindow(resourceBundle, (Stage) ap.getScene().getWindow());
            } else if (e instanceof AccessDeniedException) {
                windowsCreator.showErrorAlert(resourceBundle.getString("updateEventAccessDeniedErrorMessage"), resourceBundle);
            } else if (e instanceof NotFoundException) {
                windowsCreator.showErrorAlert(resourceBundle.getString("eventNotFoundErrorMessage"), resourceBundle);
            } else {
                windowsCreator.showErrorAlert(resourceBundle.getString("updateEventErrorMessage"), resourceBundle);
            }
        }

    }

    private boolean newEventLabelIsEqualWithOld(Event updatedEvent) {
        return updatedEvent.getStartDate().equals(this.event.getStartDate()) && updatedEvent.getStartTime().equals(this.event.getStartTime()) &&
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

    public void updateCalendarDisplay() {
        planItMainWindowController.initializeCalendar();
        planItMainWindowController.showEventsInCalendar();
        planItMainWindowController.addWeatherToCalendar();
    }

    private void delete() {
        if (event == null) {
            return;
        }

        Alert alert = createDeleteAlertWindow();

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            boolean success;
            if (event.getRepetition() != null) {
                if(event.isExceptionInRepetition()) {
                    success = deleteEvent(event.getId());
                } else {
                    success = deleteEventFromRepetition();
                }

            } else {
                success = deleteEvent(event.getId());
            }

            if (success) {
                Stage stage = (Stage) ap.getScene().getWindow();
                stage.close();
            }
        } else {
            alert.close();
        }
    }

    private void deleteAll() {
        if (event == null) {
            return;
        }

        Alert alert = createDeleteAlertWindow();

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            long eventToDeleteId = event.isExceptionInRepetition() ?
                    event.getRepetition().getEventId() :
                    event.getId();

            boolean success = deleteEvent(eventToDeleteId);

            if (success) {
                Stage stage = (Stage) ap.getScene().getWindow();
                stage.close();
            }
        } else {
            alert.close();
        }
    }

    /**
     * delete only this event from repetition
     */
    private boolean deleteEventFromRepetition() {
        try {
            eventsClient.deleteFromRepetition(this.event.getRepetition().getId(), event.getStartDate());
            planItMainWindowController.deleteEventFromCalendar(event.getId(), event.getStartDate());
            return true;
        } catch (Exception e) {
            if (e instanceof UnauthorizedException) {
                windowsCreator.createLoginWindow(resourceBundle, (Stage) ap.getScene().getWindow());
            } else if (e instanceof AccessDeniedException) {
                windowsCreator.showErrorAlert(resourceBundle.getString("deleteEventAccessDeniedErrorMessage"), resourceBundle);
            } else if (e instanceof NotFoundException) {
                windowsCreator.showErrorAlert(resourceBundle.getString("repetitionNotFoundErrorMessage"), resourceBundle);
            } else {
                windowsCreator.showErrorAlert(resourceBundle.getString("deleteEventErrorMessage"), resourceBundle);
            }
            return false;
        }
    }

    /**
     * deleting event without repetition or all events from repetition if it's repetead event
     */
    private boolean deleteEvent(long eventId) {
        try {
            eventsClient.deleteEvent(eventId);
            if (event.getRepetition() == null) {
                planItMainWindowController.deleteEventFromCalendar(event.getId(), event.getStartDate());
            } else {
                updateCalendarDisplay();
            }
            return true;
        } catch (Exception e) {
            if (e instanceof UnauthorizedException) {
                 windowsCreator.createLoginWindow(resourceBundle, (Stage) ap.getScene().getWindow());
            } else if (e instanceof AccessDeniedException) {
                windowsCreator.showErrorAlert(resourceBundle.getString("deleteEventAccessDeniedErrorMessage"), resourceBundle);
            } else if (e instanceof NotFoundException) {
                windowsCreator.showErrorAlert(resourceBundle.getString("eventNotFoundErrorMessage"), resourceBundle);
            } else {
                 windowsCreator.showErrorAlert(resourceBundle.getString("deleteEventErrorMessage"), resourceBundle);
            }
            return false;
        }
    }

    private Alert createDeleteAlertWindow() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(resourceBundle.getString("deleteConfirmationTitle"));
        if (event.getRepetition() == null || event.getRepetition() != null && !repetitionBox.isVisible()) {
            alert.setHeaderText(resourceBundle.getString("deleteConfirmationHeaderText") + event.getTitle());
            alert.setContentText(resourceBundle.getString("deleteConfirmationQuestion"));
        } else {
            alert.setHeaderText(resourceBundle.getString("deleteAllConfirmationHeaderText") + event.getTitle());
            alert.setContentText(resourceBundle.getString("deleteAllConfirmationQuestion"));
        }

        return alert;
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

    private int getDayDiff(LocalDate date1, LocalDate date2) {
        DateTime dateTime1 = new DateTime().withDate(date1.getYear(), date1.getMonthValue(), date1.getDayOfMonth());
        DateTime dateTime2 = new DateTime().withDate(date2.getYear(), date2.getMonthValue(), date2.getDayOfMonth());

        return Days.daysBetween(dateTime1, dateTime2).getDays() /*+ 1*/;
    }

    private int getMinuteDiff(LocalDateTime dateTimeI, LocalDateTime dateTimeII) {
        DateTime dateTime1 = new DateTime(dateTimeI.getYear(), dateTimeI.getMonthValue(), dateTimeI.getDayOfMonth(),
                dateTimeI.getHour(), dateTimeI.getMinute());
        DateTime dateTime2 = new DateTime(dateTimeII.getYear(), dateTimeII.getMonthValue(), dateTimeII.getDayOfMonth(),
                dateTimeII.getHour(), dateTimeII.getMinute());

        return Minutes.minutesBetween(dateTime1, dateTime2).getMinutes();
    }
}
