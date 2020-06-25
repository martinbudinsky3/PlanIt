package com.example.gui.controllers;

import com.example.client.clients.UsersClient;
import com.example.client.model.User;
import com.example.gui.utils.WindowsCreator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * Controller for "PlanItLogin.fxml"
 */
public class PlanItLoginController implements Initializable, LanguageChangeWindow {

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
        //resourceBundle = bundle;
        windowsCreator.reload(ap, bundle, "fxml/PlanItLogin.fxml",this);
    }

    /**
     * Adding functionality to the buttons.
     */
    public void addHandlers() {
        buttonLogin.setOnAction(e -> {
            try {
                buttonLoginHandler(e);
            } catch (Exception ex) {
                showClientErrorAlert();
                ex.printStackTrace();
            }
        });
        buttonRegister.setOnAction(e -> {
            try {
                windowsCreator.createRegistrationWindow(usersClient, resourceBundle, e);
            } catch (IOException ex) {
                showClientErrorAlert();
                ex.printStackTrace();
            }
        });
        changeLanguageButton.setOnAction(e -> {
            try {
                windowsCreator.createLanguageSelectorWindow(this);
            } catch (IOException ex) {
                showClientErrorAlert();
                ex.printStackTrace();
                ;
            }
        });
    }

    /**
     * Login button.
     * Getting username and password from the TextFields. If the entered data is valid, user is logged in and the "PlanItMainWindow" opens.
     */
    void buttonLoginHandler(ActionEvent event) throws Exception {
        if (textfieldName.getText().isEmpty() || passwordfieldPassword.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(resourceBundle.getString("loginEmptyAlertTitle"));
            alert.setHeaderText(null);
            alert.setContentText(resourceBundle.getString("loginAlertContent"));
            alert.showAndWait();
        } else {
            user = usersClient.getUserByUserNameAndUserPassword(textfieldName.getText(), passwordfieldPassword.getText());

            if (user == null) {
                showServerErrorAlert();
            } else if (user.getUserName() == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(resourceBundle.getString("loginWrongAlertTitle"));
                alert.setHeaderText(null);
                alert.setContentText(resourceBundle.getString("loginAlertContent"));
                alert.showAndWait();
            } else {
                windowsCreator.createMainWindow(resourceBundle, usersClient, user, event);
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
