package com.android.flighttime.main;

import android.view.View;

import com.android.flighttime.database.FlightDB;
import com.android.flighttime.database.MissionDB;
import com.android.flighttime.listener.DatePickerListener;
import com.android.flighttime.listener.TimePickerListener;
import com.roughike.swipeselector.SwipeItem;

import java.util.List;

/**
 * Created by OldMan on 09.05.2016.
 */
public interface MainView {
    void showProgress();

    void hideProgress();

    void setYears(SwipeItem[] swipeItems);

    void setMissionItems(List<MissionDB> missionDBlList);

    void showMessageSnackbar(int  message, int action, int groupPosition, int childPosition);

    void showAlertDialog(String message);

    void showSnackBar(String message);

    void onFlightItemCreated(int id, FlightDB flight);

    void onGroupItemRemoved(int groupPosition);

    void onChildItemRemoved(int groupPosition, int childPosition);

    void onGroupItemPinned(int groupPosition);

    void onChildItemPinned(int groupPosition, int childPosition);

    void onItemViewClicked(View v, boolean pinned);

    void onUnderSwipeAddFlightButtonClicked(int groupPosition, View v);

    void onUnderSwipeEditMissionButtonClicked(View v);

}
