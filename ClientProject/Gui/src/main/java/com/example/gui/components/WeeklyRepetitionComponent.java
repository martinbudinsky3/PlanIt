package com.example.gui.components;

import javafx.scene.control.Label;

public class WeeklyRepetitionComponent extends DailyRepetitionComponent {
    private final Label secondLabel = new Label("weeks");

    public WeeklyRepetitionComponent() {
        initRepetitionIntervalField();
    }
}
