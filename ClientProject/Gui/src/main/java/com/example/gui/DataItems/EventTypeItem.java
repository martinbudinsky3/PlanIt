package com.example.gui.DataItems;

import com.example.client.model.Event;

import java.util.ResourceBundle;

public class EventTypeItem {
    private Event.Type type;
    private ResourceBundle resourceBundle;

    public EventTypeItem(Event.Type type, ResourceBundle resourceBundle) {
        this.type = type;
        this.resourceBundle = resourceBundle;
    }

    public Event.Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return resourceBundle.getString(type.toString());
    }
}
