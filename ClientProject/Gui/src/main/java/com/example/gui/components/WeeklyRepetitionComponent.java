package com.example.gui.components;

import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class WeeklyRepetitionComponent extends DailyRepetitionComponent {
    private final Integer LEFT_MARGIN = 130;

    private final HBox daysOfWeekField = new HBox();
    private final HBox daysOfWeekErrorField = new HBox();

    private final List<CheckBox> dayOfWeekCheckBoxes = new ArrayList<CheckBox>();
    private final List<CheckBox> selectedDaysOfWeek = new ArrayList<CheckBox>();

    public WeeklyRepetitionComponent(ResourceBundle resourceBundle) {
        super(resourceBundle);

        initDaysOfWeekField();

        getChildren().add(daysOfWeekField);
    }

    private void initDaysOfWeekField() {
        initDaysOfWeekBoxes();
        daysOfWeekField.getChildren().addAll(dayOfWeekCheckBoxes);
        setDaysOfWeekFieldStyles();
    }

    private void initDaysOfWeekBoxes() {
        DateFormatSymbols symbols = new DateFormatSymbols(getResourceBundle().getLocale());
        List<String> dayNamesWrongOrder = Arrays.asList(symbols.getShortWeekdays());

        List<String> dayNames = new ArrayList<String>(dayNamesWrongOrder.subList(2, dayNamesWrongOrder.size()));
        dayNames.add(dayNamesWrongOrder.get(1));

        for (String dayName : dayNames) {
            CheckBox dayCheckBox = new CheckBox(dayName);
            dayCheckBox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                clearDaysOfWeekErrorLabel();
                if (isNowSelected) {
                    selectedDaysOfWeek.add(dayCheckBox);
                } else {
                    selectedDaysOfWeek.remove(dayCheckBox);
                    if(selectedDaysOfWeek.isEmpty()) {
                        createDaysOfWeekErrorLabel();
                    }
                }
            });
            dayOfWeekCheckBoxes.add(dayCheckBox);
        }

        dayOfWeekCheckBoxes.get(0).selectedProperty().setValue(true);
    }

    private void clearDaysOfWeekErrorLabel() {
        daysOfWeekErrorField.getChildren().clear();
        getChildren().remove(daysOfWeekErrorField);
    }

    private void createDaysOfWeekErrorLabel() {
        Label errorLabel = new Label(getResourceBundle().getString("repetitionIntervalErrorLabel"));
        errorLabel.getStyleClass().add("error-label");
        daysOfWeekErrorField.getChildren().add(errorLabel);

        VBox.setMargin(daysOfWeekErrorField, new Insets(0, 0, 0, LEFT_MARGIN));
        int index = getChildren().indexOf(daysOfWeekField);
        getChildren().add(index+1, daysOfWeekErrorField);
    }

    protected void setCaptions() {
        getSecondLabel().setText(getResourceBundle().getString("repetitionIntervalWeekLabel"));
    }

    private void setDaysOfWeekFieldStyles() {
        dayOfWeekCheckBoxes.forEach(checkBox -> checkBox.setFont(Font.font(16)));
        daysOfWeekField.setSpacing(10);
        VBox.setMargin(daysOfWeekField, new Insets(20, 0, 0, LEFT_MARGIN));
    }
}
