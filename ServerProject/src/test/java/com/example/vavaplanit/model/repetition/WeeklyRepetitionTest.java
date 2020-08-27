package com.example.vavaplanit.model.repetition;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class WeeklyRepetitionTest {

    @Nested
    @DisplayName("Tests for extracting days of week from int in setter method")
    class SetterTests {
        @Test
        public void _3isMondayAndTuesday() {
            List<DayOfWeek> expectedValue = createDayOfWeekList(DayOfWeek.MONDAY, DayOfWeek.TUESDAY);

            List<DayOfWeek> actualValue = createWeeklyRepetitionAndSetDaysOfWeek(3);

            assertEquals(expectedValue, actualValue);
        }

        @Test
        public void _70isTuesdayAndWednesdayAndSunday() {
            List<DayOfWeek> expectedValue = createDayOfWeekList(DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.SUNDAY);

            List<DayOfWeek> actualValue = createWeeklyRepetitionAndSetDaysOfWeek(70);

            assertEquals(expectedValue, actualValue);
        }

        private List<DayOfWeek> createWeeklyRepetitionAndSetDaysOfWeek(int daysOfWeek) {
            WeeklyRepetition weeklyRepetition = new WeeklyRepetition();
            weeklyRepetition.setDaysOfWeek(daysOfWeek);

            return weeklyRepetition.getDaysOfWeek();
        }
    }

    @Nested
    @DisplayName("Tests for extracting int from days of week in getter method")
    class GetterTests {
        @Test
        public void wednesdayAndThursdayis12() {
            int expectedValue = 12;

            int actualValue = createWeeklyRepetitionSetDaysOfWeekAndGetIntValue(
                    createDayOfWeekList(DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY));

            assertEquals(expectedValue, actualValue);
        }

        @Test
        public void mondayAndFridayAndSaturdayIs() {
            int expectedValue = 49;

            int actualValue = createWeeklyRepetitionSetDaysOfWeekAndGetIntValue(
                    createDayOfWeekList(DayOfWeek.MONDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY));

            assertEquals(expectedValue, actualValue);
        }

        private int createWeeklyRepetitionSetDaysOfWeekAndGetIntValue(List<DayOfWeek> daysOfWeek) {
            WeeklyRepetition weeklyRepetition = new WeeklyRepetition();
            weeklyRepetition.setDaysOfWeek(daysOfWeek);

            return weeklyRepetition.getDaysOfWeekInt();
        }
    }

    private List<DayOfWeek> createDayOfWeekList(DayOfWeek ...daysOfWeek) {
        List<DayOfWeek> expectedValue = new ArrayList<>();
        for(DayOfWeek dayOfWeek : daysOfWeek) {
            expectedValue.add(dayOfWeek);
        }

        return expectedValue;
    }
}