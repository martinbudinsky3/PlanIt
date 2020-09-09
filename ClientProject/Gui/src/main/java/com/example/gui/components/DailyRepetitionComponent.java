package com.example.gui.components;

import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.ResourceBundle;

public class DailyRepetitionComponent extends VBox{
    private final Integer LEFT_MARGIN = 130;
    private final ResourceBundle resourceBundle;

    private final HBox repetitionIntervalField = new HBox();
    private final HBox repetitionIntervalErrorField = new HBox();
    private final Label firstLabel = new Label();
    private final ComboBox<Integer> repetitionIntervalSelector = new ComboBox<>();
    private final Label secondLabel = new Label();

    public DailyRepetitionComponent(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;

        firstLabel.setText(resourceBundle.getString("repetitionIntervalFirstLabel"));
        setCaptions();
        initRepetitionIntervalField();

        // add everything into root
        getChildren().add(repetitionIntervalField);
    }

    private void setCaptions() {
        secondLabel.setText(resourceBundle.getString("repetitionIntervalDayLabel"));
    }

    private void initRepetitionIntervalField() {
        initRepetitionIntervalSelector();

        repetitionIntervalField.getChildren().addAll(firstLabel, repetitionIntervalSelector, secondLabel);

        setRepetitionIntervalFieldStyles();
    }

    /**
     * Create choice box for selection of repetition interval
     */
    private void initRepetitionIntervalSelector() {
        repetitionIntervalSelector.getItems().addAll(1, 2, 3, 4, 5);
        repetitionIntervalSelector.setEditable(true);
        repetitionIntervalSelector.setValue(1);
        repetitionIntervalSelector.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            try {
                clearErrorLabel(repetitionIntervalErrorField);
                repetitionIntervalSelector.setValue(Integer.parseInt(newText));
                if(Integer.parseInt(newText) < 1 || Integer.parseInt(newText) > 999) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                createErrorLabel(repetitionIntervalErrorField, repetitionIntervalField, "repetitionIntervalErrorLabel");
            }
        });
    }

    private void setRepetitionIntervalFieldStyles() {
        firstLabel.setFont(Font.font(18));
        secondLabel.setFont(Font.font(18));

        repetitionIntervalSelector.setMaxWidth(80);

        HBox.setMargin(firstLabel, new Insets(0, 15, 0, 0));
        HBox.setMargin(secondLabel, new Insets(0, 0, 0, 10));

        VBox.setMargin(repetitionIntervalField, new Insets(0, 0, 0, LEFT_MARGIN));
    }

    protected void clearErrorLabel(HBox errorField) {
        errorField.getChildren().clear();
        getChildren().remove(errorField);
    }

    protected void createErrorLabel(HBox errorField, HBox field, String key) {
        Label errorLabel = new Label(resourceBundle.getString(key));
        errorLabel.getStyleClass().add("error-label");
        errorField.getChildren().add(errorLabel);

        VBox.setMargin(errorField, new Insets(0, 0, 0, LEFT_MARGIN));
        int index = getChildren().indexOf(field);
        getChildren().add(index+1, errorField);
    }

    public ComboBox<Integer> getRepetitionIntervalSelector() {
        return repetitionIntervalSelector;
    }

    public Label getSecondLabel() {
        return secondLabel;
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }
}