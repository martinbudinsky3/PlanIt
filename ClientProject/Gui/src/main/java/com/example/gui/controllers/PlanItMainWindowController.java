package com.example.gui.controllers;

import com.example.client.clients.EventsClient;
import com.example.client.clients.UsersClient;
import com.example.client.model.Event;
import com.example.client.model.User;
import com.example.gui.PdfFile;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

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
    }


    public void setSelectedYear(int selectedYear) {
        this.selectedYear = selectedYear;
        yearLabel.setText(Integer.toString(selectedYear));
    }

    public void setSelectedMonth(int selectedMonth) {
        this.selectedMonth = selectedMonth;
        monthLabel.setText(months[selectedMonth - 1]);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        createGridPaneNodes();
        addHandlers();
        initializeMonthsAndYear();
        initializeCalendar();
        showEventsInCalendar();
    }

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

    public void addHandlers() {
        yearBack.setOnAction(e -> yearBackHandler());
        yearForward.setOnAction(e -> yearForwardHandler());
        addEventButton.setOnAction(e -> addEventButtonHandler(LocalDate.now()));
        addHandlerToDayVBoxes();
        changeLanguageButton.setOnAction(e -> buttonLanguageSelectHandler(e));
        savePdfButton.setOnAction(e -> {
            try {
                savePdfButtonHandler();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private void savePdfButtonHandler() throws Exception {
        PdfFile pdfFile = new PdfFile(user, selectedYear, selectedMonth, eventsClient, resourceBundle);
        pdfFile.pdf();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(resourceBundle.getString("pdfAlertTitle"));
        alert.setHeaderText(null);
        alert.setContentText(resourceBundle.getString("pdfAlertContent"));
        alert.showAndWait();
    }

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

    public void initializeCalendar() {
        // find out when does month start and end
        GregorianCalendar gregorianCalendar = new GregorianCalendar(selectedYear, selectedMonth - 1, 1);
        int firstDayOfMonth = gregorianCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        if(firstDayOfMonth == 0){
            firstDayOfMonth = 7;
        }
        int daysInMonth = gregorianCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        GregorianCalendar gregorianCalendar2 = new GregorianCalendar(selectedYear, Calendar.NOVEMBER, 1);

        clearCalendar();

        // add day number labels to calendar fields
        int fieldCounter = 1;
        int dayCounter = 1;
        for (int i = 1; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if (dayCounter > daysInMonth) {
                    break;
                }

                if (fieldCounter >= firstDayOfMonth) {
                    VBox dayVBox = (VBox) gridPaneNodes[j][i];
//                    GridPane.setVgrow(dayVBox, Priority.ALWAYS);  // TO DO - not working yet

                    Label dayLabel = new Label(Integer.toString(dayCounter));
                    dayVBox.getChildren().add(dayLabel);
                    VBox.setMargin(dayLabel, new Insets(5, 0, 5, 5));
                    dayCounter++;
                }

                fieldCounter++;
            }
        }

//        for(int j = 0; j < 7; j++){
//            Pane dayNamePane = (Pane) gridPaneNodes[j][0];
//            GridPane.setVgrow(dayNamePane, Priority.ALWAYS);
//        }
    }

    public void showEventsInCalendar() {
        try {
            List<Event> events = eventsClient.getUserEventsByMonth(user.getIdUser(), selectedYear, selectedMonth);
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
                            eventLabel.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {

                                FXMLLoader loader = new FXMLLoader();
                                loader.setLocation(getClass().getClassLoader().getResource("fxml/PlanItAddEvent.fxml"));
                                PlanItAddEventController planItAddEventController = new PlanItAddEventController(user.getIdUser(),
                                        Integer.parseInt(eventLabel.getId()), eventsClient, this);
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
                                window.setTitle(eventLabel.getText());
                                window.setScene(scene);
                                window.initModality(Modality.WINDOW_MODAL);
                                window.initOwner(ap.getScene().getWindow());
                                window.show();
                                mouseEvent.consume();
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
        for(int i = 1; i < 7; i++) {
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

    public void addEventButtonHandler(LocalDate initDate){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("fxml/PlanItAddEvent.fxml"));
        PlanItAddEventController planItAddEventController = new PlanItAddEventController(user.getIdUser(), initDate,
                eventsClient, this);
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
        window.setScene(scene);
        window.initModality(Modality.WINDOW_MODAL);
        window.initOwner(ap.getScene().getWindow());
        window.show();
    }

    void buttonLanguageSelectHandler(ActionEvent event){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getClassLoader().getResource("fxml/LanguageSelector.fxml"));
        LanguageSelectorController languageSelectorController = new LanguageSelectorController(this);
        fxmlLoader.setController(languageSelectorController);
        AnchorPane anchorPane = null;
        try {
            anchorPane = (AnchorPane) fxmlLoader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Scene newScene = new Scene(anchorPane);
        Stage window = new Stage();
        window.setScene(newScene);
        window.centerOnScreen();
        window.show();
    }
}
