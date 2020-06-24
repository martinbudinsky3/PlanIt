package com.example.gui.controllers;

import com.example.client.clients.EventsClient;
import com.example.client.clients.UsersClient;
import com.example.client.model.Event;
import com.example.client.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for "PlanItregistration.fxml"
 */
public class PlanItRegistrationController implements Initializable {
    private final EventsClient eventsClient;
    private final UsersClient usersClient;
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

    public PlanItRegistrationController(EventsClient eventsClient, UsersClient usersClient) {
        this.eventsClient = eventsClient;
        this.usersClient = usersClient;
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
            try {
                buttonCancelHandler(e);
            } catch (IOException ex) {
                showClientErrorAlert();
                ex.printStackTrace();
            }
        });
    }

    /**
     * Button for canceling registration (buttonCancel)
     * Shows "PlanItLogin" window.
     */
    private void buttonCancelHandler(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getClassLoader().getResource("fxml/PlanItLogin.fxml"));
        PlanItLoginController planItLoginController = new PlanItLoginController(new UsersClient());
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
    }

    /**
     * Button for registration (buttonRegister)
     * Getting first name, last name, username and password from TextFields.
     * If there is no user with the same username and password, new user is registered and "PlanItMainWindow" window shows (his calendar)
     */
    private void buttonRegisterHandler(ActionEvent event) throws Exception {

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

            Integer id = usersClient.addUser(user);

            if(id == null) {
                showServerErrorAlert();
            }
            else if (id == -1) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(resourceBundle.getString("registrationExistsAlertTitle"));
                alert.setHeaderText(null);
                alert.setContentText(resourceBundle.getString("registrationExistsAlertContent"));
                alert.showAndWait();
            } else {
                user.setIdUser(id);

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getClassLoader().getResource("fxml/PlanItMainWindow.fxml"));
                PlanItMainWindowController planItMainWindowController = new PlanItMainWindowController(eventsClient, usersClient, user);
                fxmlLoader.setController(planItMainWindowController);
                fxmlLoader.setResources(resourceBundle);
                AnchorPane rootPane = (AnchorPane) fxmlLoader.load();
                Scene newScene = new Scene(rootPane);
                newScene.getStylesheets().add(getClass().getClassLoader().getResource("css/styles.css").toExternalForm()); // not working // working for monthsList
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(newScene);
                window.centerOnScreen();
                window.resizableProperty().setValue(false);
                window.show();
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
        alert.setTitle(resourceBundle.getString("clientError"));
        alert.setHeaderText(resourceBundle.getString("errorAlertHeader"));
        alert.setContentText(resourceBundle.getString("errorAlertContext"));
        alert.showAndWait();
    }
}
