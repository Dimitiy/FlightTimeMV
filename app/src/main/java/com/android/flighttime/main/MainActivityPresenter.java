package com.android.flighttime.main;

import android.view.View;

import com.android.flighttime.data.AbstractExpandableDataProvider;
import com.android.flighttime.database.MissionDB;

/**
 * Created by OldMan on 10.05.2016.
 */
public interface MainActivityPresenter {
    void onResume();

    void onMissionItems(String year);

    void navigateToCreateMission();


    void navigateToCreateFlight(MissionDB mission);

    void navigateToChangeMission(MissionDB missionDB, int groupPosition);

    void navigateToChangeFlight(MissionDB mission, int groupPosition,  int childPosition);


    void onDeleteMission(int groupPosition);

    void onDeleteFlight(int groupPosition, int childPosition);

    void onDestroy();
}