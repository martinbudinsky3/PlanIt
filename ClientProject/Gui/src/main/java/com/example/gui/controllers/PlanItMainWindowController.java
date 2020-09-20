package com.example.gui.controllers;

import com.example.client.clients.EventsClient;
import com.example.client.clients.UsersClient;
import com.example.client.clients.WeatherClient;
import com.example.client.model.Event;
import com.example.client.model.User;
import com.example.client.model.weather.DailyWeather;
import com.example.utils.PdfFile;
import com.example.utils.Utils;
import com.example.utils.WindowsCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

/**
 * Controller for "PlanItMainWindow.fxml"
 */
public class PlanItMainWindowController implements Initializable, LanguageChangeWindow {
    private static final int CALENDAR_WIDTH = 7;
    private static final int CALENDAR_HEIGHT = 7;
    private static final int SECONDS_IN_MINUTE = 60;
    private static final int SECONDS_IN_DAY = 86400;
    private static final int MILLIS_IN_SECOND = 1000;
    private static final int FORECASTED_DAYS = 7;

    private static final Logger logger = LoggerFactory.getLogger(PlanItMainWindowController.class);
    private final WindowsCreator windowsCreator;
    private final EventsClient eventsClient;
    private final UsersClient usersClient;
    private final WeatherClient weatherClient;
    private final User user;

    @FXML
    private AnchorPane ap;
    @FXML
    private GridPane calendar;
    @FXML
    private Button addEventButton;
    @FXML
    private Button changeLanguageButton;
    @FXML
    private Button logoutButton;
    @FXML
    private Label yearLabel;
    @FXML
    private Label monthLabel;
    @FXML
    private Button yearBack;
    @FXML
    private Button yearForward;
    @FXML
    private Button savePdfButton;
    @FXML
    private ListView<String> monthsList;

    private boolean threadActive = false;
    private Integer selectedYear;
    private Integer selectedMonth;
    private Node[][] gridPaneNodes;
    private String months[];
    private ResourceBundle resourceBundle;

    public PlanItMainWindowController(EventsClient eventsClient, UsersClient usersClient, WeatherClient weatherClient, User user, WindowsCreator windowsCreator) {
        this.eventsClient = eventsClient;
        this.usersClient = usersClient;
        this.weatherClient = weatherClient;
        this.user = user;
        this.windowsCreator = windowsCreator;
    }

    public AnchorPane getAnchorPane() {
        return ap;
    }

    public int getSelectedYear() {
        return selectedYear;
    }

    /**
     * Setting year label
     *
     * @param selectedYear the year that is currently selected
     */
    public void setSelectedYear(int selectedYear) {
        this.selectedYear = selectedYear;
        yearLabel.setText(Integer.toString(selectedYear));
    }

    public int getSelectedMonth() {
        return selectedMonth;
    }

    /**
     * Setting month label
     *
     * @param selectedMonth the month that is currently selected
     */
    public void setSelectedMonth(int selectedMonth) {
        this.selectedMonth = selectedMonth;
        monthLabel.setText(months[selectedMonth - 1]);
    }


    /**
     * Initializing current year and month, adding buttons functionality, creating calendar layout
     * and display, populating calendar with events in current month, start of new thread for alert functionality
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;

        createGridPaneNodes();
        addHandlers();
        initializeMonthsAndYear();
        initializeCalendar();
        showEventsInCalendar();
        if (!threadActive) { // if thread isn't created yet
            threadActive = true;
            startAlertTask();
            startWeatherTask();
        }
    }

    /**
     * Reloading window to change to just selected language.
     */
    @Override
    public void reload(ResourceBundle bundle) {
        threadActive = false;
        windowsCreator.reload(ap, bundle, "fxml/PlanItMainWindow.fxml", this);
    }

    /**
     * Method that creates and starts new background thread with event alert functionality
     */
    public void startAlertTask() {
        // Create a Runnable
        Runnable task = () -> runAlertTask();

        // Run the task in a background thread
        Thread backgroundThread = new Thread(task);
        // Terminate the running thread if the application exits
        backgroundThread.setDaemon(true);
        // Start the thread
        backgroundThread.start();
    }

    public void startWeatherTask() {
        // Create a Runnable
        Runnable task = () -> runWeatherTask();

        // Run the task in a background thread
        Thread backgroundThread = new Thread(task);
        // Terminate the running thread if the application exits
        backgroundThread.setDaemon(true);
        // Start the thread
        backgroundThread.start();
    }

