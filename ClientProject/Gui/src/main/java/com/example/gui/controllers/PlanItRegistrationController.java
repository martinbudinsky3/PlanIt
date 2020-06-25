package com.example.gui.controllers;

import com.example.client.clients.EventsClient;
import com.example.client.clients.UsersClient;
import com.example.client.model.User;
import com.example.gui.utils.WindowsCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpStatusCodeException;

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
            try {
                buttonRegisterHandler(e);
            } catch (Exception ex) {
                showClientErrorAlert();
                ex.printStackTrace();
            }
        });
        buttonCancel.setOnAction(e -> {
            windowsCreator.createLoginWindow(resourceBundle, ap);
        });
    }


    /**
     * Button for registration (buttonRegister)
     * Getting first name, last name, username and password from TextFields.
     * If there is no user with the same username and password, new user is registered and "PlanItMainWindow" window shows (his calendar)
     */
    private void buttonRegisterHandler(ActionEvent event) {

        if (textfieldFirstName.getText().isEmpty() || textfieldLastName.getText().isEmpty() || textfieldUserName.getText().isEmpty() || textfieldUserpassword.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(resourceBundle.getString("loginEmptyAlertTitle"));
            alert.setHeaderText(null);
            alert.setContentText(resourceBundle.getString("registrationEmptyContentTitle"));
            alert.showAndWait();
        } else {

            String firstName = textfieldFirstName.getText();
            String lastName = textfieldLastName.getText();
            String userName = textfieldUserName.getText();
            String userPassword = textfieldUserpassword.getText();

            User user = new User(firstName, lastName, userName, userPassword);

            try {
                Integer id = usersClient.addUser(user);
                user.setIdUser(id);
                windowsCreator.createMainWindow(resourceBundle, usersClient, user, event);
            } catch (JsonProcessingException jsonEx) {
                logger.error("Error inserting new user.Username: " + user.getUserName() + " First name: " + user.getFirstName() +
                        ", last name: " + user.getLastName(), jsonEx);
                windowsCreator.showErrorAlert(resourceBundle);
            } catch (HttpStatusCodeException httpEx) {
                if(httpEx.getRawStatusCode() != 500) {
                    logger.error("Error inserting new user.Username: " + user.getUserName() + " First name: " + user.getFirstName() +
                    ", last name: " + user.getLastName() + ". HTTP Status: " + httpEx.getRawStatusCode(), httpEx);
                    windowsCreator.showErrorAlert(resourceBundle);
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle(resourceBundle.getString("registrationExistsAlertTitle"));
                    alert.setHeaderText(null);
                    alert.setContentText(resourceBundle.getString("registrationExistsAlertContent"));
                    alert.showAndWait();
                }
            }
        }
    }

    public void showServerErrorAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(resourceBundle.getString("serverError"));
        alert.setHeaderText(resourceBundle.getString("errorAlertHeader"));
        alert.setContentText(resourceBundle.getString("errorAlertContext"));
        alert.showAndWait();
    }

    public void showClientErrorAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(resourceBundle.getString("error"));
        alert.setHeaderText(resourceBundle.getString("errorAlertHeader"));
        alert.setContentText(resourceBundle.getString("errorAlertContext"));
        alert.showAndWait();
    }
}
