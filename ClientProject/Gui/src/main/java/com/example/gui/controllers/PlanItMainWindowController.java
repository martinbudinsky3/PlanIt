package com.example.gui.controllers;

import com.example.client.clients.EventsClient;
import com.example.client.model.Event;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
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
    private final EventsClient eventsClient;

    public PlanItMainWindowController(EventsClient eventsClient) {
        this.eventsClient = eventsClient;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeMonthsAndYear();
        initializeCalendar();
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
        monthsList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>()
        {
            public void changed(ObservableValue<? extends String> observable, final String oldvalue, final String newvalue) {
                selectedMonth = monthsList.getSelectionModel().getSelectedIndex();
                monthLabel.setText(newvalue);
                showEventsInCalendar();
            }});
    }

    public Node[][] getGridPaneNodes(){
        Node[][] gridPaneNodes = new Node[7][6];
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

        return gridPaneNodes;
    }

    public void initializeCalendar() {
        Node[][] gridPaneNodes = getGridPaneNodes();

        GregorianCalendar gregorianCalendar = new GregorianCalendar(selectedYear, selectedMonth - 1, 1);
        int firstDayOfMonth = gregorianCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        int daysInMonth = gregorianCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);


        int fieldCounter = 1;
        int dayCounter = 1;
        for (int i = 1; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (dayCounter > daysInMonth) {
                    break;
                }

                if (fieldCounter >= firstDayOfMonth) {
                    VBox dayVBox = (VBox) gridPaneNodes[j][i];
                    dayVBox.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                        // TO DO - open add event modal window
                    });

                    Label dayLabel = new Label(Integer.toString(dayCounter));
                    dayVBox.getChildren().add(dayLabel);
                    VBox.setMargin(dayLabel, new Insets(5, 0, 0, 5));
                    dayCounter++;
                }

                fieldCounter++;
            }
        }
    }

    public void showEventsInCalendar() {
        try {
            List<Event> events = eventsClient.getUserEvents(1);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        // TO DO
    }
}
