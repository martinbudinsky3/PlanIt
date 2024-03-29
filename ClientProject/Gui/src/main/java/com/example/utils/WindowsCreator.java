package com.example.utils;

import com.example.client.clients.EventsClient;
import com.example.client.clients.UsersClient;
import com.example.client.clients.WeatherClient;
import com.example.model.Event;
import com.example.model.User;
import com.example.gui.controllers.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;


public class WindowsCreator {
    private static WindowsCreator instance;

    private static final Logger logger = LoggerFactory.getLogger(WindowsCreator.class);

    private WindowsCreator() { }

    public static WindowsCreator getInstance() {
        if(instance == null) {
            instance = new WindowsCreator();
        }

        return instance;
    }

    /**
     * When user clicks on the label of event in calendar, the detail of the event shows.
     */
    public void createEventDetailWindow(Long eventId, LocalDate initDate, String title, PlanItMainWindowController planItMainWindowController,
                                        ResourceBundle resourceBundle) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("fxml/PlanItAddEvent.fxml"));
            PlanItAddEventController planItAddEventController = new PlanItAddEventController(eventId, initDate, planItMainWindowController);
            loader.setController(planItAddEventController);
            loader.setResources(resourceBundle);

            AnchorPane anchorPane = (AnchorPane) loader.load();
            Scene scene = new Scene(anchorPane);
            scene.getStylesheets().add(getClass().getClassLoader().getResource("css/styles.css").toExternalForm());
            Stage window = new Stage();
            window.setTitle(title);
            window.setScene(scene);
            window.initModality(Modality.WINDOW_MODAL);
            window.initOwner(planItMainWindowController.getAnchorPane().getScene().getWindow());
            window.resizableProperty().setValue(false);
            window.show();
        } catch (IOException ex) {
            showErrorAlert(resourceBundle);
            logger.error("Error while opening event detail window", ex);
        }
    }

    /**
     * Button addEventButton is used to open "PlanItAddEvent" window.
     *
     * @param initDate current date
     */
    public void createAddEventWindow(LocalDate initDate, PlanItMainWindowController planItMainWindowController,
                                     ResourceBundle resourceBundle, AnchorPane ap) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("fxml/PlanItAddEvent.fxml"));
            PlanItAddEventController planItAddEventController = new PlanItAddEventController(initDate, planItMainWindowController);
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
            logger.error("Error while opening add event window", ex);
        }
    }

    /**
     * Shows notification window with basic information about event.
     *
     * @param event the event to which it is notified
     */
    public void createAlertWindow(Event event, PlanItMainWindowController planItMainWindowController,
                                  ResourceBundle resourceBundle) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("fxml/AlertWindow.fxml"));
            AlertWindowController alertWindowController = new AlertWindowController(event, planItMainWindowController);
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
            logger.error("Error while opening alert window", ex);
        }
    }

    /**
     * Button for canceling registration (buttonCancel)
     * Shows "PlanItLogin" window.
     */
    public void createLoginWindow(ResourceBundle resourceBundle, Stage window) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("fxml/PlanItLogin.fxml"));
            PlanItLoginController planItLoginController = new PlanItLoginController();
            fxmlLoader.setController(planItLoginController);
            fxmlLoader.setResources(resourceBundle);
            AnchorPane rootPane = fxmlLoader.load();
            Scene newScene = new Scene(rootPane);
            newScene.getStylesheets().add(getClass().getClassLoader().getResource("css/styles.css").toExternalForm());
            window.setScene(newScene);
            window.centerOnScreen();
            window.setTitle("PlanIt");
            window.resizableProperty().setValue(false);
            window.show();
        } catch (IOException e) {
            showErrorAlert(resourceBundle);
            logger.error("Error. Can't create login window", e);
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
            logger.error("Error while reloading window", ex);
        }
    }

    public void createMainWindow(ResourceBundle resourceBundle, ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("fxml/PlanItMainWindow.fxml"));
            PlanItMainWindowController planItMainWindowController = new PlanItMainWindowController();
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
            logger.error("Error while opening main window", ex);
        }
    }

    /**
     * Registration button.
     * Opens the "PlanItRegistration window."
     */
    public void createRegistrationWindow(ResourceBundle resourceBundle, ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("fxml/PlanItRegistration.fxml"));
            PlanItRegistrationController planItRegistrationController = new PlanItRegistrationController();
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
            logger.error("Error while opening registration window", ex);
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
        } catch (IOException ex) {
            showErrorAlert(resourceBundle);
            logger.error("Error while opening language selector window", ex);
        }
    }

    public Optional<ButtonType> showInfoAlert(String title, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(contentText);

        return alert.showAndWait();
    }

    public void showErrorAlert(ResourceBundle resourceBundle) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(resourceBundle.getString("error"));
        alert.setHeaderText(resourceBundle.getString("errorAlertHeader"));
        alert.setContentText(resourceBundle.getString("errorAlertContext"));
        alert.showAndWait();
    }

    public void showErrorAlert(String headerText, ResourceBundle resourceBundle) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(resourceBundle.getString("error"));
        alert.setHeaderText(headerText);
        alert.setContentText(resourceBundle.getString("errorAlertContext"));
        alert.showAndWait();
    }
}
