package com.example.vavaplanit.model.repetition;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RepetitionTest {

    @Test
    public void dailyRepetitionInOneMonthEverySecondDay() {
        List<LocalDate> expectedDates = getEveryNdateInMonthAndYear(2, 8, 2020);
        List<LocalDate> actualDates = createDailyRepetitionAndGetDates(1L, LocalDate.of(2020, 8, 1),
                LocalDate.of(2020, 8, 31), 2, 8, 2020);

        assertEquals(expectedDates, actualDates);
    }

    @Test
    public void dailyRepetitionInTwoMonthsEveryDay() {
        List<LocalDate> expectedDates = getEveryNdateInMonthAndYear(1, 8, 2020);
        List<LocalDate> actualDates = createDailyRepetitionAndGetDates(1L, LocalDate.of(2020, 8, 1),
                LocalDate.of(2020, 9, 30), 1, 8, 2020);

        assertEquals(expectedDates, actualDates);
    }

    private List<LocalDate> getEveryNdateInMonthAndYear(int N, int month, int year) {
        List<LocalDate> dates = new ArrayList<>();

        LocalDate minDate = LocalDate.of(year, month, 1);
        LocalDate maxDate = minDate.plusMonths(1);

        for(LocalDate date = minDate; date.isBefore(maxDate); date = date.plusDays(N)) {
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