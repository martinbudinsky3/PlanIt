package com.example.vavaplanit.model.repetition;

import com.example.vavaplanit.model.Event;

public enum RepetitionType {
    DAILY, WEEKLY, MONTHLY, YEARLY;

    @Override
    public String toString() {
        switch (this) {
            case DAILY:
                return "Daily";

            case WEEKLY:
                return "Weekly";

            case MONTHLY:
                return "Monthly";

            case YEARLY:
                return "Yearly";

            default:
                return "";
        }
    }

    public static RepetitionType fromString(String stringType) {
        for(RepetitionType t : RepetitionType.values()) {
            if(t.toString().equalsIgnoreCase(stringType)) {
                return t;
            }
        }

        return null;
    }
}
