package com.android.flighttime.mission;

import android.content.Context;
import android.util.Log;

import com.android.flighttime.database.DBHelper;

import java.util.Calendar;

import io.realm.RealmChangeListener;

/**
 * Created by oldman on 19.05.16.
 */
public class MissionCreatorPresenterImpl implements MissionCreatorPresenter, RealmChangeListener {
    private final MissionCreatorView missionView;
    private final Context context;
    private DBHelper dbHelper;

    public MissionCreatorPresenterImpl(MissionCreatorView missionView, Context context) {
        this.missionView = missionView;
        this.context = context;
        dbHelper = new DBHelper(context);
    }


    @Override
    public void onResume() {
        if (missionView != null) {
            missionView.showProgress();
        }
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onDestroy() {
        dbHelper.closeRealm(this);
    }

    @Override
    public void createMission(String name, Calendar date, Calendar time) {
        missionView.showProgress();
        dbHelper.addListener(this);
        dbHelper.insertMission(name, date, time);

    }

    @Override
    public void onChange(Object element) {
       missionView.navigateToMainView();
    }
}
