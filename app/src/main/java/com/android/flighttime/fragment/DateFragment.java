package com.android.flighttime.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.android.flighttime.R;
import com.android.flighttime.listener.CityChangeListener;
import com.android.flighttime.listener.DatePickerListener;
import com.android.flighttime.listener.TimePickerListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link android.support.v4.app.Fragment.InstantiationException} interface
 * to handle interaction events.
 * Use the {@link DateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DateFragment extends Fragment implements View.OnClickListener, android.app.DatePickerDialog.OnDateSetListener, android.app.TimePickerDialog.OnTimeSetListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final int DATE_FORMAT = 0;
    public static final int TIME_FORMAT = 1;
    public static final int BEGIN_COUNT_TIME_FLIGHT = 0;
    // TODO: Rename and change types of parameters
    private int mParam1;
    private String mParam2;
    private Calendar calendar;
    private Button editDate;
    private DatePickerListener datePickerListener;
    private TimePickerListener timePickerListener;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment DateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DateFragment newInstance(int param1) {
        DateFragment fragment = new DateFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }
    public void addDatePickerListener(DatePickerListener listener) {
        this.datePickerListener = listener;
    }
    public void addTimePickerListener(TimePickerListener listener) {
        this.timePickerListener = listener;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_date, container, false);
        editDate = (Button) view.findViewById(R.id.button);
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, BEGIN_COUNT_TIME_FLIGHT);
        calendar.set(Calendar.MINUTE, BEGIN_COUNT_TIME_FLIGHT);

        if (mParam1 == DATE_FORMAT)
            editDate.setText(getDateFormat(DATE_FORMAT));
        else
            editDate.setText(getDateFormat(TIME_FORMAT));
        editDate.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (mParam1 == DATE_FORMAT) {
            Dialog picker = new android.app.DatePickerDialog(getActivity(), R.style.MyDatePickerDialogTheme, this,
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            picker.setTitle(getResources().getString(R.string.choose_flight_date));
            picker.show();
        } else {
            Dialog timePickerDialog = new android.app.TimePickerDialog(getActivity(), R.style.MyDatePickerDialogTheme, this, BEGIN_COUNT_TIME_FLIGHT, BEGIN_COUNT_TIME_FLIGHT, true);
            timePickerDialog.setTitle(getResources().getString(R.string.choose_flight_time));
            timePickerDialog.show();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(year, monthOfYear, dayOfMonth);
        editDate.setText(getDateFormat(DATE_FORMAT));
        datePickerListener.onSelectDate(calendar);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        editDate.setText(getDateFormat(TIME_FORMAT));
        timePickerListener.onSelectTimeCount(calendar);
    }

    private String getDateFormat(int format) {
        if (TIME_FORMAT == format) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return sdf.format(calendar.getTime());
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
            return sdf.format(calendar.getTime());
        }
    }


}
