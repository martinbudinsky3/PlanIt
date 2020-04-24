package com.example.gui.controllers;

import com.example.client.clients.EventsClient;
import com.example.client.clients.UsersClient;
import com.example.client.model.Event;
import com.example.client.model.User;
import com.example.gui.GuiApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.util.*;

public class PlanItMainWindowController implements Initializable {
    @FXML
    private GridPane calendar;
    @FXML
    private Button addEventButton;
    @FXML
    private Label yearLabel;
    @FXML
    private Label monthLabel;
    @FXML
    private Button yearBack;
    @FXML
    private Button yearForward;
    @FXML
    private ListView<String> monthsList;

    int selectedYear;
    int selectedMonth;
    Node[][] gridPaneNodes;
    private final EventsClient eventsClient;
    private final UsersClient usersClient;
    private final User user;

    public PlanItMainWindowController(EventsClient eventsClient, UsersClient usersClient, User user) {
        this.eventsClient = eventsClient;
        this.usersClient = usersClient;
        this.user = user;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createGridPaneNodes();
        addHandlers();
        initializeMonthsAndYear();
        initializeCalendar();
        showEventsInCalendar();
    }

    // create grid pane nodes array
    public void createGridPaneNodes(){
        gridPaneNodes = new Node[7][6];
        for (Node child : calendar.getChildren()) {
            Integer column = calendar.getColumnIndex(child);
            Integer row = calendar.getRowIndex(child);
            if(row != null) {
                if(column == null){
                    column = 0;
                }
                gridPaneNodes[column][row] = child;
            }
        }
    }

    public void addHandlers() {
        yearBack.setOnAction(e -> yearBackHandler());
        yearForward.setOnAction(e -> yearForwardHandler());
        addEventButton.setOnAction(e -> addEventButtonHandler(e));
    }

    public void initializeMonthsAndYear() {
        // find out current year and month that will be displayed
        LocalDate today = LocalDate.now();
        selectedYear = today.getYear();
        selectedMonth = today.getMonth().getValue();

        // initialize monthsList
        // TO DO - multilanguage
        String months[] = new DateFormatSymbols().getMonths();
        for (int i = 0; i < 12; i++){
            monthsList.getItems().add(months[i].toUpperCase());
        }

        yearLabel.setText(Integer.toString(selectedYear));
        monthLabel.setText(months[selectedMonth - 1]);
        monthsList.getSelectionModel().selectedItemProperty().addListener((observable, oldvalue, newvalue) -> {
            selectedMonth = monthsList.getSelectionModel().getSelectedIndex() + 1;
            monthLabel.setText(newvalue);
            initializeCalendar();
            showEventsInCalendar();
        });
    }

    public void initializeCalendar() {
        // find out when does month start and end
        GregorianCalendar gregorianCalendar = new GregorianCalendar(selectedYear, selectedMonth - 1, 1);
        int firstDayOfMonth = gregorianCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        int daysInMonth = gregorianCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        clearCalendar();

        // add day number labels to calendar fields
        int fieldCounter = 1;
        int dayCounter = 1;
        for (int i = 1; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (dayCounter > daysInMonth) {
                    break;
                }

                if (fieldCounter >= firstDayOfMonth) {
                    VBox dayVBox = (VBox) gridPaneNodes[j][i];
//                    GridPane.setVgrow(dayVBox, Priority.ALWAYS);  // TO DO - not working yet
                    dayVBox.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                        // TO DO - open add event modal window
                    });

                    Label dayLabel = new Label(Integer.toString(dayCounter));
                    dayVBox.getChildren().add(dayLabel);
                    VBox.setMargin(dayLabel, new Insets(5, 0, 5, 5));
                    dayCounter++;
                }

                fieldCounter++;
            }
        }
    }

    public void showEventsInCalendar() {

        try {
            List<Event> events = eventsClient.getUserEventsByMonth(user.getIdUser(), selectedYear, selectedMonth); // hardcoded userId
            for(int e = 0; e < events.size(); e++) {
                for (int i = 1; i < 6; i++) {
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
                            eventLabel.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                                // TO DO - open event detail window and pass its event id from event label id

                                FXMLLoader loader = new FXMLLoader();
                                loader.setLocation(getClass().getClassLoader().getResource("fxml/PlanItEventDetail.fxml"));
                                PlanItEventDetailController planItEventDetailController = new PlanItEventDetailController(eventsClient, Integer.parseInt(eventLabel.getId()), user.getIdUser());
                                loader.setController(planItEventDetailController);

                                AnchorPane anchorPane = null;
                                try {
                                    anchorPane = (AnchorPane) loader.load();
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                                Scene scene = new Scene(anchorPane);
                                Stage window = new Stage();
                                window.setScene(scene);
                                window.show();

                            });

                            dayVBox.getChildren().add(eventLabel);
                            VBox.setMargin(eventLabel, new Insets(0, 0, 0, 5));
                        }
                    }
                }
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void clearCalendar(){
        for(int i = 1; i < 6; i++) {
            for(int j = 0; j < 7; j++) {
                VBox vBox = (VBox) gridPaneNodes[j][i];
                if(vBox.getChildren().isEmpty()){
                    continue;
                }
                vBox.getChildren().clear();
                System.out.println();
            }
        }
    }

    public void yearBackHandler() {
        selectedYear -= 1;
        yearLabel.setText(Integer.toString(selectedYear));
        initializeCalendar();
        showEventsInCalendar();
    }

    public void yearForwardHandler() {
        selectedYear += 1;
        yearLabel.setText(Integer.toString(selectedYear));
        initializeCalendar();
        showEventsInCalendar();
    }

    public void addEventButtonHandler(ActionEvent event){

    }
}
