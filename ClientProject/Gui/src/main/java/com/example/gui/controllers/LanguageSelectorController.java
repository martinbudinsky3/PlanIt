package com.example.gui.controllers;

import com.example.gui.utils.UTF8Control;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

/** Controller for "LanguageSelector.fxml" */
public class LanguageSelectorController implements Initializable {

    @FXML
    private AnchorPane ap;

    private ToggleGroup group = new ToggleGroup();
    private LanguageChangeWindow languageChangeWindow;


    public LanguageSelectorController(LanguageChangeWindow languageChangeWindow){
        this.languageChangeWindow = languageChangeWindow;
    }


    /** Selecting language (slovak/ german/ english).
     * Working with "UTF8Control class" where are all the items needed for translation*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        final ObservableList names = FXCollections.observableArrayList();
        final ListView listView = new ListView();
        listView.setPrefSize(200, 250);
        listView.setEditable(true);

        names.addAll(
                "English", "Slovak", "Deutsch"
        );

        listView.setItems(names);
        listView.setCellFactory(param -> new RadioListCell());

        group.selectedToggleProperty().addListener((ob, o, n) -> {
            RadioButton rb = (RadioButton)group.getSelectedToggle();

            // create new Locale object with right selected language
            if (rb != null) {
                String language = rb.getText();
                Locale locale = new Locale("");
                if(language.equals("English")){
                    locale = new Locale("en");
                } else if(language.equals("Slovak")) {
                    locale = new Locale("sk");
                } else if(language.equals("Deutsch")){
                    locale = new Locale("de");
                }

                Stage stage = (Stage)ap.getScene().getWindow();
                stage.close();
                ResourceBundle bundle = ResourceBundle.getBundle("captions", locale, new UTF8Control());
                languageChangeWindow.reload(bundle);  // reload to display captions in new language
            }
        });

        ap.getChildren().clear();
        ap.getChildren().add(listView);
    }

    /**
     * GUI widget - list cell with radio button
     */
    private class RadioListCell extends ListCell<String> {
        @Override
        public void updateItem(String obj, boolean empty) {
            super.updateItem(obj, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                RadioButton radioButton = new RadioButton(obj);
                radioButton.setToggleGroup(group);
                setGraphic(radioButton);
            }
        }
    }
}
