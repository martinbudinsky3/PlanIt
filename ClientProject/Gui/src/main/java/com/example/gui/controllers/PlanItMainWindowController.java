package com.example.gui.controllers;

import com.example.client.clients.EventsClient;
import com.example.client.clients.UsersClient;
import com.example.client.model.Event;
import com.example.client.model.User;
import com.example.gui.utils.PdfFile;
import com.example.gui.utils.WindowsCreator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

/** Controller for "PlanItMainWindow.fxml" */
public class PlanItMainWindowController implements Initializable, LanguageChangeWindow {
    private final WindowsCreator windowsCreator;
    private final EventsClient eventsClient;
    private final UsersClient usersClient;
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

    private boolean threadFlag;
    private boolean threadActive;
    private Integer selectedYear;
    private Integer selectedMonth;
    private Node[][] gridPaneNodes;
    private String months[];
    private ResourceBundle resourceBundle;

    public PlanItMainWindowController(EventsClient eventsClient, UsersClient usersClient, User user, WindowsCreator windowsCreator) {
        this.eventsClient = eventsClient;
        this.usersClient = usersClient;
        this.user = user;
        this.windowsCreator = windowsCreator;

        threadFlag = false;
        threadActive = true;
    }


    /** Setting year label
     * @param selectedYear the year that is currently selected*/
    public void setSelectedYear(int selectedYear) {
        this.selectedYear = selectedYear;
        yearLabel.setText(Integer.toString(selectedYear));
    }

    /** Setting month label
     * @param selectedMonth the month that is currently selected*/
    public void setSelectedMonth(int selectedMonth) {
        this.selectedMonth = selectedMonth;
        monthLabel.setText(months[selectedMonth - 1]);
    }


    /** Initializing current year and month, adding buttons functionality, creating calendar layout
     * and display, populating calendar with events in current month, start of new thread for alert functionality
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        if(!threadFlag) { // if thread isn't created yet
            threadFlag = true;
            startTask();
        }
        createGridPaneNodes();
        addHandlers();
        initializeMonthsAndYear();
        initializeCalendar();
        showEventsInCalendar();
    }

    /** Reloading window to change to just selected language. */
    @Override
    public void reload(ResourceBundle bundle) {
        windowsCreator.reload(ap, bundle, "fxml/PlanItMainWindow.fxml", this);
    }

