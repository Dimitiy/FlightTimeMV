package com.shiz.flighttime.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.shiz.flighttime.R;
import com.shiz.flighttime.listener.DatePickerListener;
import com.shiz.flighttime.listener.TimePickerListener;
import com.shiz.flighttime.utils.Constants;
import com.shiz.flighttime.utils.Formatter;

import java.util.Calendar;
import java.util.Date;

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
    private static final String pickerType = "type_picker";
    private static final String previousDate = "date";
    private static final String previousDuration = "duration";

    // TODO: Rename and change types of parameters
    private int datePickerType;
    private long prevDuration = 0;
    private Date prevDate;
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
        args.putInt(pickerType, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public static DateFragment newInstance(int param1, Date param2, long param3) {
        DateFragment fragment = new DateFragment();
        Bundle args = new Bundle();
        Log.d("DateFragment", "param1" + Integer.toString(param1));
        args.putInt(pickerType, param1);
        args.putSerializable(previousDate, param2);
        args.putLong(previousDuration, param3);

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
            datePickerType = getArguments().getInt(pickerType);
            prevDate = (Date) getArguments().getSerializable(previousDate);
            prevDuration = getArguments().getLong(previousDuration);
            Log.d("DateFragment", "param" + Integer.toString(datePickerType));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_date, container, false);
        editDate = (Button) view.findViewById(R.id.button);
        calendar = Calendar.getInstance();
        if (prevDate != null) {
            editDate.setText(Formatter.getDateFormat(prevDate));
        } else if (prevDuration == 0L) {
            calendar.set(Calendar.HOUR, Constants.BEGIN_COUNT_TIME_FLIGHT);
            calendar.set(Calendar.MINUTE, Constants.BEGIN_COUNT_TIME_FLIGHT);
            if (datePickerType == Constants.DATE_FORMAT)
                editDate.setText(Formatter.getDateFormat(calendar));
            else if (datePickerType == Constants.TIME_FORMAT) {
                Log.d("DateFragment", "TIME_FORMAT" + Integer.toString(datePickerType));
                editDate.setText(Formatter.getTimeFormat(calendar));
            }
        } else {
            editDate.setText(Formatter.getFormatTime(prevDuration));
        }
        editDate.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (datePickerType == Constants.DATE_FORMAT) {
            Dialog picker = new android.app.DatePickerDialog(getActivity(), R.style.MyDatePickerDialogTheme, this,
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            picker.setTitle(getResources().getString(R.string.choose_flight_date));
            picker.show();
        } else if (datePickerType == Constants.TIME_FORMAT) {
            Dialog timePickerDialog = new android.app.TimePickerDialog(getActivity(), R.style.MyDatePickerDialogTheme, this, Constants.BEGIN_COUNT_TIME_FLIGHT, Constants.BEGIN_COUNT_TIME_FLIGHT, true);
            timePickerDialog.setTitle(getResources().getString(R.string.choose_flight_time));
            timePickerDialog.show();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(year, monthOfYear, dayOfMonth);
        editDate.setText(Formatter.getDateFormat(calendar));
        datePickerListener.onSelectDate(calendar);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        editDate.setText(Formatter.getTimeFormat(calendar));
        timePickerListener.onSelectTimeCount(Formatter.getCountMillis(calendar));
    }


}
