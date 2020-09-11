package com.example.gui.components;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.text.DateFormatSymbols;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class MonthlyRepetitionComponent extends DailyRepetitionComponent{
    private final Integer LEFT_MARGIN = 130;
    private LocalDate initDate;

    private final ToggleGroup monthlyRepetitionOptions = new ToggleGroup();
    private RadioButton dayOfMonthOption = new RadioButton();
    private RadioButton dayOfWeekWithOrdinalOption = new RadioButton();

    private HBox dayOfMonthOptionBox = new HBox();
    private Label dayOfMonthLabel = new Label();
    private ChoiceBox<Integer> dayOfMonthSelector = new ChoiceBox<>();

    private HBox dayOfWeekWithOrdinalOptionBox = new HBox();
    private Label dayOfWeekWithOrdinalLabel = new Label();
    private ChoiceBox<Object> ordinalSelector = new ChoiceBox<Object>();
    private ChoiceBox<String> dayOfWeekSelector = new ChoiceBox<String>();

    public MonthlyRepetitionComponent(ResourceBundle resourceBundle, LocalDate initDate) {
        super(resourceBundle);
        this.initDate = initDate;

        setMonthlyRepetitionCaptions();

        initializeDayOfMonthOption();

        initializeDayOfWeekWithOrdinalOption();

        monthlyRepetitionOptions.getToggles().addAll(dayOfMonthOption, dayOfWeekWithOrdinalOption);
        monthlyRepetitionOptions.selectToggle(dayOfMonthOption);

        getChildren().addAll(dayOfMonthOption, dayOfWeekWithOrdinalOption);
    }

    private void initializeDayOfMonthOption() {
        dayOfMonthLabel.setText(getResourceBundle().getString("dayOfMonthLabel"));
        initializeDayOfMonthSelector();
        dayOfMonthOptionBox.getChildren().addAll(dayOfMonthLabel, dayOfMonthSelector);
        dayOfMonthOption.setGraphic(dayOfMonthOptionBox);

        setDayOfMonthFieldStyles();
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

    private void initializeDayOfWeekWithOrdinalOption() {
        dayOfWeekWithOrdinalLabel.setText(getResourceBundle().getString("dayOfWeekWithOrdinalLabel"));
        initializeOrdinalSelector();
        initializeDayOfWeekSelector();
        dayOfWeekWithOrdinalOptionBox.getChildren().addAll(dayOfWeekWithOrdinalLabel, ordinalSelector, dayOfWeekSelector);
        dayOfWeekWithOrdinalOption.setGraphic(dayOfWeekWithOrdinalOptionBox);
        setDayOfWeekWithOrdinalFieldStyles();
    }

    private void initializeOrdinalSelector() {
        ordinalSelector.getItems().addAll(1, 2, 3, 4, getResourceBundle().getString("lastLabel"));
        Integer ordinal = countOrdinalOfDay();
        if(ordinal == 5) {
            ordinalSelector.getSelectionModel().selectLast();
        } else {
            ordinalSelector.getSelectionModel().select(ordinal);
        }
    }

    private void initializeDayOfWeekSelector() {
        DateFormatSymbols symbols = new DateFormatSymbols(getResourceBundle().getLocale());
        List<String> dayNamesWrongOrder = Arrays.asList(symbols.getWeekdays());

        List<String> dayNames = new ArrayList<String>(dayNamesWrongOrder.subList(2, dayNamesWrongOrder.size()));
        dayNames.add(dayNamesWrongOrder.get(1));

        dayOfWeekSelector.getItems().addAll(dayNames);
        int initDayOfWeekCode = initDate.getDayOfWeek().getValue();
        String initDayName = dayNames.get(initDayOfWeekCode-1);
        dayOfWeekSelector.setValue(initDayName);
    }

    private Integer countOrdinalOfDay() {
        DayOfWeek dayOfWeek = initDate.getDayOfWeek();
        // last day of previous month
        LocalDate date = LocalDate.of(initDate.getYear(), initDate.getMonthValue(), 1).minusDays(1);
        Integer ordinal = 0;
        while(!date.equals(initDate))  {
            date = date.with(TemporalAdjusters.next(dayOfWeek));
            ordinal++;
        }

        return ordinal;
    }

    private void setMonthlyRepetitionCaptions() {
        dayOfMonthLabel.setText(getResourceBundle().getString("dayOfMonthLabel"));
        dayOfWeekWithOrdinalLabel.setText(getResourceBundle().getString("dayOfWeekWithOrdinalLabel"));
    }

    protected void setRepetitionIntervalCaption() {
        getSecondLabel().setText(getResourceBundle().getString("repetitionIntervalMonthLabel"));
    }

    private void setDayOfMonthFieldStyles() {
        dayOfMonthLabel.setFont(Font.font(18));
        HBox.setMargin(dayOfMonthLabel, new Insets(0, 15, 0, 0));
        VBox.setMargin(dayOfMonthOption, new Insets(20, 0, 0, LEFT_MARGIN));
    }

    private void setDayOfWeekWithOrdinalFieldStyles() {
        dayOfWeekWithOrdinalLabel.setFont(Font.font(18));
        HBox.setMargin(dayOfWeekWithOrdinalLabel, new Insets(0, 15, 0, 0));
        HBox.setMargin(ordinalSelector, new Insets(0, 10, 0, 0));
        VBox.setMargin(dayOfWeekWithOrdinalOption, new Insets(20, 0, 0, LEFT_MARGIN));
    }

    public LocalDate getInitDate() {
        return initDate;
    }

    public ToggleGroup getMonthlyRepetitionOptions() {
        return monthlyRepetitionOptions;
    }

    public ChoiceBox<Integer> getDayOfMonthSelector() {
        return dayOfMonthSelector;
    }

    public ChoiceBox<Object> getOrdinalSelector() {
        return ordinalSelector;
    }

    public ChoiceBox<String> getDayOfWeekSelector() {
        return dayOfWeekSelector;
    }
}
