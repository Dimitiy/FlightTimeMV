package com.shiz.flighttime.main;

import com.shiz.flighttime.database.MissionDB;

/**
 * Created by OldMan on 10.05.2016.
 */
public interface MainActivityPresenter {
    void onResume();

    void onFindYearsItems();

    void onMissionItems(String year);

    void navigateToCreateMission();


    void navigateToCreateFlight(MissionDB mission);

    void navigateToChangeMission(MissionDB missionDB, int groupPosition);

    void navigateToChangeFlight(MissionDB mission, int groupPosition, int childPosition);


    void onDeleteMission(int groupPosition);

    void onDeleteFlight(int groupPosition, int childPosition);

    void onDestroy();

    void onGetTutorialItems();
}
