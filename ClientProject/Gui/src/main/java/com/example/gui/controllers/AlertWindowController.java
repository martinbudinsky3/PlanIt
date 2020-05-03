package com.example.gui.controllers;

import com.example.client.clients.EventsClient;
import com.example.client.model.Event;
import com.example.client.model.User;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AlertWindowController implements Initializable {
    ResourceBundle resourceBundle;
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
    private User user;
    private Event event;
    private EventsClient eventsClient;
    private PlanItMainWindowController planItMainWindowController;

    public AlertWindowController(User user, Event event, EventsClient eventsClient,
                                 PlanItMainWindowController planItMainWindowController) {
        this.user = user;
        this.event = event;
        this.eventsClient = eventsClient;
        this.planItMainWindowController = planItMainWindowController;
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
        postponeTimeChoice.setValue("5 min");
    }

    public void addHandlers() {
        ap.setOnMouseClicked(e -> {
            showEventDetailWindow();
            e.consume();
        });

        postponeButton.setOnAction(e -> {
            postponeButtonHandler();
            e.consume();
        });

    }

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

    private void showEventDetailWindow() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("fxml/PlanItAddEvent.fxml"));
        PlanItAddEventController planItAddEventController = new PlanItAddEventController(user.getIdUser(),
                event.getIdEvent(), eventsClient, planItMainWindowController);
        loader.setController(planItAddEventController);
        loader.setResources(resourceBundle);

        AnchorPane anchorPane = null;
        try {
            anchorPane = (AnchorPane) loader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Scene scene = new Scene(anchorPane);
        Stage window = new Stage();
        window.setTitle(event.getStarts().format(DateTimeFormatter.ofPattern("HH:mm")) + " " + event.getTitle());
        window.setScene(scene);
        window.show();

        Stage stage = (Stage) ap.getScene().getWindow();
        stage.close();
    }

    private void postponeButtonHandler() {
        String postponeTime = postponeTimeChoice.getValue();
        LocalTime previousAlertTime = event.getAlert();
        LocalTime newAlertTime = null;
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

        if (newAlertTime.isBefore(previousAlertTime)) {
            event.setAlertDate(event.getAlertDate().plusDays(1));
        }

        try {
            eventsClient.updateEvent(event, event.getIdEvent());
            Stage stage = (Stage) ap.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