    /**
     * Functionality of thread - getting events with alert time in current minute.
     * It is getting events at the beginning of every minute.
     */
    public void runAlertTask() {
        while (threadActive) {
            try {
                Platform.runLater(() -> {

                    List<Event> events = eventsClient.getEventsToAlert(user.getIdUser(), resourceBundle);

                    // TODO for each
                    // show alert for every event that is returned
                    for (int i = 0; i < events.size(); i++) {
                        Event event = events.get(i);
                        windowsCreator.createAlertWindow(user, event, eventsClient, this, resourceBundle);
                        playAlertSound();
                    }
                });

                int seconds = SECONDS_IN_MINUTE - LocalTime.now().getSecond();  // so alert comes at the beginning of the minute
                Thread.sleep(seconds * MILLIS_IN_SECOND);
            } catch (InterruptedException ex) {
                logger.error("Error in thread that gets events to alert", ex);
            }
        }
    }

    // TODO this should be in AlertWindowController
    /**
     * Sound of notification is played.
     */
    public void playAlertSound() {
        URL file = PlanItMainWindowController.class.getClassLoader().getResource("sounds/chimes.wav");
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (Exception ex) {
            windowsCreator.showErrorAlert(resourceBundle);
            logger.error("Error while playing alert sound", ex);
        }
    }

    public void runWeatherTask() {
        while(threadActive) {
            try {
                Platform.runLater(() -> {
                    addWeatherToCalendar();
                });

                int seconds = SECONDS_IN_DAY - LocalTime.now().toSecondOfDay();  // so alert comes at the beginning of the minute
                Thread.sleep(seconds * MILLIS_IN_SECOND);
            } catch (InterruptedException ex) {
                logger.error("Error in thread that gets weather forecast", ex);
            }
        }
    }

    public void addWeatherToCalendar() {
        if(!checkWeatherForecast()) {
            return;
        }

        List<DailyWeather> weatherForecast = weatherClient.getWeather();

        for (DailyWeather dailyWeather : weatherForecast) {
            if (dailyWeather.getDate().getMonthValue() != selectedMonth) {
                continue;
            }

            int j = Utils.countColumnIndexInCalendar(dailyWeather.getDate().getDayOfMonth(), selectedYear, selectedMonth);
            int i = Utils.countRowIndexInCalendar(dailyWeather.getDate().getDayOfMonth(), selectedYear, selectedMonth);

            VBox dayVBox = (VBox) gridPaneNodes[j][i];

            HBox dayHeader = (HBox) dayVBox.getChildren().get(0);
            Image img = new Image(new ByteArrayInputStream(dailyWeather.getWeather().get(0).getIconImage()));
            ImageView imageView = new ImageView(img);
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            dayHeader.getChildren().add(imageView);
            Label temperatureLabel = new Label("" + dailyWeather.getMinTemperature() + " - " + dailyWeather.getMaxTemperature() + "°C");
            dayHeader.getChildren().add(temperatureLabel);
            HBox.setMargin(imageView, new Insets(0, 0, 0, 10));
            HBox.setMargin(temperatureLabel, new Insets(10, 0, 0, 0));
        }
    }

    private boolean checkWeatherForecast() {
        return selectedYear == LocalDate.now().getYear() &&
                (selectedMonth == LocalDate.now().getMonthValue() ||
                        selectedMonth == LocalDate.now().plusDays(FORECASTED_DAYS).getMonthValue());
    }

    /**
     * Method that creates grid pane nodes array - help for calendar layout and display
     */
    public void createGridPaneNodes() {
        gridPaneNodes = new Node[7][7];
        for (Node child : calendar.getChildren()) {
            Integer column = calendar.getColumnIndex(child);
            Integer row = calendar.getRowIndex(child);
            if (row == null) {
                row = 0;
            }
            if (column == null) {
                column = 0;
            }
            gridPaneNodes[column][row] = child;
        }
    }

    /**
     * Adding functionality to buttons
     */
    public void addHandlers() {
        addHandlerToDayVBoxes();
        yearBack.setOnAction(e -> yearBackHandler());
        yearForward.setOnAction(e -> yearForwardHandler());
        addEventButton.setOnAction(e -> windowsCreator.createAddEventWindow(user, LocalDate.now(), eventsClient,
                this, resourceBundle, ap));
        logoutButton.setOnAction(e -> {
            try {
                windowsCreator.createLoginWindow(resourceBundle, (Stage) ap.getScene().getWindow(), usersClient);
                threadActive = false;
            } catch (IOException ex) {
                windowsCreator.showErrorAlert(resourceBundle);
                logger.error("Error while logging out", ex);
            }
        });
        changeLanguageButton.setOnAction(e -> {
            windowsCreator.createLanguageSelectorWindow(this, resourceBundle);
        });
        savePdfButton.setOnAction(e -> {
            savePdfButtonHandler();
        });
    }

