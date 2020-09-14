package com.example.utils;

import java.text.DateFormatSymbols;
import java.util.*;

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

    public static List<String> getDayNames(ResourceBundle resourceBundle) {
        DateFormatSymbols symbols = new DateFormatSymbols(resourceBundle.getLocale());
        List<String> dayNamesWrongOrder = Arrays.asList(symbols.getWeekdays());

        List<String> dayNames = new ArrayList<String>(dayNamesWrongOrder.subList(2, dayNamesWrongOrder.size()));
        dayNames.add(dayNamesWrongOrder.get(1));

        return dayNames;
    }
}
