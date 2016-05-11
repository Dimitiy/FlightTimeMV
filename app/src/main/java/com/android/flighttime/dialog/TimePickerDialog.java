package com.android.flighttime.dialog;

import android.app.*;
import android.os.Bundle;
import android.widget.TimePicker;

import com.android.flighttime.R;
import com.android.flighttime.listener.TimePickerListener;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by OldMan on 10.05.2016.
 */
public class TimePickerDialog extends android.support.v4.app.DialogFragment
        implements android.app.TimePickerDialog.OnTimeSetListener {
    private int countTime = 0;
    private static TimePickerListener callback;

    public static TimePickerDialog newInstance(TimePickerListener listener) {
        final TimePickerDialog frag = new TimePickerDialog();
        callback = listener;
        return frag;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // определяем текущую дату
        Dialog dialog = new android.app.TimePickerDialog(getActivity(), R.style.MyDatePickerDialogTheme, this, countTime, countTime, true);
        dialog.setTitle(getResources().getString(R.string.choose_flight_time));
        return dialog;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        callback.onSelectTimeCount(hourOfDay, minute);
    }
}
