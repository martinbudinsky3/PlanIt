package com.example.gui.controllers;

import com.example.client.clients.EventsClient;
import com.example.client.model.Event;
import com.example.client.model.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PlanItEventDetailController implements Initializable {

    @FXML
    private Label labelEnds;

    @FXML
    private Label labelStarts;

    @FXML
    private Label labelTitle;

    @FXML
    private Label labelLocation;

    @FXML
    private Label labelAlert;

    @FXML
    private Label labelDate;

    private EventsClient eventsClient;
    private Event event;
    private int idEvent;
    private int idUser;

    public PlanItEventDetailController(EventsClient eventsClient, int idEvent, int idUser) {
        this.eventsClient = eventsClient;
        this.idEvent = idEvent;
        this.idUser = idUser;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            initializeLabels();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initializeLabels() throws Exception {
        event = eventsClient.getEventByIdUserAndIdEvent(idUser,idEvent);
        labelTitle.setText(event.getTitle());
        labelLocation.setText(event.getLocation());
        labelDate.setText(String.valueOf(event.getDate()));
        labelStarts.setText(String.valueOf(event.getStarts()));
        labelEnds.setText(String.valueOf(event.getEnds()));
        labelAlert.setText(String.valueOf(event.getAlert()));
    }
}