    /**
     * Method that creates and starts new background thread with event alert functionality
     */
    public void startTask() {
        // Create a Runnable
        Runnable task = () -> runTask();

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
    public void runTask() {
        while(threadActive) {
            try {
                List<Event> events = eventsClient.getEventsToAlert(user.getIdUser(), resourceBundle);

                // show alert for every event that is returned
                for (int i = 0; i < events.size(); i++) {
                    Event event = events.get(i);
                    Platform.runLater(() -> {
                        windowsCreator.createAlertWindow(user, event, eventsClient, this, resourceBundle);
                        playAlertSound();
                    });
                }

                int seconds = 60 - LocalTime.now().getSecond();  // so alert comes at the beginning of the minute
                Thread.sleep(seconds * 1000);
            } catch(InterruptedException ex) {
                // TODO log
            }
        }
    }

    /** Sound of notification is played. */
    public void playAlertSound(){
        URL file = PlanItMainWindowController.class.getClassLoader().getResource("sounds/chimes.wav");
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch(Exception e){
            showClientErrorAlert();
            e.printStackTrace();
        }
    }

    /**
     *  Method that creates grid pane nodes array - help for calendar layout and display
     */
    public void createGridPaneNodes(){
        gridPaneNodes = new Node[7][7];
        for (Node child : calendar.getChildren()) {
            Integer column = calendar.getColumnIndex(child);
            Integer row = calendar.getRowIndex(child);
            if(row == null) {
                row = 0;
            }
            if(column == null){
                column = 0;
            }
            gridPaneNodes[column][row] = child;
        }
    }

    /** Adding functionality to buttons */
    public void addHandlers() {
        addHandlerToDayVBoxes();
        yearBack.setOnAction(e -> yearBackHandler());
        yearForward.setOnAction(e -> yearForwardHandler());
        addEventButton.setOnAction(e -> windowsCreator.createAddEventWindow(user, LocalDate.now(), eventsClient,
                this, resourceBundle, ap));
        logoutButton.setOnAction(e -> {
            try {
                windowsCreator.createLoginWindow(resourceBundle, ap, usersClient);
                threadActive = false;
            } catch (IOException ex) {
                windowsCreator.showErrorAlert(resourceBundle);
                ex.printStackTrace(); // TODO logging
            }
        });
        changeLanguageButton.setOnAction(e -> {
            windowsCreator.createLanguageSelectorWindow(this, resourceBundle);
        });
        savePdfButton.setOnAction(e -> {
            savePdfButtonHandler();
        });
    }

    /** Button for creating Pdf File. After creating PDF, informing window appears*/
    private void savePdfButtonHandler() {
        PdfFile pdfFile = new PdfFile(user, selectedYear, selectedMonth, eventsClient, resourceBundle, windowsCreator);
        pdfFile.pdf();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(resourceBundle.getString("pdfAlertTitle"));
        alert.setHeaderText(null);
        alert.setContentText(resourceBundle.getString("pdfAlertContent"));
        alert.showAndWait();
    }

    /** Finding current year and date, initializing labels with corresponding values
     * initialize months list
     */
    public void initializeMonthsAndYear() {
        // find out current year and month that will be displayed
        if(selectedMonth == null && selectedYear == null) {
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
        for (int i = 0; i < 12; i++){
            monthsList.getItems().add(months[i].toUpperCase());
        }

        // initialize labels
        yearLabel.setText(Integer.toString(selectedYear));
        monthLabel.setText(months[selectedMonth - 1]);
        // add listener to list cells
        monthsList.getSelectionModel().selectedItemProperty().addListener((observable, oldvalue, newvalue) -> {
            selectedMonth = monthsList.getSelectionModel().getSelectedIndex() + 1;
            newvalue.toLowerCase();
            newvalue = newvalue.substring(0, 1) + newvalue.substring(1).toLowerCase();
            monthLabel.setText(newvalue);
            initializeCalendar();
            showEventsInCalendar();
        });
    }

    /**
     * Adding handlers on click on VBoxes that represent day box in calendar
     */
    public void addHandlerToDayVBoxes(){
        for (int i = 1; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                VBox dayVBox = (VBox) gridPaneNodes[j][i];
                dayVBox.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                    if (!dayVBox.getChildren().isEmpty()) {  // that's how i know it is day VBox
                        Label dayLabel = (Label) dayVBox.getChildren().get(0);
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

    /** Initialize calendar. Find out when days in months started. Making som calculations so it
     * fills the cells of the calendar by numbers of days in coresponding boxes */
    public void initializeCalendar() {
        // find out when does month start and end
        GregorianCalendar gregorianCalendar = new GregorianCalendar(selectedYear, selectedMonth - 1, 1);
        int firstDayOfMonth = gregorianCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        if(firstDayOfMonth == 0){
            firstDayOfMonth = 7;
        }
        int daysInMonth = gregorianCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        clearCalendar();

        // add day number labels to calendar fields
        int fieldCounter = 1;
        int dayCounter = 1;
        for (int i = 1; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if (dayCounter > daysInMonth || fieldCounter < firstDayOfMonth) {  // this VBoxes will not be day boxes in current month
                    VBox dayVBox = (VBox) gridPaneNodes[j][i];
                    dayVBox.getStyleClass().clear();
                    dayVBox.getStyleClass().add("extra-day");
                    fieldCounter++;
                    continue;
                }

                if (fieldCounter >= firstDayOfMonth) {  // this VBoxes will be day boxes in current month
                    VBox dayVBox = (VBox) gridPaneNodes[j][i];

                    Label dayLabel = new Label(Integer.toString(dayCounter));
                    dayVBox.getChildren().add(dayLabel);
                    dayVBox.getStyleClass().clear();
                    dayVBox.getStyleClass().add("day");
                    VBox.setMargin(dayLabel, new Insets(5, 0, 2, 5));
                    dayCounter++;
                    fieldCounter++;
                }
            }
        }
    }

    /** Adding events into calendar cells. The starts time and title of the event are displayed.
     * It is possible to click on each event to show the detail. */
    public void showEventsInCalendar() {
//        try {
            List<Event> events = eventsClient.getUserEventsByMonth(user.getIdUser(), selectedYear, selectedMonth, resourceBundle);
//            if(events == null){
//                showServerErrorAlert();
//                return;
//            }
            for(int e = 0; e < events.size(); e++) {
                for (int i = 1; i < 7; i++) {
                    for (int j = 0; j < 7; j++) {
                        VBox dayVBox = (VBox) gridPaneNodes[j][i];
                        if(dayVBox.getChildren().isEmpty()){  // if VBox is not day box
                            continue;
                        }

                        Label dayLabel = (Label) dayVBox.getChildren().get(0);
                        int dayNumber = Integer.parseInt(dayLabel.getText());
                        Event event = events.get(e);

                        if(event.getDate().getDayOfMonth() == dayNumber) { // if event is from day of current day box add it to that day box
                            Label eventLabel = new Label();
                            String eventLabelText = event.getStarts() + " " + event.getTitle();
                            eventLabel.setText(eventLabelText);
                            eventLabel.setId(Integer.toString(event.getIdEvent())); // storing event id in its label id
                            eventLabel.setPrefWidth(dayVBox.getPrefWidth());
                            eventLabel.getStyleClass().add("event-label");
                            eventLabel.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> { // add handler to event label
                                windowsCreator.createEventDetailWindow(Integer.parseInt(eventLabel.getId()), eventLabel.getText(),
                                        user, eventsClient, this, resourceBundle, ap);
                                mouseEvent.consume();
                            });

                            dayVBox.getChildren().add(eventLabel);
                            VBox.setMargin(eventLabel, new Insets(0, 0, 0, 5));
                        }
                    }
                }
            }
//        } catch (Exception e){
//            showClientErrorAlert();
//            System.out.println(e.getMessage());
//        }
    }

    /** Clear calendar (without number of days and events in calendar cells.)
     * It is used in reloading of calendar e.g in displaying calendar for new selected month
     */
    public void clearCalendar(){
        for(int i = 1; i < 7; i++) {
            for(int j = 0; j < 7; j++) {
                VBox vBox = (VBox) gridPaneNodes[j][i];
                if(vBox.getChildren().isEmpty()){
                    continue;
                }
                vBox.getChildren().clear();
            }
        }
    }

    /** Button yearBack allows user to show his events in calendar for given month but last year. */
    public void yearBackHandler() {
        selectedYear -= 1;
        yearLabel.setText(Integer.toString(selectedYear));
        initializeCalendar();
        showEventsInCalendar();
    }

    /** Button yearForward allows user to show his events in calendar for given month but next year. */
    public void yearForwardHandler() {
        selectedYear += 1;
        yearLabel.setText(Integer.toString(selectedYear));
        initializeCalendar();
        showEventsInCalendar();
    }

    public void showServerErrorAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(resourceBundle.getString("serverError"));
        alert.setHeaderText(resourceBundle.getString("errorAlertHeader"));
        alert.setContentText(resourceBundle.getString("errorAlertContext"));
        alert.showAndWait();
    }

    public void showClientErrorAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(resourceBundle.getString("error"));
        alert.setHeaderText(resourceBundle.getString("errorAlertHeader"));
        alert.setContentText(resourceBundle.getString("errorAlertContext"));
        alert.showAndWait();
    }
}
