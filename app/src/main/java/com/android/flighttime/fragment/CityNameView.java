package com.android.flighttime.fragment;

import android.text.TextWatcher;

import com.google.android.gms.common.api.Status;

/**
 * Created by oldman on 19.05.16.
 */
public interface CityNameView {
    void showProgress();

    void hideProgress();

    void onPlaceSelected(String place);

    void onErrorPlaceSelection(Status status);

}
