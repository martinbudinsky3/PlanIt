package com.example.vavaplanit.model.repetition;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.DayOfWeek;
import java.time.LocalDate;
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

    @Nested
    @DisplayName("Tests for computing dates in method figureOutDates")
    class FigureOutDatesTests {
        @Test
        public void weeklyRepetitionEveryWeekOnMondayAndFriday() {
            List<LocalDate> expectedDates = new ArrayList<>() {
                {
                    add(LocalDate.of(2020, 8, 3));
                    add(LocalDate.of(2020, 8, 7));
                    add(LocalDate.of(2020, 8, 10));
                    add(LocalDate.of(2020, 8, 14));
                    add(LocalDate.of(2020, 8, 17));
                    add(LocalDate.of(2020, 8, 21));
                    add(LocalDate.of(2020, 8, 24));
                    add(LocalDate.of(2020, 8, 28));
                    add(LocalDate.of(2020, 8, 31));
                }
            };

            List<LocalDate> actualDates = createWeeklyRepetitionAndGetDates(1, LocalDate.of(2020, 7, 3),
                    LocalDate.of(2020, 9, 1), 1, 17, 8, 2020);

            assertEquals(expectedDates, actualDates);
        }

        @Test
        public void weeklyRepetitionEvery2WeeksOnTuesdayAndWednesdayAndSaturday() {
            List<LocalDate> expectedDates = new ArrayList<>() {
                {
                    add(LocalDate.of(2020, 8, 1));
                    add(LocalDate.of(2020, 8, 11));
                    add(LocalDate.of(2020, 8, 12));
                    add(LocalDate.of(2020, 8, 15));
                    add(LocalDate.of(2020, 8, 25));
                    add(LocalDate.of(2020, 8, 26));
                    add(LocalDate.of(2020, 8, 29));
                }
            };

            List<LocalDate> actualDates = createWeeklyRepetitionAndGetDates(1, LocalDate.of(2020, 7, 1),
                    LocalDate.of(2020, 9, 1), 2, 38, 8, 2020);

            assertEquals(expectedDates, actualDates);
        }

        @Test
        public void weeklyRepetitionLastHalfOfMonthEveryWeekOnSaturday() {
            List<LocalDate> expectedDates = new ArrayList<>() {
                {
                    add(LocalDate.of(2020, 8, 15));
                    add(LocalDate.of(2020, 8, 22));
                    add(LocalDate.of(2020, 8, 29));
                }
            };

            List<LocalDate> actualDates = createWeeklyRepetitionAndGetDates(1, LocalDate.of(2020, 8, 15),
                    LocalDate.of(2020, 9, 1), 1, 32, 8, 2020);

            assertEquals(expectedDates, actualDates);
        }

        @Test
        public void weeklyRepetitionFirstHalfOfMonthEveryWeekOnThursdayAndSunday() {
            List<LocalDate> expectedDates = new ArrayList<>() {
                {
                    add(LocalDate.of(2020, 8, 2));
                    add(LocalDate.of(2020, 8, 6));
                    add(LocalDate.of(2020, 8, 9));
                    add(LocalDate.of(2020, 8, 13));
                }
            };

            List<LocalDate> actualDates = createWeeklyRepetitionAndGetDates(1, LocalDate.of(2020, 7, 1),
                    LocalDate.of(2020, 8, 15), 1, 72, 8, 2020);

            assertEquals(expectedDates, actualDates);
        }

        @Test
        public void weeklyRepetitionWithNoOccurrenceInAugust2020() {
            List<LocalDate> expectedDates = new ArrayList<>();

            List<LocalDate> actualDates = createWeeklyRepetitionAndGetDates(1, LocalDate.of(2020, 7, 31),
                    LocalDate.of(2020, 12, 1), 6, 16, 8, 2020);

            assertEquals(expectedDates, actualDates);
        }

        @Test
        public void weeklyRepetitionWithStartInAnotherYearEvery2Weeks() {
            List<LocalDate> expectedDates = new ArrayList<>() {
                {
                    add(LocalDate.of(2021, 1, 14));
                    add(LocalDate.of(2021, 1, 28));
                }
            };

            List<LocalDate> actualDates = createWeeklyRepetitionAndGetDates(1, LocalDate.of(2020, 12, 31),
                    LocalDate.of(2021, 2, 1), 2, 8, 1, 2021);

            assertEquals(expectedDates, actualDates);
        }

        @Test
        public void weeklyRepetitionLastHalfOfMonthEverySecondMonday() {
            List<LocalDate> expectedDates = new ArrayList<>() {
                {
                    add(LocalDate.of(2020, 8, 10));
                    add(LocalDate.of(2020, 8, 24));
                }
            };

            List<LocalDate> actualDates = createWeeklyRepetitionAndGetDates(1, LocalDate.of(2020, 8, 10),
                    LocalDate.of(2020, 9, 7), 2, 1, 8, 2020);

            assertEquals(expectedDates, actualDates);
        }

        private List<LocalDate> createWeeklyRepetitionAndGetDates(Integer eventId, LocalDate start, LocalDate end,
                                                                 int repetitionInterval, Integer daysOfWeek, int month, int year) {
            WeeklyRepetition repetition = new WeeklyRepetition(eventId, start, end, repetitionInterval, daysOfWeek);

            return repetition.figureOutDates(month, year);
        }
    }

    @Nested
    @DisplayName("Tests for checking given date against repetition rules")
    class CheckDateTests {
        @Test
        public void dateBeforeRepetitionStartReturnsFalse() {
            boolean expected = false;

            boolean actual = createWeeklyRepetitionAndCheckDate(1, LocalDate.of(2020, 9, 15),
                    LocalDate.of(2020, 12, 15), 1, 2, LocalDate.of(2020, 9, 1));

            assertEquals(expected, actual);
        }

        @Test
        public void dateAfterRepetitionEndReturnsFalse() {
            boolean expected = false;

            boolean actual = createWeeklyRepetitionAndCheckDate(1, LocalDate.of(2020, 9, 15),
                    LocalDate.of(2020, 12, 15), 1, 2, LocalDate.of(2020, 12, 16));

            assertEquals(expected, actual);
        }

        @Test
        public void dateSomewhereBetweenTwoRepetitionsReturnsFalse() {
            boolean expected = false;

            boolean actual = createWeeklyRepetitionAndCheckDate(1, LocalDate.of(2020, 9, 15),
                    LocalDate.of(2020, 12, 15), 2, 2, LocalDate.of(2020, 10, 1));

            assertEquals(expected, actual);
        }

        @Test
        public void dateExactlyBetweenTwoRepetitionsReturnsFalse() {
            boolean expected = false;

            boolean actual = createWeeklyRepetitionAndCheckDate(1, LocalDate.of(2020, 9, 15),
                    LocalDate.of(2020, 12, 15), 2, 2, LocalDate.of(2020, 9, 22));

            assertEquals(expected, actual);
        }

        @Test
        public void dateInRepetitionDateReturnsTrue() {
            boolean expected = true;

            boolean actual = createWeeklyRepetitionAndCheckDate(1, LocalDate.of(2020, 9, 15),
                    LocalDate.of(2020, 12, 15), 2, 2, LocalDate.of(2020, 10, 13));

            assertEquals(expected, actual);
        }

        @Test
        public void dateInRepetitionDateWithYearOverlapReturnsTrue() {
            boolean expected = true;

            boolean actual = createWeeklyRepetitionAndCheckDate(1, LocalDate.of(2020, 12, 15),
                    LocalDate.of(2021, 6, 15), 2, 2, LocalDate.of(2021, 1, 12));

            assertEquals(expected, actual);
        }

        private boolean createWeeklyRepetitionAndCheckDate(Integer eventId, LocalDate start, LocalDate end,
                                                                  int repetitionInterval, Integer daysOfWeek, LocalDate date) {
            WeeklyRepetition repetition = new WeeklyRepetition(eventId, start, end, repetitionInterval, daysOfWeek);

            return repetition.checkDate(date);
        }
    }
}