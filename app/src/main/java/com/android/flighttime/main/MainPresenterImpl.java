/*
 *
 *  * Copyright (C) 2014 Antonio Leiva Gordillo.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.android.flighttime.main;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.android.flighttime.data.AbstractExpandableDataProvider;
import com.android.flighttime.database.DBHelper;
import com.android.flighttime.database.FlightDB;
import com.android.flighttime.database.MissionDB;
import com.android.flighttime.listener.OnTaskCreateListener;
import com.roughike.swipeselector.SwipeItem;

import java.util.Calendar;
import java.util.List;

import io.realm.RealmChangeListener;


public class MainPresenterImpl implements MainPresenter, OnTaskCreateListener, FindItemsInteractor.OnMissionFinishedListener, FindItemsInteractor.OnYearsFinishedListener, RealmChangeListener {

    private MainView mainView;
    private FindItemsInteractor findItemsInteractor;
    private DBHelper dbHelper;
    private Context context;
    private String TAG = MainPresenterImpl.class.getSimpleName();

    public MainPresenterImpl(MainView mainView, Context context) {
        this.mainView = mainView;
        this.context = context;
        dbHelper = new DBHelper(context);
        findItemsInteractor = new FindItemsInteractorImpl(context);
    }

    @Override
    public void onResume() {
        if (mainView != null) {
            mainView.showProgress();
        }
        findItemsInteractor.findYearsItems(dbHelper, this);
    }

    @Override
    public void onMissionItems(String year) {
        if (mainView != null) {
            mainView.showProgress();
        }
        findItemsInteractor.findMissionItems(dbHelper, year, this);
    }


    @Override
    public void navigateToCreateMission(View v) {

    }

    @Override
    public void navigateToCreateFlight(AbstractExpandableDataProvider.MissionData mission, View v) {
//        new FlightCreator(mission, (Activity) mainView, this);
    }

    @Override
    public void navigateToChangeMission() {

    }

    @Override
    public void onDeleteMission(final int groupPosition) {
        if (dbHelper != null)
            dbHelper.deleteMission(groupPosition);
            dbHelper.addListener(new RealmChangeListener() {
                @Override
                public void onChange(Object element) {
                    Log.d(TAG, "delete mission" + groupPosition);
                }
            });
    }

    @Override
    public void onDeleteFlight(int groupPosition, int childPosition) {
        if (dbHelper != null)
            dbHelper.deleteFlightInMission(groupPosition, childPosition);
    }

    @Override
    public void onDestroy() {
        mainView = null;
        dbHelper.closeRealm(this);

    }

    @Override
    public void onYearsFinished(SwipeItem[] yearsList) {
        if (mainView != null) {
            mainView.setYears(yearsList);
            mainView.hideProgress();
        }
    }
    @Override
    public void onMissionFinished(List<MissionDB> missionsList) {
        if (mainView != null) {
            Log.d(TAG, "on missionFinished");
            mainView.setMissionItems(missionsList);
            mainView.hideProgress();
        }
    }



    @Override
    public void onMissionCreated(String address, Calendar date, Calendar time) {
        Log.d("MissionCreated", address);
        dbHelper.insertMission(address, date, time);
        
//        findItemsInteractor.findYearsItems(dbHelper, this);

//        mainView.onit;
//        mainView.setMissionItems();
    }

    @Override
    public void onFlightCreated(AbstractExpandableDataProvider.MissionData mission, Calendar calendar) {
        long second = (calendar.get(Calendar.HOUR) * 3600) + (calendar.get(Calendar.MINUTE) * 60);

        FlightDB flight = new FlightDB();
        flight.setId(dbHelper.getPrimaryKey(flight));
        flight.setDate(calendar.getTime());
        flight.setDuration(second);

        dbHelper.insertFlightInMission(mission.getMission().getId(), flight);
        mainView.onFlightItemCreated(mission.getMissionId(), flight);
    }

    @Override
    public void onChange(Object element) {
        Log.d(TAG, "element.getClass().toString()");
    }
}
