package com.example.gui.controllers;

import com.example.client.clients.EventsClient;
import com.example.client.model.Event;
import com.example.client.model.User;
import com.example.gui.utils.WindowsCreator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/** Controller for "AlertWindow.fxml" */
public class AlertWindowController implements Initializable {
    private final WindowsCreator windowsCreator;

    @FXML
    private AnchorPane ap;
    @FXML
    private Button postponeButton;
    @FXML
    private ChoiceBox<String> postponeTimeChoice;
    @FXML
    private Label titleLabel;
    @FXML
    private Label timeLabel;

    private ResourceBundle resourceBundle;
    private User user;
    private Event event;
    private EventsClient eventsClient;
    private PlanItMainWindowController planItMainWindowController;

    /** Constructor */
    public AlertWindowController(User user, Event event, EventsClient eventsClient,
                                 PlanItMainWindowController planItMainWindowController, WindowsCreator windowsCreator) {
        this.user = user;
        this.event = event;
        this.eventsClient = eventsClient;
        this.planItMainWindowController = planItMainWindowController;
        this.windowsCreator = windowsCreator;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        addHandlers();
        showDetail();

        postponeTimeChoice.getItems().add(resourceBundle.getString("5minutes"));
        postponeTimeChoice.getItems().add(resourceBundle.getString("30minutes"));
        postponeTimeChoice.getItems().add(resourceBundle.getString("1hour"));
        postponeTimeChoice.getItems().add(resourceBundle.getString("6hours"));
        postponeTimeChoice.setValue(resourceBundle.getString("5minutes"));
    }

    /** Adding handlers to click on the Button (postponeButton) and to click on the AnchorPane (ap) */
    public void addHandlers() {
        ap.setOnMouseClicked(e -> {
            String title = event.getStarts().format(DateTimeFormatter.ofPattern("HH:mm")) + " " + event.getTitle();
            windowsCreator.createEventDetailWindow(event.getIdEvent(), title, user, eventsClient, planItMainWindowController,
                    resourceBundle, ap);
            e.consume();
        });

        postponeButton.setOnAction(e -> {
            postponeButtonHandler();
            e.consume();
        });

    }

    /** Showing Alert Window with basic information of given event (title, starts and ends time).
     * This window is shown at the bottom right of the screen when it's time to notify the event. */
    public void showDetail() {
        String title = event.getTitle();
        LocalDate startsDate = event.getDate();
        LocalDate endsDate = event.getEndsDate();
        String timeText;

        if (startsDate.equals(endsDate)) {
            DateTimeFormatter dtfTime = DateTimeFormatter.ofPattern("HH:mm");
            timeText = event.getStarts().format(dtfTime) + "-" + event.getEnds().format(dtfTime);
        } else {
            DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("dd.MM");
            timeText = event.getDate().format(dtfDate) + "-" + event.getEndsDate().format(dtfDate);
        }

        titleLabel.setText(title);
        timeLabel.setText(timeText);
    }

    /** A handler for postpone button with functionality for the user to postpone the alert time. */
    private void postponeButtonHandler() {
        String postponeTime = postponeTimeChoice.getValue();
        LocalTime previousAlertTime = event.getAlert();
        LocalTime newAlertTime = null;
        // postpone alert time according to user choice
        if (postponeTime.equals(resourceBundle.getString("5minutes"))) {
            newAlertTime = event.getAlert().plusMinutes(5);
            event.setAlert(newAlertTime);
        } else if (postponeTime.equals(resourceBundle.getString("30minutes"))) {
            newAlertTime = event.getAlert().plusMinutes(30);
            event.setAlert(newAlertTime);
        } else if (postponeTime.equals(resourceBundle.getString("1hour"))) {
            newAlertTime = event.getAlert().plusHours(1);
            event.setAlert(newAlertTime);
        } else if (postponeTime.equals(resourceBundle.getString("6hours"))) {
            newAlertTime = event.getAlert().plusHours(6);
            event.setAlert(newAlertTime);
        }

        // if postponed alert time oversteps midnight, set alert date to next day
        if (newAlertTime.isBefore(previousAlertTime)) {
            event.setAlertDate(event.getAlertDate().plusDays(1));
        }

        // updating event - new alert time
        boolean success = eventsClient.updateEvent(event, event.getIdEvent(), resourceBundle);
        if(success){
            Stage stage = (Stage) ap.getScene().getWindow();
            stage.close();
        }
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
