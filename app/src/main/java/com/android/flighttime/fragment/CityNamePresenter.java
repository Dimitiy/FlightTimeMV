package com.android.flighttime.fragment;

/**
 * Created by oldman on 19.05.16.
 */
public interface CityNamePresenter {

    void onResume();

    void onPause();


    void onBuildGoogleApiClient(String year);

    void onDestroy();
}
