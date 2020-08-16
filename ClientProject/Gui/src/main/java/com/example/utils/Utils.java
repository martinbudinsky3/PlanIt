package com.example.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Utils {
    private static int countFirstDayOfMonth(int selectedYear, int selectedMonth) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar(selectedYear, selectedMonth - 1, 1);
        int firstDayOfMonth = gregorianCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (firstDayOfMonth == 0) {
            firstDayOfMonth = 7;
        }

        return firstDayOfMonth;
    }

    public static int countRowIndexInCalendar(int day, int selectedYear, int selectedMonth) {
        int firstDayOfMonth = countFirstDayOfMonth(selectedYear, selectedMonth);

        return (day - 1 + firstDayOfMonth - 1) / 7 + 1;
    }

    public static int countColumnIndexInCalendar(int day, int selectedYear, int selectedMonth) {
        int firstDayOfMonth = countFirstDayOfMonth(selectedYear, selectedMonth);

        return (day - 1 + firstDayOfMonth - 1) % 7;
    }
}
