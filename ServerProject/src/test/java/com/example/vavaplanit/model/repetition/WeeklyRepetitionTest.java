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
        public void _3IsMondayAndTuesday() {
            List<DayOfWeek> expectedValue = createDayOfWeekList(DayOfWeek.MONDAY, DayOfWeek.TUESDAY);

            List<DayOfWeek> actualValue = createWeeklyRepetitionAndSetDaysOfWeek(3);

            assertEquals(expectedValue, actualValue);
        }

        @Test
        public void _70IsTuesdayAndWednesdayAndSunday() {
            List<DayOfWeek> expectedValue = createDayOfWeekList(DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.SUNDAY);

            List<DayOfWeek> actualValue = createWeeklyRepetitionAndSetDaysOfWeek(70);

            assertEquals(expectedValue, actualValue);
        }

        private List<DayOfWeek> createDayOfWeekList(DayOfWeek ...daysOfWeek) {
            List<DayOfWeek> expectedValue = new ArrayList<>();
            for(DayOfWeek dayOfWeek : daysOfWeek) {
                expectedValue.add(dayOfWeek);
            }

            return expectedValue;
        }

        private List<DayOfWeek> createWeeklyRepetitionAndSetDaysOfWeek(int daysOfWeek) {
            WeeklyRepetition weeklyRepetition = new WeeklyRepetition();
            weeklyRepetition.setDaysOfWeek(daysOfWeek);

            return weeklyRepetition.getDaysOfWeek();
        }
    }

}