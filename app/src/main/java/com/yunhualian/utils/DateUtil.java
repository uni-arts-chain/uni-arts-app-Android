package com.yunhualian.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static String dateToStringWithAll(Long date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static String dateToStringWithZh(Long date) {
        return new SimpleDateFormat("MM月dd日").format(date);
    }

    public static String dateToStringWithZhYear(Long date) {
        return new SimpleDateFormat("yyyy年MM月dd日").format(date);
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

    public static Long getTomorrowCurrentTime() {
        return System.currentTimeMillis() + (24 * 3600 * 1000);
    }

    public static String getCurrentSecondTime() {
        long time = System.currentTimeMillis() / 1000;

        return String.valueOf(time);
    }

    /*
     * 将时间转换为时间戳(秒)
     */
    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime() / 1000;
        res = String.valueOf(ts);
        return res;
    }
}
