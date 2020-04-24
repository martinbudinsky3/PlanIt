package com.example.gui.controllers;

import com.example.client.clients.EventsClient;
import com.example.client.clients.UsersClient;
import com.example.client.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class PlanItLoginController {

    private User user;
    private final UsersClient usersClient;

    @FXML
    private Button buttonLogin;

    @FXML
    private TextField textfieldId;

    @FXML
    private Button buttonRegister;

    public PlanItLoginController(UsersClient usersClient)  {
        this.usersClient = usersClient;
    }

    @FXML
    void handleLogin(ActionEvent event) throws Exception {
        if (textfieldId.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Nevyplnili ste všetky polia.");
            alert.setHeaderText(null);
            alert.setContentText("Pred prihlásením zadajte svoje ID!");
            alert.showAndWait();
        } else {
            try {
                if (usersClient.getUserById(Integer.parseInt(textfieldId.getText())).isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Zadali ste nesprávne ID");
                    alert.setHeaderText(null);
                    alert.setContentText("Používateľ s takým ID neexistuje.");
                    alert.showAndWait();
                } else {
                    user = usersClient.getUserById(Integer.parseInt(textfieldId.getText())).get(0);
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getClassLoader().getResource("fxml/PlanItMainWindow.fxml"));
                    PlanItMainWindowController planItMainWindowController = new PlanItMainWindowController(new EventsClient(), usersClient, user);
                    fxmlLoader.setController(planItMainWindowController);
                    AnchorPane rootPane = (AnchorPane) fxmlLoader.load();
                    Scene newScene = new Scene(rootPane, 1213, 630);
                    //newScene.getStylesheets().add(getClass().getClassLoader().getResource("css/styles.css").toExternalForm()); // not working
                    Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
                    window.setScene(newScene);
                    window.centerOnScreen();
                    window.show();
                }

            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Zadali ste zlé ID");
                alert.setHeaderText(null);
                alert.setContentText("Neznáme znaky v ID čísle!");
                alert.showAndWait();
            }
        }


    }

    @FXML
    void handleRegister(ActionEvent event) {

    }
}
