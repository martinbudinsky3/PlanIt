package com.example.gui.controllers;

import com.example.client.clients.UsersClient;
import com.example.client.model.User;
import com.example.gui.utils.WindowsCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * Controller for "PlanItLogin.fxml"
 */
public class PlanItLoginController implements Initializable, LanguageChangeWindow {

    private static final Logger logger = LoggerFactory.getLogger(PlanItLoginController.class);

    private final UsersClient usersClient;
    private final WindowsCreator windowsCreator;

    @FXML
    private AnchorPane ap;
    @FXML
    private Button buttonLogin;
    @FXML
    private TextField textfieldName;
    @FXML
    private PasswordField passwordfieldPassword;
    @FXML
    private Button buttonRegister;
    @FXML
    private Button changeLanguageButton;

    private User user;
    private ResourceBundle resourceBundle;

    public PlanItLoginController(UsersClient usersClient, WindowsCreator windowsCreator) {
        this.usersClient = usersClient;
        this.windowsCreator = windowsCreator;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resourceBundle = resources;
        addHandlers();
    }

    /**
     * Reloading window to change to just selected language.
     */
    @Override
    public void reload(ResourceBundle bundle) {
        windowsCreator.reload(ap, bundle, "fxml/PlanItLogin.fxml",this);
    }

    /**
     * Adding functionality to the buttons.
     */
    public void addHandlers() {
        buttonLogin.setOnAction(e -> {
            buttonLoginHandler(e);
        });
        buttonRegister.setOnAction(e -> {
            windowsCreator.createRegistrationWindow(usersClient, resourceBundle, e);
        });
        changeLanguageButton.setOnAction(e -> {
            windowsCreator.createLanguageSelectorWindow(this, resourceBundle);
        });
    }

    /**
     * Login button.
     * Getting username and password from the TextFields. If the entered data is valid, user is logged in and the "PlanItMainWindow" opens.
     */
    void buttonLoginHandler(ActionEvent event) {
        if (textfieldName.getText().isEmpty() || passwordfieldPassword.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(resourceBundle.getString("loginEmptyAlertTitle"));
            alert.setHeaderText(null);
            alert.setContentText(resourceBundle.getString("loginAlertContent"));
            alert.showAndWait();
        } else {
            try {
                user = usersClient.getUserByUserNameAndUserPassword(textfieldName.getText(), passwordfieldPassword.getText());
                windowsCreator.createMainWindow(resourceBundle, usersClient, user, event);
            } catch (JsonProcessingException | ResourceAccessException | HttpStatusCodeException ex) {
                windowsCreator.showErrorAlert(resourceBundle);
                if(ex instanceof JsonProcessingException) {
                    logger.error("Error logging in user " + textfieldName.getText(), ex);
                } else if(ex instanceof ResourceAccessException) {
                    logger.error("Error while connecting to server", ex);
                } else {
                    if(((HttpStatusCodeException)ex).getRawStatusCode() != 500) {
                        logger.error("Error logging in user " + textfieldName.getText() + ". HTTP Status: " +
                                ((HttpStatusCodeException)ex).getRawStatusCode(), ex);
                        windowsCreator.showErrorAlert(resourceBundle);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle(resourceBundle.getString("loginWrongAlertTitle"));
                        alert.setHeaderText(null);
                        alert.setContentText(resourceBundle.getString("loginAlertContent"));
                        alert.showAndWait();
                    }
                }
            }
        }
    }
}
