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
public class RepetitionTest {

    @Nested
    @DisplayName("Tests for computing dates from day of month in method figureOutDates")
    class FigureOutDatesTests {
        @Test
        public void dailyRepetitionInOneMonthEverySecondDay() {
            List<LocalDate> expectedDates = getEveryNdateInMonthAndYear(2, 8, 2020, LocalDate.of(2020, 8, 1),
                    LocalDate.of(2020, 9, 1));

            List<LocalDate> actualDates = createDailyRepetitionAndGetDates(1L, LocalDate.of(2020, 8, 1),
                    LocalDate.of(2020, 8, 31), 2, 8, 2020);

            assertEquals(expectedDates, actualDates);
        }

        @Test
        public void dailyRepetitionInTwoMonthsEveryDay() {
            List<LocalDate> expectedDates = getEveryNdateInMonthAndYear(1, 8, 2020, LocalDate.of(2020, 8, 1),
                    LocalDate.of(2020, 9, 1));

            List<LocalDate> actualDates = createDailyRepetitionAndGetDates(1L, LocalDate.of(2020, 8, 1),
                    LocalDate.of(2020, 9, 30), 1, 8, 2020);

            assertEquals(expectedDates, actualDates);
        }

        @Test
        public void dailyRepetitionLastHalfOfMonthEveryDay() {
            List<LocalDate> expectedDates = getEveryNdateInMonthAndYear(1, 8, 2020, LocalDate.of(2020, 8, 15),
                    LocalDate.of(2020, 9, 1));

            List<LocalDate> actualDates = createDailyRepetitionAndGetDates(1L, LocalDate.of(2020, 8, 15),
                    LocalDate.of(2020, 9, 30), 1, 8, 2020);

            assertEquals(expectedDates, actualDates);
        }

        @Test
        public void dailyRepetitionFirstHalfOfMonthEveryThirdDay() {
            List<LocalDate> expectedDates = getEveryNdateInMonthAndYear(3, 8, 2020, LocalDate.of(2020, 8, 1),
                    LocalDate.of(2020, 8, 15));
            List<LocalDate> actualDates = createDailyRepetitionAndGetDates(1L, LocalDate.of(2020, 8, 1),
                    LocalDate.of(2020, 8, 15), 3, 8, 2020);

            assertEquals(expectedDates, actualDates);
        }

        @Test
        public void dailyRepetitionWithStartAndEndOutsideAugust2020() {
            List<LocalDate> expectedDates = new ArrayList<>() {
                {
                    add(LocalDate.of(2020, 8, 10));
                    add(LocalDate.of(2020, 8, 20));
                    add(LocalDate.of(2020, 8, 30));
                }
            };

            List<LocalDate> actualDates = createDailyRepetitionAndGetDates(1L, LocalDate.of(2020, 7, 1),
                    LocalDate.of(2020, 12, 31), 10, 8, 2020);

            assertEquals(expectedDates, actualDates);
        }

        @Test
        public void dailyRepetitionWithoutOccurenceInAugust2020() {
            List<LocalDate> expectedDates = new ArrayList<>();

            List<LocalDate> actualDates = createDailyRepetitionAndGetDates(1L, LocalDate.of(2020, 7, 1),
                    LocalDate.of(2020, 12, 31), 100, 8, 2020);

            assertEquals(expectedDates, actualDates);
        }

        private List<LocalDate> getEveryNdateInMonthAndYear(int N, int month, int year, LocalDate start, LocalDate end) {
            List<LocalDate> dates = new ArrayList<>();

            LocalDate minDate = LocalDate.of(year, month, 1);
            if (minDate.isBefore(start)) {
                minDate = start;
            }

            LocalDate maxDate = minDate.plusMonths(1);
            if (maxDate.isAfter(end)) {
                maxDate = end;
            }

            for (LocalDate date = minDate; date.isBefore(maxDate); date = date.plusDays(N)) {
                dates.add(date);
            }

            return dates;
        }

        private List<LocalDate> createDailyRepetitionAndGetDates(Long eventId, LocalDate start, LocalDate end,
                                                                 int repetitionInterval, int month, int year) {
            Repetition repetition = new Repetition(eventId, start, end, repetitionInterval);
            return repetition.figureOutDates(month, year);
        }
    }

    @Nested
    @DisplayName("Tests for checking given date against repetition rules")
    class CheckDateTests {
        @Test
        public void dateBeforeRepetitionStartReturnsFalse() {
            boolean expected = false;

            boolean actual = createDailyRepetitionAndCheckDate(1L, LocalDate.of(2020, 9, 15),
                    LocalDate.of(2020, 12, 15), 1, LocalDate.of(2020, 9, 1));

            assertEquals(expected, actual);
        }

        @Test
        public void dateAfterRepetitionEndReturnsFalse() {
            boolean expected = false;

            boolean actual = createDailyRepetitionAndCheckDate(1L, LocalDate.of(2020, 9, 15),
                    LocalDate.of(2020, 12, 15), 1, LocalDate.of(2020, 12, 16));

            assertEquals(expected, actual);
        }

        @Test
        public void dateBetweenTwoRepetitionDatesReturnsFalse() {
            boolean expected = false;

            boolean actual = createDailyRepetitionAndCheckDate(1L, LocalDate.of(2020, 9, 15),
                    LocalDate.of(2020, 12, 15), 2, LocalDate.of(2020, 9, 16));

            assertEquals(expected, actual);
        }

        @Test
        public void dateInRepetitionDateReturnsTrue() {
            boolean expected = true;

            boolean actual = createDailyRepetitionAndCheckDate(1L, LocalDate.of(2020, 9, 15),
                    LocalDate.of(2020, 12, 15), 3, LocalDate.of(2020, 9, 21));

            assertEquals(expected, actual);
        }

        @Test
        public void dateWithYearOverlapInRepetitionDateReturnsTrue() {
            boolean expected = true;

            boolean actual = createDailyRepetitionAndCheckDate(1L, LocalDate.of(2020, 12, 15),
                    LocalDate.of(2021, 1, 15), 3, LocalDate.of(2021, 1, 2));

            assertEquals(expected, actual);
        }

        private boolean createDailyRepetitionAndCheckDate(Long eventId, LocalDate start, LocalDate end,
                                                          int repetitionInterval, LocalDate date) {
            Repetition repetition = new Repetition(eventId, start, end, repetitionInterval);
            return repetition.checkDate(date);
        }
    }

}