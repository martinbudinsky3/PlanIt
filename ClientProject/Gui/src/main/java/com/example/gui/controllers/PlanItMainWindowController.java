package com.example.gui.controllers;

import com.example.client.clients.EventsClient;
import com.example.client.clients.UsersClient;
import com.example.client.model.Event;
import com.example.client.model.User;
import com.example.gui.utils.PdfFile;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

/** Controller for "PlanItMainWindow.fxml" */
public class PlanItMainWindowController implements Initializable, LanguageChangeWindow {
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
    private Integer selectedYear;
    private Integer selectedMonth;
    private Node[][] gridPaneNodes;
    private String months[];
    private ResourceBundle resourceBundle;
    private final EventsClient eventsClient;
    private final UsersClient usersClient;
    private final User user;

    public PlanItMainWindowController(EventsClient eventsClient, UsersClient usersClient, User user) {
        this.eventsClient = eventsClient;
        this.usersClient = usersClient;
        this.user = user;
        threadFlag = false;
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


    /** Initializing calendar, current year and month, adding buttons functionality. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        if(!threadFlag) {
            startTask();
            threadFlag = true;
        }
        createGridPaneNodes();
        addHandlers();
        initializeMonthsAndYear();
        initializeCalendar();
        showEventsInCalendar();
    }

    /** Reloading window to change to language. */
    @Override
    public void reload(ResourceBundle bundle) {
        try {
            Scene scene = ap.getScene();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("fxml/PlanItMainWindow.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setResources(bundle);
            AnchorPane rootPane = (AnchorPane) fxmlLoader.load();
            scene.setRoot(rootPane);
        } catch(IOException e){
            showClientErrorAlert();
            e.printStackTrace();
        }
    }

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

    public void runTask() {
        while(true) {
            try {
                List<Event> events = eventsClient.getEventsToAlert(user.getIdUser());

                if(events != null) {
                    // show alert for every event that is returned
                    for (int i = 0; i < events.size(); i++) {
                        Event event = events.get(i);
                        Platform.runLater(() -> {
                            showAlertWindow(event);
                            playAlertSound();
                        });
                    }

                    int seconds = 60 - LocalTime.now().getSecond();  // so alert comes at the beginning of the minute
                    Thread.sleep(seconds * 1000);
                }
            }
            catch (Exception e) {
                showClientErrorAlert();
                e.printStackTrace();
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

    // create grid pane nodes array
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

    /** Adding buttons functionality */
    public void addHandlers() {
        addHandlerToDayVBoxes();
        yearBack.setOnAction(e -> yearBackHandler());
        yearForward.setOnAction(e -> yearForwardHandler());
        addEventButton.setOnAction(e -> addEventButtonHandler(LocalDate.now()));
        logoutButton.setOnAction(e -> logoutButtonHandler());
        changeLanguageButton.setOnAction(e -> buttonLanguageSelectHandler(e));
        savePdfButton.setOnAction(e -> {
            try {
                savePdfButtonHandler();
            } catch (Exception ex) {
                showClientErrorAlert();
                ex.printStackTrace();
            }
        });
    }

    /** Button for creating Pdf File. After creating PDF, informing window appears*/
    private void savePdfButtonHandler() throws Exception {
        PdfFile pdfFile = new PdfFile(user, selectedYear, selectedMonth, eventsClient, resourceBundle);
        pdfFile.pdf();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(resourceBundle.getString("pdfAlertTitle"));
        alert.setHeaderText(null);
        alert.setContentText(resourceBundle.getString("pdfAlertContent"));
        alert.showAndWait();
    }

    /** Finding current year and date, initializing labels and lists with these values */
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

        yearLabel.setText(Integer.toString(selectedYear));
        monthLabel.setText(months[selectedMonth - 1]);
        monthsList.getSelectionModel().selectedItemProperty().addListener((observable, oldvalue, newvalue) -> {
            selectedMonth = monthsList.getSelectionModel().getSelectedIndex() + 1;
            newvalue.toLowerCase();
            newvalue = newvalue.substring(0, 1) + newvalue.substring(1).toLowerCase();
            monthLabel.setText(newvalue);
            initializeCalendar();
            showEventsInCalendar();
        });
    }

    public void addHandlerToDayVBoxes(){
        for (int i = 1; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                VBox dayVBox = (VBox) gridPaneNodes[j][i];
                dayVBox.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                    if (!dayVBox.getChildren().isEmpty()) {
                        Label dayLabel = (Label) dayVBox.getChildren().get(0);
                        int day = Integer.parseInt(dayLabel.getText());
                        LocalDate initDate = LocalDate.of(selectedYear, selectedMonth, day);
                        addEventButtonHandler(initDate);
                        e.consume();
                    }
                });
            }
        }
    }

    /** Initialize calendar. Find out when days in months started.
     * Fill the cells of the calendar by numbers of days. */
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
                if (dayCounter > daysInMonth || fieldCounter < firstDayOfMonth) {
                    VBox dayVBox = (VBox) gridPaneNodes[j][i];
                    dayVBox.getStyleClass().clear();
                    dayVBox.getStyleClass().add("extra-day");
                    fieldCounter++;
                    continue;
                }

                if (fieldCounter >= firstDayOfMonth) {
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
        try {
            List<Event> events = eventsClient.getUserEventsByMonth(user.getIdUser(), selectedYear, selectedMonth);
            if(events == null){
                showServerErrorAlert();
                return;
            }
            for(int e = 0; e < events.size(); e++) {
                for (int i = 1; i < 7; i++) {
                    for (int j = 0; j < 7; j++) {
                        VBox dayVBox = (VBox) gridPaneNodes[j][i];
                        if(dayVBox.getChildren().isEmpty()){
                            continue;
                        }

                        Label dayLabel = (Label) dayVBox.getChildren().get(0);
                        int dayNumber = Integer.parseInt(dayLabel.getText());
                        Event event = events.get(e);

                        if(event.getDate().getDayOfMonth() == dayNumber) {
                            Label eventLabel = new Label();
                            String eventLabelText = event.getStarts() + " " + event.getTitle();
                            eventLabel.setText(eventLabelText);
                            eventLabel.setId(Integer.toString(event.getIdEvent()));
                            eventLabel.setPrefWidth(dayVBox.getPrefWidth());
                            eventLabel.getStyleClass().add("event-label");
                            eventLabel.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                                eventLabelHandler(eventLabel);
                                mouseEvent.consume();
                            });

                            dayVBox.getChildren().add(eventLabel);
                            VBox.setMargin(eventLabel, new Insets(0, 0, 0, 5));
                        }
                    }
                }
            }
        } catch (Exception e){
            showClientErrorAlert();
            System.out.println(e.getMessage());
        }
    }

    /** Clear calendar (without number of days and events in calendar cells.)*/
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

    /** When user clicks on the label of event in calendar, the detail of the event shows. */
    public void eventLabelHandler(Label eventLabel){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("fxml/PlanItAddEvent.fxml"));
            PlanItAddEventController planItAddEventController = new PlanItAddEventController(user.getIdUser(),
                    Integer.parseInt(eventLabel.getId()), eventsClient, this);
            loader.setController(planItAddEventController);
            loader.setResources(resourceBundle);

            AnchorPane anchorPane = anchorPane = (AnchorPane) loader.load();
            Scene scene = new Scene(anchorPane);
            scene.getStylesheets().add(getClass().getClassLoader().getResource("css/styles.css").toExternalForm());
            Stage window = new Stage();
            window.setTitle(eventLabel.getText());
            window.setScene(scene);
            window.initModality(Modality.WINDOW_MODAL);
            window.initOwner(ap.getScene().getWindow());
            window.resizableProperty().setValue(false);
            window.show();
        } catch (IOException ex) {
            showClientErrorAlert();
            ex.printStackTrace();
        }
    }

    /** Button addEventButton is used to open "PlanItAddEvent" window.
     * @param initDate current date*/
    public void addEventButtonHandler(LocalDate initDate){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("fxml/PlanItAddEvent.fxml"));
            PlanItAddEventController planItAddEventController = new PlanItAddEventController(user.getIdUser(), initDate,
                    eventsClient, this);
            loader.setController(planItAddEventController);
            loader.setResources(resourceBundle);

            AnchorPane anchorPane = (AnchorPane) loader.load();
            Scene scene = new Scene(anchorPane);
            scene.getStylesheets().add(getClass().getClassLoader().getResource("css/styles.css").toExternalForm());
            Stage window = new Stage();
            window.setScene(scene);
            window.initModality(Modality.WINDOW_MODAL);
            window.initOwner(ap.getScene().getWindow());
            window.resizableProperty().setValue(false);
            window.show();
        } catch (IOException ex) {
            showClientErrorAlert();
            ex.printStackTrace();
        }
    }

    /** Button logoutButton is used for log out the current user. The "PlanItLogin" window appears.*/
    public void logoutButtonHandler() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("fxml/PlanItLogin.fxml"));
            PlanItLoginController planItLoginController = new PlanItLoginController(new UsersClient());
            fxmlLoader.setController(planItLoginController);
            fxmlLoader.setResources(resourceBundle);
            AnchorPane rootPane = (AnchorPane) fxmlLoader.load();
            Scene newScene = new Scene(rootPane);
            newScene.getStylesheets().add(getClass().getClassLoader().getResource("css/styles.css").toExternalForm());
            Stage window = (Stage) ap.getScene().getWindow();
            window.setScene(newScene);
            window.centerOnScreen();
            window.setTitle("PlanIt");
            window.resizableProperty().setValue(false);
            window.show();
        } catch (Exception exception) {
            showClientErrorAlert();
            exception.printStackTrace();
        }
    }

    /** Button for selecting language
     * Opens the "languageSelector" window.*/
    public void buttonLanguageSelectHandler(ActionEvent event){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("fxml/LanguageSelector.fxml"));
            LanguageSelectorController languageSelectorController = new LanguageSelectorController(this);
            fxmlLoader.setController(languageSelectorController);
            AnchorPane anchorPane = (AnchorPane) fxmlLoader.load();
            Scene newScene = new Scene(anchorPane);
            Stage window = new Stage();
            window.setScene(newScene);
            window.centerOnScreen();
            window.resizableProperty().setValue(false);
            window.show();
        } catch (IOException ex) {
            showClientErrorAlert();
            ex.printStackTrace();
        }
    }

    /** Shows notification window with basic information about event.
     * @param event the event to which it is notified */
    public void showAlertWindow(Event event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("fxml/AlertWindow.fxml"));
            AlertWindowController alertWindowController = new AlertWindowController(user, event, eventsClient, this);
            loader.setController(alertWindowController);
            loader.setResources(resourceBundle);

            AnchorPane anchorPane = (AnchorPane) loader.load();
            Scene scene = new Scene(anchorPane);
            Stage window = new Stage();

            Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
            window.setX(primaryScreenBounds.getMinX() + primaryScreenBounds.getWidth() - 450);
            window.setY(primaryScreenBounds.getMinY() + primaryScreenBounds.getHeight() - 330);

            window.setScene(scene);
            window.resizableProperty().setValue(false);
            window.show();
        } catch (IOException ex) {
            showClientErrorAlert();
            ex.printStackTrace();
        }
    }

    public void showServerErrorAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(resourceBundle.getString("serverError"));
        alert.setHeaderText(resourceBundle.getString("errorAlertHeader"));
        alert.setContentText(resourceBundle.getString("errorAlertContext"));
        alert.showAndWait();
    }

    public void showClientErrorAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(resourceBundle.getString("clientError"));
        alert.setHeaderText(resourceBundle.getString("errorAlertHeader"));
        alert.setContentText(resourceBundle.getString("errorAlertContext"));
        alert.showAndWait();
    }
}
