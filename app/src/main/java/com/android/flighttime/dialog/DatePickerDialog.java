package com.android.flighttime.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import com.android.flighttime.R;
import com.android.flighttime.listener.DatePickerListener;

import java.util.Calendar;

/**
 * Created by OldMan on 10.05.2016.
 */
public class DatePickerDialog extends android.support.v4.app.DialogFragment
        implements android.app.DatePickerDialog.OnDateSetListener {
    private Calendar calendar;
    private static DatePickerListener callback;

    public static DatePickerDialog newInstance(DatePickerListener listener) {
        final DatePickerDialog frag = new DatePickerDialog();
        callback = listener;
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // определяем текущую дату
        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // создаем DatePickerDialog и возвращаем его
        Dialog picker = new android.app.DatePickerDialog(getActivity(), R.style.MyDatePickerDialogTheme, this,
                year, month, day);
        picker.setTitle(getResources().getString(R.string.choose_date));
        return picker;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        callback.onSelectDate(year, monthOfYear, dayOfMonth);
    }
}
