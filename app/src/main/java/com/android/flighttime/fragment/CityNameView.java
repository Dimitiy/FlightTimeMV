package com.android.flighttime.fragment;

import android.text.TextWatcher;

/**
 * Created by oldman on 19.05.16.
 */
public interface CityNameView {
    void showProgress();

    void hideProgress();

    void showRecycleView();

    void setGoogleApiClient(boolean isGoogleApiClient);

}
