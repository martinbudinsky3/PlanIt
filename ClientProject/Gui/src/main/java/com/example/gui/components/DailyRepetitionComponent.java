package com.example.gui.components;

import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

// TODO multilanguage
public class DailyRepetitionComponent extends VBox{
    private final Integer LEFT_MARGIN = 130;

    private HBox repetitionIntervalField = new HBox();
    private HBox repetitionIntervalErrorField = new HBox();
    private final Label firstLabel = new Label("Every");
    private ComboBox<Integer> repetitionIntervalSelector = new ComboBox<>();
    private final Label secondLabel = new Label("days");



    public DailyRepetitionComponent() {
        initRepetitionIntervalField();
    }

    /**
     * Create choice box for selection of repetition interval
     */
    protected void initRepetitionIntervalSelector() {
        repetitionIntervalSelector.getItems().addAll(1, 2, 3, 4, 5);
        repetitionIntervalSelector.setEditable(true);
        repetitionIntervalSelector.setMaxWidth(80);
        repetitionIntervalSelector.setValue(1);
        repetitionIntervalSelector.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            try {
                clearErrorLabel();
                repetitionIntervalSelector.setValue(Integer.parseInt(newText));
                if(Integer.parseInt(newText) < 1 || Integer.parseInt(newText) > 999) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                createRepetitionIntervalErrorLabel();
            }
        });
    }

    private void clearErrorLabel() {
        repetitionIntervalErrorField.getChildren().clear();
        getChildren().remove(repetitionIntervalErrorField);

    }

    protected void initRepetitionIntervalField() {
        initRepetitionIntervalSelector();

        repetitionIntervalField.getChildren().addAll(firstLabel, repetitionIntervalSelector, secondLabel);

        // add everything into root
        getChildren().add(repetitionIntervalField);

        setRepetitionIntervalFieldStyles();
    }

    protected void setRepetitionIntervalFieldStyles() {
        firstLabel.setFont(Font.font(20));
        secondLabel.setFont(Font.font(20));

        HBox.setMargin(firstLabel, new Insets(0, 15, 0, 0));
        HBox.setMargin(secondLabel, new Insets(0, 0, 0, 10));

        VBox.setMargin(repetitionIntervalField, new Insets(0, 0, 0, LEFT_MARGIN));
    }

    private void createRepetitionIntervalErrorLabel() {
        Label errorLabel = new Label("Insert please number from range 1 to 999");
        errorLabel.getStyleClass().add("error-label");
        repetitionIntervalErrorField.getChildren().add(errorLabel);

        VBox.setMargin(repetitionIntervalErrorField, new Insets(0, 0, 0, LEFT_MARGIN));
        int index = getChildren().indexOf(repetitionIntervalField);
        getChildren().add(index+1, repetitionIntervalErrorField);
    }

    public ComboBox<Integer> getRepetitionIntervalSelector() {
        return repetitionIntervalSelector;
    }
}
