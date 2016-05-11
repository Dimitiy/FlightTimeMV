package com.android.flighttime.main;

import android.view.View;

import com.roughike.swipeselector.SwipeItem;

/**
 * Created by OldMan on 10.05.2016.
 */
public interface MainPresenter {
    void onResume();

    void onSwipeItemClicked(SwipeItem item);

    void onGroupItemRemoved(int groupPosition);

    void onChildItemRemoved(int groupPosition, int childPosition);

    void onGroupItemPinned(int groupPosition);

    void onChildItemPinned(int groupPosition, int childPosition);

    void onItemViewClicked(View v, boolean pinned);

    void navigateToCreateMission(View v);

    void navigateToChangeMission();

    void onUnderSwipeAddFlightButtonClicked(int groupPosition, View v);

    void onUnderSwipeEditMissionButtonClicked(View v);

    void onDestroy();
}
