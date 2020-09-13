package com.example.gui.components;

import com.example.client.model.repetition.YearlyRepetition;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ResourceBundle;

public class YearlyRepetitionComponent extends MonthlyRepetitionComponent{
    private final Integer LEFT_MARGIN = 130;
    private final Integer MONTH_FIELD_INDEX = 1;

    private final HBox monthField = new HBox();
    private final Label monthLabel = new Label();
    private final ChoiceBox<String> monthSelector = new ChoiceBox<String>();

    public YearlyRepetitionComponent(ResourceBundle resourceBundle) {
        super(resourceBundle);

        setYearlyRepetitionCaptions();

        initializeMonthField();

        getChildren().add(MONTH_FIELD_INDEX, monthField);
    }

    private void initializeMonthField() {
        initializeMonthSelector();
        monthField.getChildren().addAll(monthLabel, monthSelector);
        setMonthFieldStyles();
    }

    private void setYearlyRepetitionCaptions() {
        monthLabel.setText(getResourceBundle().getString("monthSelectionLabel"));
    }

    protected void setRepetitionIntervalCaption() {
        getSecondLabel().setText(getResourceBundle().getString("repetitionIntervalYearLabel"));
    }

    private void initializeMonthSelector() {
        for(Month month : Month.values()) {
            String monthName = month.getDisplayName(TextStyle.FULL_STANDALONE, getResourceBundle().getLocale());
            monthSelector.getItems().add(monthName);
        }

        monthSelector.getSelectionModel().selectedIndexProperty().addListener(((observable, oldValue, newValue) -> {
            Month newMonth = Month.of((Integer) newValue + 1);
            int numberOfDaysInMonth = newMonth.maxLength();

            int selectedDayOfMonth = getDayOfMonthSelector().getValue();
            getDayOfMonthSelector().getItems().clear();

            for(int i = 1; i <= numberOfDaysInMonth; i++) {
                getDayOfMonthSelector().getItems().add(i);
            }

            getDayOfMonthSelector().setValue(selectedDayOfMonth);
        }));
    }

    private void setMonthFieldStyles() {
        monthLabel.setFont(Font.font(18));
        HBox.setMargin(monthLabel, new Insets(0, 15, 0, 0));
        VBox.setMargin(monthField, new Insets(20, 0, 0, LEFT_MARGIN));
    }

    public void setInitValues(LocalDate initDate) {
        super.setInitValues(initDate);

        int initMonthValue = initDate.getMonth().getValue();
        monthSelector.setValue(monthSelector.getItems().get(initMonthValue-1));
    }

    public void showRepetitionDetail(YearlyRepetition repetition) {
        super.showRepetitionDetail(repetition);

        int repetitionMonthValue = repetition.getMonth();
        monthSelector.setValue(monthSelector.getItems().get(repetitionMonthValue-1));
    }
}
