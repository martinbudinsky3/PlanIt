package com.example.gui.dataitems;

import com.example.model.repetition.RepetitionType;

import java.util.ResourceBundle;

public class RepetitionTypeItem {
    private RepetitionType type;
    private ResourceBundle resourceBundle;

    public RepetitionTypeItem(RepetitionType type, ResourceBundle resourceBundle) {
        this.type = type;
        this.resourceBundle = resourceBundle;
    }

    public RepetitionType getType() {
        return type;
    }

    @Override
    public String toString() {
        return resourceBundle.getString(type.toString());
    }
}
