package com.example.gui.controllers;

import com.example.client.UsersClient;
import com.example.model.User;
import com.example.utils.WindowsCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for "PlanItRegistration.fxml"
 */
public class PlanItRegistrationController implements Initializable {
    private static final Logger logger = LoggerFactory.getLogger(PlanItRegistrationController.class);

    private final UsersClient usersClient;
    private final WindowsCreator windowsCreator;

    @FXML
    private AnchorPane ap;
    @FXML
    private TextField textfieldLastName;
    @FXML
    private TextField textfieldUserName;
    @FXML
    private TextField textfieldFirstName;
    @FXML
    private PasswordField textfieldUserpassword;
    @FXML
    private Button buttonRegister;
    @FXML
    private Button buttonCancel;

    private User user;
    private ResourceBundle resourceBundle;

    public PlanItRegistrationController(UsersClient usersClient, WindowsCreator windowsCreator) {
        this.usersClient = usersClient;
        this.windowsCreator = windowsCreator;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resourceBundle = resources;
        addHandlers();
    }

    /**
     * Initializing buttons functionality.
     */
    public void addHandlers() {
        buttonRegister.setOnAction(e -> {
            buttonRegisterHandler(e);
        });
        buttonCancel.setOnAction(e -> {
            try {
                windowsCreator.createLoginWindow(resourceBundle, (Stage) ap.getScene().getWindow(), usersClient);
            } catch (IOException ex) {
                windowsCreator.showErrorAlert(resourceBundle);
                logger.error("Error while opening login window", ex);
            }
        });
    }


    /**
     * Button for registration (buttonRegister)
     * Getting first name, last name, username and password from TextFields.
     * If there is no user with the same username and password, new user is registered and "PlanItMainWindow" window shows (his calendar)
     */
    private void buttonRegisterHandler(ActionEvent event) {

        if (textfieldFirstName.getText().isEmpty() || textfieldLastName.getText().isEmpty() || textfieldUserName.getText().isEmpty() || textfieldUserpassword.getText().isEmpty()) {
            windowsCreator.showInfoAlert(resourceBundle.getString("loginEmptyAlertTitle"),
                    resourceBundle.getString("registrationEmptyAlertContent"));
        } else {
            String firstName = textfieldFirstName.getText();
            String lastName = textfieldLastName.getText();
            String userName = textfieldUserName.getText();
            String userPassword = textfieldUserpassword.getText();

            User user = new User(firstName, lastName, userName, userPassword);

            try {
                usersClient.register(user);
                windowsCreator.showInfoAlert("Registration successful", "Continue with login");
            } catch (JsonProcessingException | ResourceAccessException | HttpStatusCodeException ex) {
                if(ex instanceof JsonProcessingException) {
                    windowsCreator.showErrorAlert(resourceBundle);
                    logger.error("Error inserting new user.Username: " + user.getUsername() + " First name: " + user.getFirstName() +
                            ", last name: " + user.getLastName(), ex);
                } else if(ex instanceof ResourceAccessException) {
                    windowsCreator.showErrorAlert(resourceBundle);
                    logger.error("Error while connecting to server", ex);
                } else {
                    if(((HttpStatusCodeException)ex).getRawStatusCode() == 412) {
                        windowsCreator.showInfoAlert(resourceBundle.getString("registrationExistsAlertTitle"),
                                resourceBundle.getString("registrationExistsAlertContent"));
                    } else {
                        windowsCreator.showErrorAlert(resourceBundle);
                        logger.error("Error inserting new user.Username: " + user.getUsername() + " First name: " + user.getFirstName() +
                                ", last name: " + user.getLastName() + ". HTTP Status: " + ((HttpStatusCodeException)ex).getRawStatusCode(), ex);
                    }
                }
            }
        }
    }
}