    /**
     * Button for creating Pdf File. After creating PDF, informing window appears
     */
    private void savePdfButtonHandler() {
        File file = showFileChooser();
        if (file != null) {  // if user hit cancel button nothing happens
            PdfFile pdfFile = new PdfFile(user, selectedYear, selectedMonth, eventsClient, resourceBundle, file, windowsCreator);
            pdfFile.pdf();
        }
    }

    private File showFileChooser() {
        FileChooser fileChooser = new FileChooser();

        // set extension filter for pdf files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);

        // set default pdf file name
        fileChooser.setInitialFileName("PlanIt.pdf");

        // show save file dialog
        File file = fileChooser.showSaveDialog(ap.getScene().getWindow());

        return file;
    }

    /**
     * Finding current year and date, initializing labels with corresponding values
     * initialize months list
     */
    public void initializeMonthsAndYear() {
        // find out current year and month that will be displayed
        if (selectedMonth == null && selectedYear == null) {
            LocalDate today = LocalDate.now();
            selectedYear = today.getYear();
            selectedMonth = today.getMonth().getValue();
        }

        // initialize monthsList
        months = new String[]{resourceBundle.getString("january"), resourceBundle.getString("february"),
                resourceBundle.getString("march"), resourceBundle.getString("april"),
                resourceBundle.getString("may"), resourceBundle.getString("june"),
                resourceBundle.getString("july"), resourceBundle.getString("august"),
                resourceBundle.getString("september"), resourceBundle.getString("october"),
                resourceBundle.getString("november"), resourceBundle.getString("december")};
        for (int i = 0; i < 12; i++) {
            monthsList.getItems().add(months[i].toUpperCase());
        }

        // initialize labels
        yearLabel.setText(Integer.toString(selectedYear));
        monthLabel.setText(months[selectedMonth - 1]);
        // add listener to list cells
        monthsList.getSelectionModel().selectedItemProperty().addListener((observable, oldvalue, newvalue) -> {
            selectedMonth = monthsList.getSelectionModel().getSelectedIndex() + 1;
            newvalue = newvalue.substring(0, 1) + newvalue.substring(1).toLowerCase();
            monthLabel.setText(newvalue);
            initializeCalendar();
            showEventsInCalendar();
            addWeatherToCalendar();
        });
    }

