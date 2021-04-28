package com.yunhualian.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtil {

    public static String dateToStringWithAll(Long date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static String dateToStringWith(Long date) {
        return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date);
    }

    public static String dateToStringWithAllt(Long date) {
        return new SimpleDateFormat("HH:mm MM/dd").format(date);
    }

    public static String dateToStringWithAllDate(Long date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public static String dateToStringWithTime(Long date) {
        return new SimpleDateFormat("HH:mm:ss").format(date);
    }

    public static String dateToStringWithSeconds(Long date) {
        return new SimpleDateFormat("mm:ss").format(date);
    }

    public static Long getToday() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTimeInMillis();
    }

    public static Long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public static String getCurrentSecondTime() {
        long time = System.currentTimeMillis() / 1000;

        return String.valueOf(time);
    }
}
