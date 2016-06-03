package com.android.flighttime.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by OldMan on 22.03.2016.
 */
public class Formatter {
    private static int COUNT_MINUTE_IN_HOUR = 60;

    public static String getFormatTime(long time) {
        return new SimpleDateFormat("HH:mm").format(new Date(time));
    }

    public static int getYearDate(String year) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(formatter.parse(year));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal.get(Calendar.YEAR);
    }

    public static long getCountMinute(Calendar calendar) {
        long minute = (calendar.get(Calendar.HOUR) * 3600) + (calendar.get(Calendar.MINUTE) * 60);
        Log.d("Date", "minute " + Long.toString(minute));
        return minute;
    }

    public static Date getYearDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getYearDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, date.getYear());
        cal.set(Calendar.MONTH, date.getMonth());
        cal.set(Calendar.DAY_OF_MONTH, date.getDay());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static String getTimeFormat(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(calendar.getTime());
    }

    public static String getDateFormat(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        return sdf.format(calendar.getTime());
    }

    public static String getDateFormat(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        return sdf.format(date);
    }

    public static String getFormatDuration(long duration) {
       Log.d("FORMATTER",  String.valueOf(duration/COUNT_MINUTE_IN_HOUR) + String.valueOf(duration % COUNT_MINUTE_IN_HOUR));

        return String.valueOf(duration/COUNT_MINUTE_IN_HOUR) + ":" + String.valueOf(duration % COUNT_MINUTE_IN_HOUR) ;

    }
}