    /**
     * Adding handlers on click on VBoxes that represent day box in calendar
     */
    public void addHandlerToDayVBoxes() {
        for (int i = 1; i < CALENDAR_HEIGHT; i++) {
            for (int j = 0; j < CALENDAR_WIDTH; j++) {
                VBox dayVBox = (VBox) gridPaneNodes[j][i];
                dayVBox.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                    if (!dayVBox.getChildren().isEmpty()) {  // that's how i know it is day VBox
                        HBox dayHeader = (HBox) dayVBox.getChildren().get(0);
                        Label dayLabel = (Label) dayHeader.getChildren().get(0);
                        int day = Integer.parseInt(dayLabel.getText());  // get day from dayLabel
                        LocalDate initDate = LocalDate.of(selectedYear, selectedMonth, day);
                        windowsCreator.createAddEventWindow(user, initDate, eventsClient, this,
                                resourceBundle, ap); // opens add event window
                        e.consume();
                    }
                });
            }
        }
    }

    /**
     * Initialize calendar. Find out when days in months started. Making som calculations so it
     * fills the cells of the calendar by numbers of days in coresponding boxes
     */
    public void initializeCalendar() {
        // find out when does month start and end
        GregorianCalendar gregorianCalendar = new GregorianCalendar(selectedYear, selectedMonth - 1, 1);
        int firstDayOfMonth = gregorianCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (firstDayOfMonth == 0) {
            firstDayOfMonth = 7;
        }
        int daysInMonth = gregorianCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        clearCalendar();

        // add day number labels to calendar fields
        int fieldCounter = 1;
        int dayCounter = 1;
        for (int i = 1; i < CALENDAR_HEIGHT; i++) {
            for (int j = 0; j < CALENDAR_WIDTH; j++) {
                if (dayCounter > daysInMonth || fieldCounter < firstDayOfMonth) {  // this VBoxes will not be day boxes in current month
                    VBox dayVBox = (VBox) gridPaneNodes[j][i];
                    dayVBox.getStyleClass().clear();
                    dayVBox.getStyleClass().add("extra-day");
                    fieldCounter++;
                    continue;
                }

                if (fieldCounter >= firstDayOfMonth) {  // this VBoxes will be day boxes in current month
                    VBox dayVBox = (VBox) gridPaneNodes[j][i];
                    dayVBox.setId(Integer.toString(dayCounter)); // set day of month as id

                    Label dayLabel = new Label(Integer.toString(dayCounter));
                    HBox dayHeader = new HBox();
                    dayHeader.setMaxHeight(60);
                    dayHeader.getChildren().add(dayLabel);
                    dayVBox.getChildren().add(dayHeader);
                    dayVBox.getStyleClass().clear();
                    dayVBox.getStyleClass().add("day");
                    HBox.setMargin(dayLabel, new Insets(8, 0, 2, 8));
                    dayCounter++;
                    fieldCounter++;
                }
            }
        }
    }

    /**
     * Adding events into calendar cells. The starts time and title of the event are displayed.
     * It is possible to click on each event to show the detail.
     */
    public void showEventsInCalendar() {
        List<Event> events = eventsClient.getUserEventsByMonth(user.getIdUser(), selectedYear, selectedMonth, resourceBundle);

        for (Event event : events) {
            if(event.getDates() == null || event.getDates().isEmpty()) {
                int j = Utils.countColumnIndexInCalendar(event.getDate().getDayOfMonth(), selectedYear, selectedMonth);
                int i = Utils.countRowIndexInCalendar(event.getDate().getDayOfMonth(), selectedYear, selectedMonth);

                VBox dayVBox = (VBox) gridPaneNodes[j][i];
                addEventToCalendar(event, dayVBox);

                continue;
            }

            for(LocalDate date : event.getDates()) {
                int j = Utils.countColumnIndexInCalendar(date.getDayOfMonth(), selectedYear, selectedMonth);
                int i = Utils.countRowIndexInCalendar(date.getDayOfMonth(), selectedYear, selectedMonth);

                VBox dayVBox = (VBox) gridPaneNodes[j][i];
                addEventToCalendar(event, dayVBox);
            }
        }
    }

    /**
     * Clear calendar (without number of days and events in calendar cells.)
     * It is used in reloading of calendar e.g in displaying calendar for new selected month
     */
    public void clearCalendar() {
        for (int i = 1; i < CALENDAR_HEIGHT; i++) {
            for (int j = 0; j < CALENDAR_WIDTH; j++) {
                VBox vBox = (VBox) gridPaneNodes[j][i];
                if (vBox.getChildren().isEmpty()) {
                    continue;
                }
                vBox.getChildren().clear();
            }
        }
    }

    /**
     * Button yearBack allows user to show his events in calendar for given month but last year.
     */
    public void yearBackHandler() {
        selectedYear -= 1;
        yearLabel.setText(Integer.toString(selectedYear));
        initializeCalendar();
        showEventsInCalendar();
        addWeatherToCalendar();
    }

    /**
     * Button yearForward allows user to show his events in calendar for given month but next year.
     */
    public void yearForwardHandler() {
        selectedYear += 1;
        yearLabel.setText(Integer.toString(selectedYear));
        initializeCalendar();
        showEventsInCalendar();
        addWeatherToCalendar();
    }

    public void deleteEventFromCalendar(long idEvent, LocalDate date) {
        int j = Utils.countColumnIndexInCalendar(date.getDayOfMonth(), selectedYear, selectedMonth);
        int i = Utils.countRowIndexInCalendar(date.getDayOfMonth(), selectedYear, selectedMonth);

        VBox dayVBox = (VBox) gridPaneNodes[j][i];

        List<Node> nodes = dayVBox.getChildren();

        for(int n = 1; n < nodes.size(); n++) {
            Label eventLabel = (Label) nodes.get(n);
            if(Integer.parseInt(eventLabel.getId()) == idEvent) {
                logger.debug("Removing event with id " + idEvent + " from field of date " + date);
                nodes.remove(eventLabel);
                break;
            }
        }
    }

    public void updateEventInCalendar(Event event, LocalDate oldDate, LocalTime oldTime) {
        // TODO set event id to label
        int j = Utils.countColumnIndexInCalendar(event.getDate().getDayOfMonth(), selectedYear, selectedMonth);
        int i = Utils.countRowIndexInCalendar(event.getDate().getDayOfMonth(), selectedYear, selectedMonth);

        VBox dayVBox = (VBox) gridPaneNodes[j][i];
        List<Node> nodes = dayVBox.getChildren();

        if(oldDate.equals(event.getDate()) && oldTime.equals(event.getStarts())) {
            for(int n = 1; n < nodes.size(); n++) {
                Label eventLabel = (Label) nodes.get(n);
                if(Integer.parseInt(eventLabel.getId()) == event.getIdEvent()) {
                    String eventLabelText = event.getStarts() + " " + event.getTitle();
                    eventLabel.setText(eventLabelText);
                    break;
                }
            }
        } else {
            deleteEventFromCalendar(event.getIdEvent(), oldDate);

            nodes.subList(1, nodes.size()).clear(); // remove only event labels, not header with day number and weather

            List<Event> events = eventsClient.getUserEventsByDate(user.getIdUser(), event.getDate(), resourceBundle);

            for (Event ev : events) {
                addEventToCalendar(ev, dayVBox);
            }
        }
    }

    public void createEventInCalendar(LocalDate date) {
        int j = Utils.countColumnIndexInCalendar(date.getDayOfMonth(), selectedYear, selectedMonth);
        int i = Utils.countRowIndexInCalendar(date.getDayOfMonth(), selectedYear, selectedMonth);

        VBox dayVBox = (VBox) gridPaneNodes[j][i];

        List<Node> nodes = dayVBox.getChildren();
        nodes.subList(1, nodes.size()).clear(); // remove only event labels, not header with day number and weather

        List<Event> events = eventsClient.getUserEventsByDate(user.getIdUser(), date, resourceBundle);

        for (Event ev : events) {
            addEventToCalendar(ev, dayVBox);
        }
    }

    private void addEventToCalendar(Event event, VBox dayVBox) {
        Label eventLabel = new Label();
        String eventLabelText = event.getStarts() + " " + event.getTitle();
        eventLabel.setText(eventLabelText);
        eventLabel.setId(Long.toString(event.getIdEvent())); // storing event id in its label id
        eventLabel.setPrefWidth(dayVBox.getPrefWidth());
        eventLabel.getStyleClass().add("event-label");
        if (event.getType() == Event.Type.FREE_TIME) {
            eventLabel.getStyleClass().add("free-time-label");
        } else if (event.getType() == Event.Type.WORK) {
            eventLabel.getStyleClass().add("work-label");
        } else if (event.getType() == Event.Type.SCHOOL) {
            eventLabel.getStyleClass().add("school-label");
        }

        eventLabel.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> { // add handler to event label
            eventLabelClickedHandler(eventLabel);
            mouseEvent.consume();
        });

        dayVBox.getChildren().add(eventLabel);
        VBox.setMargin(eventLabel, new Insets(0, 0, 0, 8));
    }

    private void eventLabelClickedHandler(Label eventLabel) {
        try {
            VBox dayOfMonthBox = (VBox) eventLabel.getParent();
            int dayOfMonth = Integer.parseInt(dayOfMonthBox.getId());
            // TODO move API call to event detail window controller
            Event event = eventsClient.getEvent(user.getIdUser(), Integer.parseInt(eventLabel.getId()),
                    LocalDate.of(selectedYear, selectedMonth, dayOfMonth));
            windowsCreator.createEventDetailWindow(event, eventLabel.getText(),
                    user, eventsClient, this, resourceBundle);
        } catch (JsonProcessingException | ResourceAccessException | HttpStatusCodeException ex) {
            windowsCreator.showErrorAlert(resourceBundle.getString("getEventErrorMessage"), resourceBundle);
            if (ex instanceof JsonProcessingException) {
                logger.error("Error. Something went wrong while finding event by user's [" + user.getIdUser() + "] " +
                        "and event's [" + Integer.parseInt(eventLabel.getId()) + "] ID", ex);
            } else if (ex instanceof ResourceAccessException) {
                logger.error("Error while connecting to server", ex);
            } else {
                logger.error("Error. Something went wrong while finding event by user's [" + user.getIdUser() + "] " +
                        "and event's [" + Integer.parseInt(eventLabel.getId()) + "] ID." +
                        " HTTP status: " + ((HttpStatusCodeException) ex).getRawStatusCode(), ex);
            }
        }
    }
}
