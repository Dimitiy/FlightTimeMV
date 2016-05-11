package com.android.flighttime.main;

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

    void setItems(List<MissionDB> missionDBlList);

    void showMessage(String message);

    void showAlertDialog(String message);

    void showSnackBar(String message);

}
