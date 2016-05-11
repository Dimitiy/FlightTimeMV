package com.android.flighttime.dialog;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.android.flighttime.listener.CityChangeListener;
import com.android.flighttime.listener.DatePickerListener;
import com.android.flighttime.listener.OnMissionCreateListener;
import com.android.flighttime.listener.TimePickerListener;

import java.util.Calendar;

/**
 * Created by OldMan on 10.05.2016.
 */
public class MissionCreator implements DatePickerListener, TimePickerListener, CityChangeListener {

    private Context context;
    private Calendar calendar;
    private OnMissionCreateListener callback;
    private String address;

    public MissionCreator(Context context, OnMissionCreateListener callback) {
        this.context = context;
        this.callback = callback;
        calendar = Calendar.getInstance();
        createNameTheCity();
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
        callback.onMissionCreated(address, calendar);
    }


    private void createNameTheCity() {
        CityIndexDialog cityDialog = CityIndexDialog.newInstance(this);
        cityDialog.show(((FragmentActivity) context).getSupportFragmentManager(), "city_picker");
    }

    private void createDatePicker() {
        DatePickerDialog picker = DatePickerDialog.newInstance(this);
        picker.show(((FragmentActivity) context).getSupportFragmentManager(), "date_picker");
    }

    private void createTimePicker() {
        TimePickerDialog picker = TimePickerDialog.newInstance(this);
        picker.show(((FragmentActivity) context).getSupportFragmentManager(), "time_picker");
    }

    @Override
    public void onChangeCity(String city) {
        this.address = city;
        createDatePicker();
    }
}
