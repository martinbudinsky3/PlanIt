package com.example.gui.utils;

import com.example.client.clients.EventsClient;
import com.example.client.clients.UsersClient;
import com.example.client.model.Event;
import com.example.client.model.User;
import com.example.gui.controllers.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ResourceBundle;


public class WindowsCreator {

    /** When user clicks on the label of event in calendar, the detail of the event shows. */
    public void createEventDetailWindow(int eventId, String title, User user, EventsClient eventsClient,
                                        PlanItMainWindowController planItMainWindowController, ResourceBundle resourceBundle,
                                        AnchorPane ap) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("fxml/PlanItAddEvent.fxml"));
            PlanItAddEventController planItAddEventController = new PlanItAddEventController(user.getIdUser(),
                    eventId, eventsClient, planItMainWindowController);
            loader.setController(planItAddEventController);
            loader.setResources(resourceBundle);

            AnchorPane anchorPane = anchorPane = (AnchorPane) loader.load();
            Scene scene = new Scene(anchorPane);
            scene.getStylesheets().add(getClass().getClassLoader().getResource("css/styles.css").toExternalForm());
            Stage window = new Stage();
            window.setTitle(title);
            window.setScene(scene);
            window.initModality(Modality.WINDOW_MODAL);
            window.initOwner(ap.getScene().getWindow());
            window.resizableProperty().setValue(false);
            window.show();
        } catch (IOException ex) {
            showErrorAlert(resourceBundle);
            ex.printStackTrace(); // TODO logging
        }
    }

    /** Button addEventButton is used to open "PlanItAddEvent" window.
     * @param initDate current date*/
    public void createAddEventWindow(User user, LocalDate initDate, EventsClient eventsClient,
                                     PlanItMainWindowController planItMainWindowController, ResourceBundle resourceBundle,
                                     AnchorPane ap) {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("fxml/PlanItAddEvent.fxml"));
            PlanItAddEventController planItAddEventController = new PlanItAddEventController(user.getIdUser(), initDate,
                    eventsClient, planItMainWindowController);
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
            showErrorAlert(resourceBundle);
            ex.printStackTrace(); // TODO logging
        }
    }

    /** Shows notification window with basic information about event.
     * @param event the event to which it is notified */
    public void createAlertWindow(User user, Event event, EventsClient eventsClient,
                                  PlanItMainWindowController planItMainWindowController, ResourceBundle resourceBundle) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("fxml/AlertWindow.fxml"));
            AlertWindowController alertWindowController = new AlertWindowController(user, event, eventsClient,
                    planItMainWindowController, this);
            loader.setController(alertWindowController);
            loader.setResources(resourceBundle);

            AnchorPane anchorPane = (AnchorPane) loader.load();
            Scene scene = new Scene(anchorPane);
            Stage window = new Stage();

            // so it shows in right bottom corner
            Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
            window.setX(primaryScreenBounds.getMinX() + primaryScreenBounds.getWidth() - 450);
            window.setY(primaryScreenBounds.getMinY() + primaryScreenBounds.getHeight() - 330);

            window.setScene(scene);
            window.resizableProperty().setValue(false);
            window.show();
        } catch (IOException ex) {
            showErrorAlert(resourceBundle);
            ex.printStackTrace(); // TODO logging
        }
    }

    /**
     * Button for canceling registration (buttonCancel)
     * Shows "PlanItLogin" window.
     */
    public void createLoginWindow(ResourceBundle resourceBundle, AnchorPane ap) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("fxml/PlanItLogin.fxml"));
            PlanItLoginController planItLoginController = new PlanItLoginController(new UsersClient(), new WindowsCreator());
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
        } catch (IOException ex) {
            showErrorAlert(resourceBundle);
            ex.printStackTrace(); // TODO logging
        }
    }

    public void reload(AnchorPane ap, ResourceBundle resourceBundle, String fxmlFilePath, LanguageChangeWindow languageChangeWindow) {
        try {
            Scene scene = ap.getScene();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource(fxmlFilePath));
            fxmlLoader.setController(languageChangeWindow);
            fxmlLoader.setResources(resourceBundle);
            AnchorPane rootPane = (AnchorPane) fxmlLoader.load();
            scene.setRoot(rootPane);

        } catch (IOException ex) {
            showErrorAlert(resourceBundle);
            ex.printStackTrace();
        }
    }

    public void createMainWindow(ResourceBundle resourceBundle, UsersClient usersClient, User user, ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("fxml/PlanItMainWindow.fxml"));
            PlanItMainWindowController planItMainWindowController = new PlanItMainWindowController(new EventsClient(),
                    usersClient, user, this);
            fxmlLoader.setController(planItMainWindowController);
            fxmlLoader.setResources(resourceBundle);
            AnchorPane rootPane = (AnchorPane) fxmlLoader.load();
            Scene newScene = new Scene(rootPane);
            newScene.getStylesheets().add(getClass().getClassLoader().getResource("css/styles.css").toExternalForm());
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(newScene);
            window.centerOnScreen();
            window.resizableProperty().setValue(false);
            window.show();
        } catch (IOException ex) {
            showErrorAlert(resourceBundle);
            ex.printStackTrace();
        }
    }

    /**
     * Registration button.
     * Opens the "PlanItRegistration window."
     */
    public void createRegistrationWindow(UsersClient usersClient, ResourceBundle resourceBundle, ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("fxml/PlanItRegistration.fxml"));
            PlanItRegistrationController planItRegistrationController = new PlanItRegistrationController(usersClient, this);
            fxmlLoader.setController(planItRegistrationController);
            fxmlLoader.setResources(resourceBundle);
            AnchorPane rootPane = (AnchorPane) fxmlLoader.load();
            Scene newScene = new Scene(rootPane);
            newScene.getStylesheets().add(getClass().getClassLoader().getResource("css/styles.css").toExternalForm());
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(newScene);
            window.centerOnScreen();
            window.resizableProperty().setValue(false);
            window.show();
        } catch (IOException ex) {
            showErrorAlert(resourceBundle);
            ex.printStackTrace();
        }
    }

    /**
     * Button for selecting language
     * Opens the "languageSelector" window.
     */
    public void createLanguageSelectorWindow(LanguageChangeWindow languageChangeWindow, ResourceBundle resourceBundle) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("fxml/LanguageSelector.fxml"));
            LanguageSelectorController languageSelectorController = new LanguageSelectorController(languageChangeWindow);
            fxmlLoader.setController(languageSelectorController);
            AnchorPane rootPane = (AnchorPane) fxmlLoader.load();
            Scene newScene = new Scene(rootPane);
            Stage window = new Stage();
            window.setScene(newScene);
            window.centerOnScreen();
            window.resizableProperty().setValue(false);
            window.show();
        }
        catch (IOException ex) {
            showErrorAlert(resourceBundle);
            ex.printStackTrace();
        }
    }

    public void showErrorAlert(ResourceBundle resourceBundle){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(resourceBundle.getString("error"));
        alert.setHeaderText(resourceBundle.getString("errorAlertHeader"));
        alert.setContentText(resourceBundle.getString("errorAlertContext"));
        alert.showAndWait();
    }
}
