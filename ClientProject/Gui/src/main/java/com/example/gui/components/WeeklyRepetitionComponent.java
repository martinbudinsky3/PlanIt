package com.example.gui.components;

import com.example.model.repetition.Repetition;
import com.example.model.repetition.WeeklyRepetition;
import com.example.utils.Utils;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
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
        setRepetitionIntervalCaption();
        
        initDaysOfWeekField();

        getChildren().add(daysOfWeekField);
    }

    private void initDaysOfWeekField() {
        initDaysOfWeekBoxes();
        daysOfWeekField.getChildren().addAll(dayOfWeekCheckBoxes);
        setDaysOfWeekFieldStyles();
    }

    private void initDaysOfWeekBoxes() {
        List<String> dayNames = Utils.getDayNames(true, getResourceBundle());

        for (String dayName : dayNames) {
            CheckBox dayCheckBox = new CheckBox(dayName);
            dayCheckBox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                clearErrorLabel(daysOfWeekErrorField);
                if (isNowSelected) {
                    selectedDaysOfWeek.add(dayCheckBox);
                } else {
                    selectedDaysOfWeek.remove(dayCheckBox);
                    if(selectedDaysOfWeek.isEmpty()) {
                        createErrorLabel(daysOfWeekErrorField, daysOfWeekField, "repetitionIntervalErrorLabel");
                    }
                }
            });
            dayOfWeekCheckBoxes.add(dayCheckBox);
        }
    }

    protected void setRepetitionIntervalCaption() {
        getSecondLabel().setText(getResourceBundle().getString("repetitionIntervalWeekLabel"));
    }

    private void setDaysOfWeekFieldStyles() {
        dayOfWeekCheckBoxes.forEach(checkBox -> checkBox.setFont(Font.font(16)));
        daysOfWeekField.setSpacing(10);
        VBox.setMargin(daysOfWeekField, new Insets(20, 0, 0, LEFT_MARGIN));
    }

    public void setInitValues(LocalDate initDate) {
        super.setInitValues(initDate);
        DayOfWeek initDayOfWeek = initDate.getDayOfWeek();
        dayOfWeekCheckBoxes.get(initDayOfWeek.getValue()-1).selectedProperty().setValue(true);
    }

    public void showRepetitionDetail(WeeklyRepetition repetition) {
        super.showRepetitionDetail(repetition);
        for(DayOfWeek dayOfWeek : repetition.getDaysOfWeek()) {
            dayOfWeekCheckBoxes.get(dayOfWeek.getValue()-1).selectedProperty().setValue(true);
        }
    }

    public WeeklyRepetition readInput() {
        Repetition repetition = super.readInput();

        List<DayOfWeek> daysOfWeek = new ArrayList<>();
        if(daysOfWeekErrorField.getChildren().isEmpty() && repetition != null) {
            for(CheckBox checkBox : selectedDaysOfWeek) {
                int index = dayOfWeekCheckBoxes.indexOf(checkBox);
                DayOfWeek dayOfWeek = DayOfWeek.of(index+1);
                daysOfWeek.add(dayOfWeek);
            }

            WeeklyRepetition weeklyRepetition = new WeeklyRepetition(repetition.getRepetitionInterval(), daysOfWeek);

            return weeklyRepetition;
        }

        return null;
    }
}
