package com.shiz.flighttime.utils;

import android.content.Context;
import android.util.Log;

import com.shiz.flighttime.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by OldMan on 22.03.2016.
 */
public class Formatter {
    private static int COUNT_MINUTE_IN_HOUR = 3600;

    public static String getFormatTime(long timeInMillis) {
        int hours = (int) (timeInMillis / (1000 * 60 * 60));
        int minutes = (int) ((timeInMillis / (1000 * 60)) % 60);
        return hours + ":" + minutes;
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

    public static long getCountMillis(Calendar calendar) {
        long millis = (calendar.get(Calendar.HOUR) * 3600 + calendar.get(Calendar.MINUTE) * 60) * 1000;
        Log.d("Date", "minute " + Long.toString(millis));
        return millis;
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
        SimpleDateFormat sdf = new SimpleDateFormat();
        return sdf.format(date);
    }

    public static String getFormatDuration(Context context, long timeInMillis) {
        int hours = (int) (timeInMillis / (1000 * 60 * 60));
        int minutes = (int) ((timeInMillis / (1000 * 60)) % 60);
        return String.format(context.getString(R.string.format_hour), ((hours < 10) ? "0" + hours : "" + hours))
                + String.format(context.getString(R.string.format_minute), ((minutes < 10) ? ":0" + minutes : ":" + minutes));
    }
}
