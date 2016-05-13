package com.android.flighttime.data;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.android.flighttime.dialog.CityIndexDialog;
import com.android.flighttime.dialog.DatePickerDialog;
import com.android.flighttime.dialog.TimePickerDialog;
import com.android.flighttime.listener.CityChangeListener;
import com.android.flighttime.listener.DatePickerListener;
import com.android.flighttime.listener.OnTaskCreateListener;
import com.android.flighttime.listener.TimePickerListener;

import java.util.Calendar;

/**
 * Created by OldMan on 10.05.2016.
 */
public class MissionCreator implements DatePickerListener, CityChangeListener {

    private Context context;
    private Calendar calendar;
    private OnTaskCreateListener callback;
    private String address;

    public MissionCreator(Context context, OnTaskCreateListener callback) {
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

    @Override
    public void onChangeCity(String city) {
        this.address = city;
        createDatePicker();
    }
}
