package com.example.model;

public enum EventType {
    FREE_TIME, WORK, SCHOOL, OTHERS;

    @Override
    public String toString() {
        switch (this) {
            case FREE_TIME:
                return "freeTime";

            case WORK:
                return "work";

            case SCHOOL:
                return "school";

            case OTHERS:
                return "others";

            default:
                return "";
        }
    }

    public static EventType fromString(String stringType) {
        for(EventType t : EventType.values()) {
            if(t.toString().equalsIgnoreCase(stringType)) {
                return t;
            }
        }

        return null;
    }
}
