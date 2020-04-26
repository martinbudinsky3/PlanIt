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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class PlanItLoginController implements Initializable {

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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addHandlers();
    }

    public void addHandlers() {
        buttonLogin.setOnAction(e -> {
            try {
                buttonLoginHandler(e);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        buttonRegister.setOnAction(e -> buttonRegisterHandler(e));
    }

    void buttonLoginHandler(ActionEvent event) throws Exception {
        if (textfieldId.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Nevyplnili ste všetky polia.");
            alert.setHeaderText(null);
            alert.setContentText("Pred prihlásením zadajte svoje ID!");
            alert.showAndWait();
        } else {
            try {
// Checking if id exists not working
//                if (usersClient.getUserById(Integer.parseInt(textfieldId.getText())) == null) {
//                    Alert alert = new Alert(Alert.AlertType.ERROR);
//                    alert.setTitle("Zadali ste nesprávne ID");
//                    alert.setHeaderText(null);
//                    alert.setContentText("Používateľ s takým ID neexistuje.");
//                    alert.showAndWait();
//                } else {
                    user = usersClient.getUserById(Integer.parseInt(textfieldId.getText()));
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getClassLoader().getResource("fxml/PlanItMainWindow.fxml"));
                    PlanItMainWindowController planItMainWindowController = new PlanItMainWindowController(new EventsClient(), usersClient, user);
                    fxmlLoader.setController(planItMainWindowController);
                    AnchorPane rootPane = (AnchorPane) fxmlLoader.load();
                    Scene newScene = new Scene(rootPane, 1213, 630);
                    newScene.getStylesheets().add(getClass().getClassLoader().getResource("css/styles.css").toExternalForm()); // not working // working for monthsList
                    Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
                    window.setScene(newScene);
                    window.centerOnScreen();
                    window.show();
//                }

            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Zadali ste zlé ID");
                alert.setHeaderText(null);
                alert.setContentText("Neznáme znaky v ID čísle!");
                alert.showAndWait();
            }
        }
    }

    void buttonRegisterHandler(ActionEvent event) {
        //TODO new users registration
    }

}
