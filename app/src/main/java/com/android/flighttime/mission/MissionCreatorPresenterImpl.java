package com.android.flighttime.mission;

import android.content.Context;
import android.util.Log;

import com.android.flighttime.database.DBHelper;

import java.util.Calendar;

import io.realm.RealmChangeListener;

/**
 * Created by oldman on 19.05.16.
 */
public class MissionCreatorPresenterImpl implements MissionCreatorPresenter {
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
        dbHelper.closeRealm();
    }

    @Override
    public void createMission(final String name, Calendar date, final long duration) {
        missionView.showProgress();
        dbHelper.addListener(new RealmChangeListener() {
            @Override
            public void onChange(Object element) {
                Log.d("MissionCreator", "createMission " + name + " " + duration);
                onNavigateToMainView();
                dbHelper.deleteListener(this);

            }
        });
        dbHelper.insertMission(name, date, duration);

    }

    @Override
    public void createFlight(int missionId, Calendar date, long  duration) {
        missionView.showProgress();
        dbHelper.addListener(new RealmChangeListener() {
            @Override
            public void onChange(Object element) {
                onNavigateToMainView();
                dbHelper.deleteListener(this);
            }
        });
        dbHelper.insertFlightInMission(missionId, date, duration);
    }

    @Override
    public void updateMission(int id, String nameCity, Calendar calendarDate, long duration) {
        missionView.showProgress();
        Log.d("MissionCreator", "updateMission " + id + " " + duration);

        dbHelper.addListener(new RealmChangeListener() {
            @Override
            public void onChange(Object element) {
                onNavigateToMainView();
                dbHelper.deleteListener(this);
            }
        });
        dbHelper.updateMission(id, nameCity, calendarDate.getTime(), duration);
    }

    private void onNavigateToMainView() {
        missionView.hideProgress();
        missionView.navigateToMainView();
    }
}
