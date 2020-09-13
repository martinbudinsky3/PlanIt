package com.example.gui.components;

import com.example.client.model.repetition.MonthlyRepetition;
import com.example.client.model.repetition.WeeklyRepetition;
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

    public MonthlyRepetitionComponent(ResourceBundle resourceBundle) {
        super(resourceBundle);

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
    }

    private void initializeDayOfWeekSelector() {
        dayOfWeekSelector.getItems().addAll(getDayNames());
    }

    private List<String> getDayNames() {
        DateFormatSymbols symbols = new DateFormatSymbols(getResourceBundle().getLocale());
        List<String> dayNamesWrongOrder = Arrays.asList(symbols.getWeekdays());

        List<String> dayNames = new ArrayList<String>(dayNamesWrongOrder.subList(2, dayNamesWrongOrder.size()));
        dayNames.add(dayNamesWrongOrder.get(1));

        return dayNames;
    }

    private Integer countOrdinalOfDay(LocalDate initDate) {
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

    public void setInitValues(LocalDate initDate) {
        super.setInitValues(initDate);

        // set init value for dayOfMonthSelector
        dayOfMonthSelector.setValue(initDate.getDayOfMonth());

        setInitDayOfWeekAndOrdinalValue(initDate);
    }

    private void setInitDayOfWeekAndOrdinalValue(LocalDate initDate) {
        // set init value for dayOfWeekSelector
        Integer ordinal = countOrdinalOfDay(initDate);
        if(ordinal == 5) {
            ordinalSelector.getSelectionModel().selectLast();
        } else {
            ordinalSelector.getSelectionModel().select(ordinal);
        }

        // set init value for dayOfWeekSelector
        int initDayOfWeekCode = initDate.getDayOfWeek().getValue();
        String initDayName = getDayNames().get(initDayOfWeekCode-1);
        dayOfWeekSelector.setValue(initDayName);
    }

    public void showRepetitionDetail(MonthlyRepetition repetition) {
        super.showRepetitionDetail(repetition);

        if(repetition.getDayOfMonth() != null) {
            dayOfMonthSelector.setValue(repetition.getDayOfMonth());
            setInitDayOfWeekAndOrdinalValue(repetition.getStart());
        } else if(repetition.getOrdinal() != null && repetition.getDaysOfWeek() != null){
            setInitDayOfWeekAndOrdinalValue(repetition.getStart());
            dayOfMonthSelector.setValue(repetition.getDayOfMonth());
        }
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
