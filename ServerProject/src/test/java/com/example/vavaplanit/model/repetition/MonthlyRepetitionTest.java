package com.example.vavaplanit.model.repetition;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MonthlyRepetitionTest {

    @Nested
    @DisplayName("Tests for computing dates in method figureOutDates")
    class FigureOutDatesTests {

        @Nested
        @DisplayName("Tests for computing dates from day of month")
        class FigureOutDatesFromDayOfMonthTests {
            @Test
            public void monthlyRepetitionEveryMonthOn20th() {
                LocalDate expectedDate = LocalDate.of(2020, 8, 20);

                LocalDate actualDate = createMonthlyRepetitionAndGetDates(1L, LocalDate.of(2020, 6, 20),
                        LocalDate.of(2020, 12, 20), 1, null, 20,
                        null, 8, 2020).get(0);

                assertEquals(expectedDate, actualDate);
            }

            @Test
            public void monthlyRepetitionEveryMonthOn31st() {
                LocalDate expectedDate = LocalDate.of(2020, 2, 29);

                LocalDate actualDate = createMonthlyRepetitionAndGetDates(1L, LocalDate.of(2020, 1, 31),
                        LocalDate.of(2020, 12, 31), 1, null, 31,
                        null, 2, 2020).get(0);

                assertEquals(expectedDate, actualDate);
            }

            @Test
            public void monthlyRepetitionEverySecondMonthWithoutOccurrenceInMonth() {
                List<LocalDate> expectedDate = new ArrayList<>();

                List<LocalDate> actualDate = createMonthlyRepetitionAndGetDates(1L, LocalDate.of(2020, 1, 31),
                        LocalDate.of(2020, 12, 31), 2, null, 31,
                        null, 2, 2020);

                assertEquals(expectedDate, actualDate);
            }

            @Test
            public void monthlyRepetitionEvery13thMonthWithOccurrenceInMonth() {
                LocalDate expectedDate = LocalDate.of(2020, 8, 15);

                LocalDate actualDate = createMonthlyRepetitionAndGetDates(1L, LocalDate.of(2019, 7, 15),
                        LocalDate.of(2020, 12, 15), 13, null, 15,
                        null, 8, 2020).get(0);

                assertEquals(expectedDate, actualDate);
            }
        }

        @Nested
        @DisplayName("Tests for computing dates from ordinal of day of week")
        class FigureOutDatesFromOrdinalAndDayOfWeekTests {
            @Test
            public void monthlyRepetitionEveryMonthOnLastMonday() {
                LocalDate expectedDate = LocalDate.of(2020, 8, 31);

                LocalDate actualDate = createMonthlyRepetitionAndGetDates(1L, LocalDate.of(2020, 7, 27),
                        LocalDate.of(2020, 12, 28), 1, 1, null,
                        5, 8, 2020).get(0);

                assertEquals(expectedDate, actualDate);
            }

            @Test
            public void monthlyRepetitionEveryMonthOnFirstFriday() {
                LocalDate expectedDate = LocalDate.of(2020, 8, 7);

                LocalDate actualDate = createMonthlyRepetitionAndGetDates(1L, LocalDate.of(2020, 7, 3),
                        LocalDate.of(2020, 12, 4), 1, 16, null,
                        1, 8, 2020).get(0);

                assertEquals(expectedDate, actualDate);
            }

            @Test
            public void monthlyRepetitionEveryMonthOn2ndWednesday() {
                LocalDate expectedDate = LocalDate.of(2020, 8, 12);

                LocalDate actualDate = createMonthlyRepetitionAndGetDates(1L, LocalDate.of(2020, 7, 8),
                        LocalDate.of(2020, 12, 9), 1, 4, null,
                        2, 8, 2020).get(0);

                assertEquals(expectedDate, actualDate);
            }

            @Test
            public void monthlyRepetitionEvery2ndMonthOn3rdThursday() {
                LocalDate expectedDate = LocalDate.of(2020, 8, 20);

                LocalDate actualDate = createMonthlyRepetitionAndGetDates(1L, LocalDate.of(2020, 6, 18),
                        LocalDate.of(2020, 12, 17), 2, 8, null,
                        3, 8, 2020).get(0);

                assertEquals(expectedDate, actualDate);
            }

            @Test
            public void monthlyRepetitionEvery2ndMonthWithoutOccurrenceInMonth() {
                List<LocalDate> expectedDate = new ArrayList<>();

                List<LocalDate> actualDate = createMonthlyRepetitionAndGetDates(1L, LocalDate.of(2020, 7, 16),
                        LocalDate.of(2020, 11, 19), 2, 8, null,
                        3, 8, 2020);

                assertEquals(expectedDate, actualDate);
            }
        }

        private List<LocalDate> createMonthlyRepetitionAndGetDates(Long eventId, LocalDate start, LocalDate end,
                                                                   int repetitionInterval, Integer daysOfWeek, Integer dayOfMonth,
                                                                   Integer ordinal, int month, int year) {

            MonthlyRepetition repetition = new MonthlyRepetition(eventId, start, end, repetitionInterval, daysOfWeek,
                    dayOfMonth, ordinal);

            return repetition.figureOutDates(month, year);
        }
    }

    @Nested
    @DisplayName("Tests for checking given date against repetition rules")
    class CheckDateTests {

        @Test
        public void dateBeforeRepetitionStartReturnsFalse() {
            boolean expected = false;

            boolean actual = createMonthlyRepetitionAndCheckDate(1L, LocalDate.of(2020, 9, 15),
                    LocalDate.of(2020, 12, 15), 1, 2, null, 3,
                    LocalDate.of(2020, 9, 1));

            assertEquals(expected, actual);
        }

        @Test
        public void dateAfterRepetitionEndReturnsFalse() {
            boolean expected = false;

            boolean actual = createMonthlyRepetitionAndCheckDate(1L, LocalDate.of(2020, 9, 15),
                    LocalDate.of(2020, 12, 15), 1, null, 15, null,
                    LocalDate.of(2020, 12, 16));

            assertEquals(expected, actual);
        }

        @Nested
        @DisplayName("Repetition with day of month tests")
        class FigureOutDatesFromDayOfMonthTests {

        }

        @Nested
        @DisplayName("Repetition with ordinal of day of week tests")
        class FigureOutDatesFromOrdinalAndDayOfWeekTests {

        }

        private boolean createMonthlyRepetitionAndCheckDate(Long eventId, LocalDate start, LocalDate end,
                                                                   int repetitionInterval, Integer daysOfWeek, Integer dayOfMonth,
                                                                   Integer ordinal, LocalDate date) {

            MonthlyRepetition repetition = new MonthlyRepetition(eventId, start, end, repetitionInterval, daysOfWeek,
                    dayOfMonth, ordinal);

            return repetition.checkDate(date);
        }
    }
}