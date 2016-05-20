package com.android.flighttime.mission;

import android.view.View;

import com.android.flighttime.data.AbstractExpandableDataProvider;

/**
 * Created by oldman on 19.05.16.
 */
public interface MissionCreatorPresenter {
    void onResume();

    void onPause();


    void onBuildGoogleApiClient(String year);

    void onDestroy();
}
