package com.example.vavaplanit.model;

public enum EventType {
    FREE_TIME, WORK, SCHOOL, OTHERS;

    @Override
    public String toString() {
        switch (this) {
            case FREE_TIME:
                return "Free time";

            case WORK:
                return "Work";

            case SCHOOL:
                return "School";

            case OTHERS:
                return "Others";

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
