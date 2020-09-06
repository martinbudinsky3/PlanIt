package com.example.client.model.repetition;

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

    public static com.example.vavaplanit.model.repetition.RepetitionType fromString(String stringType) {
        for(com.example.vavaplanit.model.repetition.RepetitionType t : com.example.vavaplanit.model.repetition.RepetitionType.values()) {
            if(t.toString().equalsIgnoreCase(stringType)) {
                return t;
            }
        }

        return null;
    }
}
