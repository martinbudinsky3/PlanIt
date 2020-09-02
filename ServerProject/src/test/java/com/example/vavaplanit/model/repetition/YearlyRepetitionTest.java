package com.example.vavaplanit.model.repetition;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class YearlyRepetitionTest {
    @Nested
    @DisplayName("Tests for computing dates in method figureOutDates")
    class FigureOutDatesTests {

        @Nested
        @DisplayName("Tests for computing dates from day of month")
        class FigureOutDatesFromDayOfMonthTests {
            @Test
            public void yearlyRepetitionEveryYearOn29thFebruary() {
                LocalDate expectedDate = LocalDate.of(2021, 2, 28);

                LocalDate actualDate = createYearlyRepetitionAndGetDates(1L, LocalDate.of(2020, 2, 29),
                        LocalDate.of(2022, 2, 28), 1, null, 29, null,
                        2, 2, 2021).get(0);

                assertEquals(expectedDate, actualDate);
            }

            @Test
            public void yearlyRepetitionEvery2ndYearOn15thJuly() {
                LocalDate expectedDate = LocalDate.of(2021, 7, 15);

                LocalDate actualDate = createYearlyRepetitionAndGetDates(1L, LocalDate.of(2017, 7, 15),
                        LocalDate.of(2021, 7, 15), 2, null, 15, null,
                        7, 7, 2021).get(0);

                assertEquals(expectedDate, actualDate);
            }

            @Test
            public void yearlyRepetitionEveryYearWithoutOccurrence() {
                List<LocalDate> expectedDate = new ArrayList<>();

                List<LocalDate> actualDate = createYearlyRepetitionAndGetDates(1L, LocalDate.of(2017, 7, 15),
                        LocalDate.of(2021, 7, 15), 1, null, 15, null,
                        7, 6, 2021);

                assertEquals(expectedDate, actualDate);
            }

            @Test
            public void yearlyRepetitionEvery2ndYearWithoutOccurrence() {
                List<LocalDate> expectedDate = new ArrayList<>();

                List<LocalDate> actualDate = createYearlyRepetitionAndGetDates(1L, LocalDate.of(2017, 7, 15),
                        LocalDate.of(2021, 7, 15), 2, null, 15, null,
                        7, 7, 2020);

                assertEquals(expectedDate, actualDate);
            }
        }

        @Nested
        @DisplayName("Tests for computing dates from ordinal of day of week")
        class FigureOutDatesFromOrdinalAndDayOfWeekTests {
            @Test
            public void monthlyRepetitionEveryAugustOnLastMonday() {
                LocalDate expectedDate = LocalDate.of(2020, 8, 31);

                LocalDate actualDate = createYearlyRepetitionAndGetDates(1L, LocalDate.of(2019, 8, 26),
                        LocalDate.of(2021, 8, 30), 1, 1, null,
                        5, 8,8, 2020).get(0);

                assertEquals(expectedDate, actualDate);
            }

            @Test
            public void monthlyRepetitionEveryAugustOnFirstWednesday() {
                LocalDate expectedDate = LocalDate.of(2020, 8, 5);

                LocalDate actualDate = createYearlyRepetitionAndGetDates(1L, LocalDate.of(2019, 8, 7),
                        LocalDate.of(2021, 8, 4), 1, 4, null,
                        1, 8,8, 2020).get(0);

                assertEquals(expectedDate, actualDate);
            }

            @Test
            public void monthlyRepetitionEvery2ndAugustOn3rdSunday() {
                LocalDate expectedDate = LocalDate.of(2020, 8, 16);

                LocalDate actualDate = createYearlyRepetitionAndGetDates(1L, LocalDate.of(2018, 8, 19),
                        LocalDate.of(2022, 8, 21), 2, 64, null,
                        3, 8,8, 2020).get(0);

                assertEquals(expectedDate, actualDate);
            }
        }

        private List<LocalDate> createYearlyRepetitionAndGetDates(Long eventId, LocalDate start, LocalDate end,
                                                                  int repetitionInterval, Integer daysOfWeek, Integer dayOfMonth,
                                                                  Integer ordinal, int repeatMonth, int month, int year) {

            YearlyRepetition repetition = new YearlyRepetition(eventId, start, end, repetitionInterval, daysOfWeek,
                    dayOfMonth, ordinal, repeatMonth);

            return repetition.figureOutDates(month, year);
        }
    }

    @Nested
    @DisplayName("Tests for checking given date against repetition rules")
    class CheckDateTests {

        @Test
        public void dateBeforeRepetitionStartReturnsFalse() {
            boolean expected = false;

            boolean actual = createYearlyRepetitionAndCheckDate(1L, LocalDate.of(2020, 9, 15),
                    LocalDate.of(2020, 9, 16), 1, 2, null, 3,
                    9, LocalDate.of(2020, 9, 1));

            assertEquals(expected, actual);
        }

        @Test
        public void dateAfterRepetitionEndReturnsFalse() {
            boolean expected = false;

            boolean actual = createYearlyRepetitionAndCheckDate(1L, LocalDate.of(2020, 9, 15),
                    LocalDate.of(2022, 9, 15), 1, null, 15, null,
                    9, LocalDate.of(2023, 9, 15));

            assertEquals(expected, actual);
        }

        @Nested
        @DisplayName("Repetition with day of month tests")
        class CheckDateRepetitionWithDayOfMonthTests {
            @Test
            public void dateExactlyBetweenTwoRepetitionsReturnsFalse() {
                boolean expected = false;

                boolean actual = createYearlyRepetitionAndCheckDate(1L, LocalDate.of(2020, 8, 15),
                        LocalDate.of(2024, 8, 15), 2, null, 15, null,
                        9, LocalDate.of(2021, 8, 15));

                assertEquals(expected, actual);
            }

            @Test
            public void dateSomewhereBetweenTwoRepetitionsReturnsFalse() {
                boolean expected = false;

                boolean actual = createYearlyRepetitionAndCheckDate(1L, LocalDate.of(2020, 8, 15),
                        LocalDate.of(2024, 8, 15), 2, null, 15, null,
                        8, LocalDate.of(2020, 8, 17));

                assertEquals(expected, actual);
            }

            @Test
            public void dateInRepetitionDayOfMonthAndInterval2ReturnsTrue() {
                boolean expected = true;

                boolean actual = createYearlyRepetitionAndCheckDate(1L, LocalDate.of(2020, 8, 15),
                        LocalDate.of(2026, 8, 15), 2, null, 15, null,
                        8, LocalDate.of(2024, 8, 15));

                assertEquals(expected, actual);
            }
        }

        @Nested
        @DisplayName("Repetition with ordinal of day of week tests")
        class CheckDateWithOrdinalAndDayOfWeekTests {
            @Test
            public void dateExactlyBetweenTwoRepetitionsReturnsFalse() {
                boolean expected = false;

                boolean actual = createYearlyRepetitionAndCheckDate(1L, LocalDate.of(2020, 8, 15),
                        LocalDate.of(2022, 8, 20), 2, 32, null, 3,
                        8, LocalDate.of(2021, 8, 21));

                assertEquals(expected, actual);
            }

            @Test
            public void dateSomewhereBetweenTwoRepetitionsReturnsFalse() {
                boolean expected = false;

                boolean actual = createYearlyRepetitionAndCheckDate(1L, LocalDate.of(2020, 8, 15),
                        LocalDate.of(2022, 8, 20), 1, 32, null, 3,
                        8, LocalDate.of(2021, 8, 5));

                assertEquals(expected, actual);
            }

            @Test
            public void lastMondayDateReturnsTrue() {
                boolean expected = true;

                boolean actual = createYearlyRepetitionAndCheckDate(1L, LocalDate.of(2020, 8, 31),
                        LocalDate.of(2021, 8, 30), 1, 1, null, 5,
                        8, LocalDate.of(2021, 8, 30));

                assertEquals(expected, actual);
            }

            @Test
            public void dateIn2ndThursdayAndInterval3ReturnsTrue() {
                boolean expected = true;

                boolean actual = createYearlyRepetitionAndCheckDate(1L, LocalDate.of(2020, 9, 10),
                        LocalDate.of(2026, 9, 10), 3, 8, null, 2,
                        9, LocalDate.of(2023, 9, 14));

                assertEquals(expected, actual);
            }
        }

        private boolean createYearlyRepetitionAndCheckDate(Long eventId, LocalDate start, LocalDate end,
                                                                  int repetitionInterval, Integer daysOfWeek, Integer dayOfMonth,
                                                                  Integer ordinal, int repeatMonth, LocalDate date) {

            YearlyRepetition repetition = new YearlyRepetition(eventId, start, end, repetitionInterval, daysOfWeek,
                    dayOfMonth, ordinal, repeatMonth);

            return repetition.checkDate(date);
        }
    }
}