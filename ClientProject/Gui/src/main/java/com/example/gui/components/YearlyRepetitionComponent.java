package com.example.gui.components;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ResourceBundle;

public class YearlyRepetitionComponent extends MonthlyRepetitionComponent{
    private final Integer LEFT_MARGIN = 130;
    private final HBox monthField = new HBox();
    private final Label monthLabel = new Label();
    private final ChoiceBox<String> monthSelector = new ChoiceBox<String>();

    public YearlyRepetitionComponent(ResourceBundle resourceBundle, LocalDate initDate) {
        super(resourceBundle, initDate);

        setYearlyRepetitionCaptions();

        initializeMonthSelector();
    }

    private void setYearlyRepetitionCaptions() {
        monthLabel.setText(getResourceBundle().getString("monthSelectionLabel"));
    }

    protected void setRepetitionIntervalCaption() {
        getSecondLabel().setText(getResourceBundle().getString("repetitionIntervalYearLabel"));
    }

    private void initializeMonthSelector() {
        for(Month month : Month.values()) {
            String monthName = month.getDisplayName(TextStyle.FULL, getResourceBundle().getLocale());
            monthSelector.getItems().add(monthName);
        }

        Month initMonth = getInitDate().getMonth();

    }
}
