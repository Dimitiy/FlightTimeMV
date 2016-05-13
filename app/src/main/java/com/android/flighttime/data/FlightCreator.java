package com.android.flighttime.data;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.android.flighttime.dialog.DatePickerDialog;
import com.android.flighttime.dialog.TimePickerDialog;
import com.android.flighttime.listener.DatePickerListener;
import com.android.flighttime.listener.OnTaskCreateListener;
import com.android.flighttime.listener.TimePickerListener;

import java.util.Calendar;

/**
 * Created by oldman on 13.05.16.
 */
public class FlightCreator implements DatePickerListener, TimePickerListener {

    private Context context;
    private Calendar calendar;
    private OnTaskCreateListener callback;
    private String address;
    private AbstractExpandableDataProvider.MissionData mission;

    public FlightCreator(AbstractExpandableDataProvider.MissionData mission, Context context, OnTaskCreateListener callback) {
        this.context = context;
        this.callback = callback;
        this.mission = mission;
        calendar = Calendar.getInstance();
        createDatePicker();
    }

    @Override
    public void onSelectDate(int year, int monthOfYear, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        createTimePicker();
    }

    @Override
    public void onSelectTimeCount(int hour, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        callback.onFlightCreated(mission, calendar);
    }


    private void createDatePicker() {
        DatePickerDialog picker = DatePickerDialog.newInstance(this);
        picker.show(((FragmentActivity) context).getSupportFragmentManager(), "date_picker");
    }

    private void createTimePicker() {
        TimePickerDialog picker = TimePickerDialog.newInstance(this);
        picker.show(((FragmentActivity) context).getSupportFragmentManager(), "time_picker");
    }
}
