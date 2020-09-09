package com.example.gui.components;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MonthlyRepetitionComponent extends DailyRepetitionComponent{
    private LocalDate initDate;

    private final ToggleGroup monthlyRepetitionOptions = new ToggleGroup();
    private RadioButton dayOfMonthOption = new RadioButton();
    private RadioButton dayOfWeekWithOrdinalOption = new RadioButton();

    private HBox dayOfMonthOptionBox = new HBox();
    private Label dayOfMonthLabel = new Label();
    private ChoiceBox<Integer> dayOfMonthSelector = new ChoiceBox<>();

    private HBox dayOfWeekWithOrdinalOptionBox = new HBox();
    private Label dayOfWeekWithOrdinalLabel = new Label();
    private ChoiceBox<Integer> ordinalSelector = new ChoiceBox<Integer>();
    private ChoiceBox<String> dayOfWeekSelector = new ChoiceBox<String>();

    public MonthlyRepetitionComponent(ResourceBundle resourceBundle, LocalDate initDate) {
        super(resourceBundle);
        this.initDate = initDate;

        setCaptions();

        initializeDayOfMonthOptionBox();

        initializedayOfWeekWithOrdinalOptionBox();
    }

    private void initializeDayOfMonthOptionBox() {

    }

    private void initializeDayOfMonthSelector() {
        List<Integer> daysOfMonthNumbers = new ArrayList<Integer>();
        for(int i = 1; i <= 31; i++) {
            daysOfMonthNumbers.add(i);
        }

        dayOfMonthSelector.getItems().addAll(daysOfMonthNumbers);
        dayOfMonthSelector.setMaxWidth(80);
        dayOfMonthSelector.setValue(initDate.getDayOfMonth());
    }

    private void initializedayOfWeekWithOrdinalOptionBox() {
    }

    protected void setCaptions() {
        getSecondLabel().setText(getResourceBundle().getString("repetitionIntervalMonthLabel"));
        dayOfMonthLabel.setText(getResourceBundle().getString("dayOfMonthLabel"));
        dayOfWeekWithOrdinalLabel.setText(getResourceBundle().getString("dayOfWeekWithOrdinalLabel"));
    }
}
