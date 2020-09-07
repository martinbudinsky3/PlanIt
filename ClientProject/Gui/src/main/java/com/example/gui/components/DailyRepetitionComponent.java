package com.example.gui.components;

import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

// TODO multilanguage
public class DailyRepetitionComponent {
    private final Integer LEFT_MARGIN = 130;
    private VBox root = new VBox();
    private HBox repetitionIntervalField = new HBox();
    private final Label firstLabel = new Label("Every");
    private ComboBox<Integer> repetitionIntervalSelector = new ComboBox<>();
    private final Label secondLabel = new Label("days");



    public DailyRepetitionComponent() {
        // create choice box for selection of repetition interval
        repetitionIntervalSelector.getItems().addAll(1, 2, 3, 4, 5, 6);
        repetitionIntervalSelector.setEditable(true);
        repetitionIntervalSelector.setMaxWidth(80);
        repetitionIntervalSelector.setValue(1);
        repetitionIntervalSelector.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            try {
                repetitionIntervalSelector.setValue(Integer.parseInt(newText));
            } catch (NumberFormatException e) {
                createErrorLabel();
            }
        });

        // add everything in one HBox
        repetitionIntervalField.getChildren().addAll(firstLabel, repetitionIntervalSelector, secondLabel);

        // add result to root element
        root.getChildren().add(repetitionIntervalField);

        setStyles();
    }

    private void setStyles() {
        firstLabel.setFont(Font.font(20));
        secondLabel.setFont(Font.font(20));

        HBox.setMargin(firstLabel, new Insets(0, 15, 0, 0));
        HBox.setMargin(secondLabel, new Insets(0, 0, 0, 10));

        VBox.setMargin(repetitionIntervalField, new Insets(0, 0, 0, LEFT_MARGIN));
    }

    private void createErrorLabel() {
        // TODO
    }

    public VBox get() {
        return root;
    }

    public ComboBox<Integer> getRepetitionIntervalSelector() {
        return repetitionIntervalSelector;
    }
}
