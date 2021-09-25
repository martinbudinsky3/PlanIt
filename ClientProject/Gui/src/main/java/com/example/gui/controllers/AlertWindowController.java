package com.example.gui.controllers;

import com.example.client.EventsClient;
import com.example.model.Event;
import com.example.model.User;
import com.example.utils.WindowsCreator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
            String title = event.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")) + " " + event.getTitle();
            windowsCreator.createEventDetailWindow(event, title, user, eventsClient, planItMainWindowController,
                    resourceBundle);
            Stage stage = (Stage) ap.getScene().getWindow();
            stage.close();
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
        LocalDate startsDate = event.getStartDate();
        LocalDate endsDate = event.getEndDate();
        String timeText;

        if (startsDate.equals(endsDate)) {
            DateTimeFormatter dtfTime = DateTimeFormatter.ofPattern("HH:mm");
            timeText = event.getStartTime().format(dtfTime) + "-" + event.getEndTime().format(dtfTime);
        } else {
            DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("dd.MM");
            timeText = event.getStartDate().format(dtfDate) + "-" + event.getEndDate().format(dtfDate);
        }

        titleLabel.setText(title);
        timeLabel.setText(timeText);
    }

    /** A handler for postpone button with functionality for the user to postpone the alert time. */
    private void postponeButtonHandler() {
        String postponeTime = postponeTimeChoice.getValue();
        LocalTime previousAlertTime = event.getAlertTime();
        LocalTime newAlertTime = null;
        // postpone alert time according to user choice
        if (postponeTime.equals(resourceBundle.getString("5minutes"))) {
            newAlertTime = event.getAlertTime().plusMinutes(5);
            event.setAlertTime(newAlertTime);
        } else if (postponeTime.equals(resourceBundle.getString("30minutes"))) {
            newAlertTime = event.getAlertTime().plusMinutes(30);
            event.setAlertTime(newAlertTime);
        } else if (postponeTime.equals(resourceBundle.getString("1hour"))) {
            newAlertTime = event.getAlertTime().plusHours(1);
            event.setAlertTime(newAlertTime);
        } else if (postponeTime.equals(resourceBundle.getString("6hours"))) {
            newAlertTime = event.getAlertTime().plusHours(6);
            event.setAlertTime(newAlertTime);
        }

        // if postponed alert time oversteps midnight, set alert date to next day
        if (newAlertTime.isBefore(previousAlertTime)) {
            event.setAlertDate(event.getAlertDate().plusDays(1));
        }

        // updating event - new alert time
        boolean success = eventsClient.updateEvent(event, event.getId(), resourceBundle);
        if(success){
            Stage stage = (Stage) ap.getScene().getWindow();
            stage.close();
        }
    }
}
