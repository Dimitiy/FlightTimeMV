package com.android.flighttime.main;

import android.view.View;

import com.android.flighttime.data.YearEntity;
import com.android.flighttime.database.FlightDB;
import com.android.flighttime.database.MissionDB;
import com.android.flighttime.listener.DatePickerListener;
import com.android.flighttime.listener.DeleteDialogClickListener;
import com.android.flighttime.listener.TimePickerListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OldMan on 09.05.2016.
 */
public interface MainActivityView {
    void showProgress();

    void hideProgress();

    void setYears(ArrayList<YearEntity> yearsList);

    void setMissionItems(List<MissionDB> missionDBlList);

    void showMessageSnackbar(int  message, int action, int groupPosition, int childPosition);

    void showAlertDialog(DeleteDialogClickListener  listener);

    void showSnackBar(String message);

    void onFlightItemCreated(int id, FlightDB flight);

    void onGroupItemRemoved(int groupPosition);

    void onChildItemRemoved(int groupPosition, int childPosition);

    void onGroupItemPinned(int groupPosition);

    void onChildItemPinned(int groupPosition, int childPosition);

    void onItemViewClicked(View v, boolean pinned);

    void onUnderSwipeAddFlightButtonClicked(int groupPosition);

    void onUnderSwipeEditMissionButtonClicked(int groupPosition);

    void onEditFlightSwiped(int groupPosition, int childPosition);

    void notifyGroupItemRestored(int groupPosition);

}
