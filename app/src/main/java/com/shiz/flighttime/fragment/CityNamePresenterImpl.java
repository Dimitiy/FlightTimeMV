package com.shiz.flighttime.fragment;

import android.support.v4.app.FragmentActivity;

/**
 * Created by oldman on 19.05.16.
 */
public class CityNamePresenterImpl implements CityNamePresenter {
    private final CityNameView cityView;
    //    private final Context context;

    public CityNamePresenterImpl(String oldCity, CityNameFragment cityView, FragmentActivity fragmentManager) {
        this.cityView = cityView;
    }


    @Override
    public void onResume() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onDestroy() {

    }
}
