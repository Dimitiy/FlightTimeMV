package com.android.flighttime.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by OldMan on 22.03.2016.
 */
public class DateFormatter {
    private static int COUNT_MILLISECOND_IN_MINUTE = 60000;
    public static String getFormatTime(long time){
        return new SimpleDateFormat("HH:mm").format(new Date(time));
    }

    public static int getDate(String year) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(formatter.parse(year));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal.get(Calendar.YEAR);
    }

    public static long getCountMinute(Calendar calendar){
        long minute = calendar.getTimeInMillis()/COUNT_MILLISECOND_IN_MINUTE;
        Log.d("Date", "minute " + Long.toString(minute));
       return minute;
    }
    public static Date getDate(int year, int month, int day) {
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
    public static Date getDate(Date date){
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

}
