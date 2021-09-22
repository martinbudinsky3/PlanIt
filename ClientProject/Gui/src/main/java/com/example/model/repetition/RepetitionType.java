package com.example.model.repetition;

public enum RepetitionType {
    DAILY, WEEKLY, MONTHLY, YEARLY;

    @Override
    public String toString() {
        switch (this) {
            case DAILY:
                return "daily";

            case WEEKLY:
                return "weekly";

            case MONTHLY:
                return "monthly";

            case YEARLY:
                return "yearly";

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
