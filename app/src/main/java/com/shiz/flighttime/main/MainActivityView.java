package com.shiz.flighttime.main;

import android.view.View;

import com.shiz.flighttime.database.FlightDB;
import com.shiz.flighttime.database.MissionDB;
import com.shiz.flighttime.data.YearEntity;
import com.shiz.flighttime.listener.DeleteDialogClickListener;

import java.util.ArrayList;
import java.util.List;

import za.co.riggaroo.materialhelptutorial.TutorialItem;

/**
 * Created by OldMan on 09.05.2016.
 */
public interface MainActivityView {
    void showProgress();

    void hideProgress();

    void setYears(ArrayList<YearEntity> yearsList);

    void setMissionItems(List<MissionDB> missionDBlList);

    void showMessageSnackbar(int message, int action, int groupPosition, int childPosition);

    void loadTutorial(ArrayList<TutorialItem> tutorialItems);

    void showAlertDialog(DeleteDialogClickListener listener);

    void showSnackBar(String message);

    void onFlightItemCreated(int id, FlightDB flight);

    void onGroupItemRemoved(int groupPosition);

    void notifyOnGroupItemRemoved();

    void onChildItemRemoved(int groupPosition, int childPosition);

    void onGroupItemPinned(int groupPosition);

    void onChildItemPinned(int groupPosition, int childPosition);

    void onItemViewClicked(View v, boolean pinned);

    void onUnderSwipeAddFlightButtonClicked(View groupPosition);

    void onUnderSwipeEditMissionButtonClicked(View groupPosition);

    void onEditFlightSwiped(int groupPosition, int childPosition);

    void notifyGroupItemRestored(int groupPosition);

}
