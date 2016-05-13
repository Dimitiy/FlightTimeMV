package com.android.flighttime.main;

import android.view.View;

import com.android.flighttime.data.AbstractExpandableDataProvider;
import com.roughike.swipeselector.SwipeItem;

/**
 * Created by OldMan on 10.05.2016.
 */
public interface MainPresenter {
    void onResume();

    void onMissionItems(String year);

    void navigateToCreateMission(View v);

    void navigateToCreateFlight(AbstractExpandableDataProvider.MissionData mission, View v);

    void navigateToChangeMission();

    void onDeleteMission(int groupPosition);

    void onDeleteFlight(int groupPosition, int childPosition);


    void onDestroy();
}
