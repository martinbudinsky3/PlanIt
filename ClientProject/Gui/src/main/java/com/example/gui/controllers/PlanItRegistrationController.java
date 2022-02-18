package com.example.gui.controllers;

import com.example.client.clients.UsersClient;
import com.example.client.exceptions.ConflictException;
import com.example.model.User;
import com.example.utils.WindowsCreator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for "PlanItRegistration.fxml"
 */
public class PlanItRegistrationController implements Initializable {
    private static final Logger logger = LoggerFactory.getLogger(PlanItRegistrationController.class);

    private final UsersClient usersClient = UsersClient.getInstance();
    private final WindowsCreator windowsCreator = WindowsCreator.getInstance();

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
            windowsCreator.createLoginWindow(resourceBundle, (Stage) ap.getScene().getWindow());
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
                Optional<ButtonType> result = windowsCreator.showInfoAlert(resourceBundle.getString("successfulRegistrationAlertTitle"),
                        resourceBundle.getString("successfulRegistrationAlertContent"));
                if (result.get() == ButtonType.OK) {
                    windowsCreator.createLoginWindow(resourceBundle, (Stage) ap.getScene().getWindow());
                }

            } catch (Exception e) {
                if (e instanceof ConflictException) {
                    windowsCreator.showInfoAlert(resourceBundle.getString("registrationExistsAlertTitle"),
                            resourceBundle.getString("registrationExistsAlertContent"));
                } else {
                    windowsCreator.showErrorAlert(resourceBundle);
                }

            }
        }
    }
}
