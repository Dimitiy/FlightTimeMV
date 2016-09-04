package com.shiz.flighttime.mission;

import android.util.Log;

import com.shiz.flighttime.database.DBHelper;

import java.util.Calendar;

import io.realm.RealmChangeListener;

/**
 * Created by oldman on 19.05.16.
 */
public class MissionCreatorPresenterImpl implements MissionCreatorPresenter {
    private final MissionCreatorView missionView;
    private DBHelper dbHelper;

    public MissionCreatorPresenterImpl(MissionCreatorView missionView) {
        this.missionView = missionView;
        dbHelper = new DBHelper();
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
    public void createMission(final String name, Calendar date) {
        if (missionView != null) {
            missionView.showProgress();
        }
        dbHelper.addListener(new RealmChangeListener() {
            @Override
            public void onChange(Object element) {
                Log.d("MissionCreator", "createMission " + name);
                onNavigateToMainView();
                dbHelper.deleteListener(this);
            }
        });
        dbHelper.insertMission(name, date);
    }

    @Override
    public void createFlight(int missionId, Calendar date, long duration) {
        if (missionView != null) {
            missionView.showProgress();
        }
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
    public void updateMission(int id, String nameCity, Calendar calendarDate) {
        if (missionView != null)
            missionView.showProgress();
        Log.d("MissionCreator", "updateMission " + id);

        dbHelper.addListener(new RealmChangeListener() {
            @Override
            public void onChange(Object element) {
                onNavigateToMainView();
                dbHelper.deleteListener(this);
            }
        });
        dbHelper.updateMission(id, nameCity, calendarDate.getTime());
    }

    @Override
    public void updateFlight(int idMission, int idFlight, Calendar date, long duration) {
        if (missionView != null)
            missionView.showProgress();
        Log.d("MissionCreator", "updateMission " + idMission + " idFlight " + idFlight);

        dbHelper.addListener(new RealmChangeListener() {
            @Override
            public void onChange(Object element) {
                onNavigateToMainView();
                dbHelper.deleteListener(this);
            }
        });
        dbHelper.updateFlightInMission(idMission, idFlight, date.getTime(), duration);
    }

    private void onNavigateToMainView() {
        if (missionView != null) {
            missionView.hideProgress();
            missionView.navigateToMainView();
        }
    }
}
