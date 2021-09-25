package com.example.gui.dataitems;

import com.example.model.EventType;

import java.util.ResourceBundle;

public class EventTypeItem {
    private EventType type;
    private ResourceBundle resourceBundle;

    public EventTypeItem(EventType type, ResourceBundle resourceBundle) {
        this.type = type;
        this.resourceBundle = resourceBundle;
    }

    public EventType getType() {
        return type;
    }

    @Override
    public String toString() {
        return resourceBundle.getString(type.toString());
    }
}
