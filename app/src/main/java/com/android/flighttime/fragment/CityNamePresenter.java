package com.android.flighttime.fragment;

/**
 * Created by oldman on 19.05.16.
 */
public interface CityNamePresenter {

    void onResume();

    void onStop();

    void onStart();

    void onBuildGoogleApiClient();

    void onPlaceResult(String placeId);

    void onDestroy();
}
