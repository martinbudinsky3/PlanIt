package com.example.gui.controllers;

import com.example.client.clients.EventsClient;
import com.example.client.clients.UsersClient;
import com.example.client.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PlanItLoginController implements Initializable, LanguageChangeWindow {

    private User user;
    private final UsersClient usersClient;

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

    private ResourceBundle resourceBundle;

    public PlanItLoginController(UsersClient usersClient)  {
        this.usersClient = usersClient;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resourceBundle = resources;
        addHandlers();
    }

    @Override
    public void reload(ResourceBundle bundle){
        resourceBundle = bundle;
        try {
            Scene scene = ap.getScene();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("fxml/PlanItLogin.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setResources(bundle);
            AnchorPane rootPane = (AnchorPane) fxmlLoader.load();
            scene.setRoot(rootPane);

        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void addHandlers() {
        buttonLogin.setOnAction(e -> {
            try {
                buttonLoginHandler(e);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        buttonRegister.setOnAction(e -> {
            try {
                buttonRegisterHandler(e);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        changeLanguageButton.setOnAction(e -> {
            try {
                buttonLanguageSelectHandler(e);
            } catch(IOException ex) {
                ex.printStackTrace();;
            }
        });
    }

    void buttonLoginHandler(ActionEvent event) throws Exception {
        if (textfieldName.getText().isEmpty() || passwordfieldPassword.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(resourceBundle.getString("loginEmptyAlertTitle"));
            alert.setHeaderText(null);
            alert.setContentText(resourceBundle.getString("loginAlertContent"));
            alert.showAndWait();
        } else {
            user = usersClient.getUserByUserNameAndUserPassword(textfieldName.getText(), passwordfieldPassword.getText());

            if (user == null){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(resourceBundle.getString("loginWrongAlertTitle"));
                alert.setHeaderText(null);
                alert.setContentText(resourceBundle.getString("loginAlertContent"));
                alert.showAndWait();
            }
            else {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getClassLoader().getResource("fxml/PlanItMainWindow.fxml"));
                PlanItMainWindowController planItMainWindowController = new PlanItMainWindowController(new EventsClient(), usersClient, user);
                fxmlLoader.setController(planItMainWindowController);
                fxmlLoader.setResources(resourceBundle);
                AnchorPane rootPane = (AnchorPane) fxmlLoader.load();
                Scene newScene = new Scene(rootPane);
                newScene.getStylesheets().add(getClass().getClassLoader().getResource("css/styles.css").toExternalForm());
                Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
                window.setScene(newScene);
                window.centerOnScreen();
                window.resizableProperty().setValue(false);
                window.show();
            }
        }
    }

    void buttonRegisterHandler(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getClassLoader().getResource("fxml/PlanItRegistration.fxml"));
        PlanItRegistrationController planItRegistrationController = new PlanItRegistrationController(new EventsClient(), usersClient);
        fxmlLoader.setController(planItRegistrationController);
        fxmlLoader.setResources(resourceBundle);
        AnchorPane rootPane = (AnchorPane) fxmlLoader.load();
        Scene newScene = new Scene(rootPane);
        newScene.getStylesheets().add(getClass().getClassLoader().getResource("css/styles.css").toExternalForm());
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(newScene);
        window.centerOnScreen();
        window.resizableProperty().setValue(false);
        window.show();
    }

    void buttonLanguageSelectHandler(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getClassLoader().getResource("fxml/LanguageSelector.fxml"));
        LanguageSelectorController languageSelectorController = new LanguageSelectorController(this);
        fxmlLoader.setController(languageSelectorController);
        AnchorPane rootPane = (AnchorPane) fxmlLoader.load();
        Scene newScene = new Scene(rootPane);
        Stage window = new Stage();
        window.setScene(newScene);
        window.centerOnScreen();
        window.resizableProperty().setValue(false);
        window.show();
    }

}
