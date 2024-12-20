package com.peoplestrong.timeoff.encashment.pojo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

    public static String dateToString(Date date) {
        return DATE_FORMAT.format(date);
    }

    public static Date stringToDate(String dateString) throws ParseException {
        return DATE_FORMAT.parse(dateString);
    }

    public static int compareDates(String dateStr1, String dateStr2) throws ParseException {
        Date date1 = stringToDate(dateStr1);
        Date date2 = stringToDate(dateStr2);
        return date1.compareTo(date2);
    }

    public static Boolean isNextDay(String previousEndDateStr, String currentStartDateStr) throws ParseException {
        Date previousEndDate = stringToDate(previousEndDateStr);
        Date currentStartDate = stringToDate(currentStartDateStr);

        long previousEndMillis = previousEndDate.getTime();
        long currentStartMillis = currentStartDate.getTime();
        long differenceInMillis = currentStartMillis - previousEndMillis;
        long oneDayInMillis = 24 * 60 * 60 * 1000;

        return differenceInMillis == oneDayInMillis;
    }
}