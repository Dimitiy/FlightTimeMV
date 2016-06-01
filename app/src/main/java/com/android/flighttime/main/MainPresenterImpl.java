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
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import com.android.flighttime.data.AbstractExpandableDataProvider;
import com.android.flighttime.database.DBHelper;
import com.android.flighttime.database.FlightDB;
import com.android.flighttime.database.MissionDB;
import com.android.flighttime.listener.OnTaskCreateListener;
import com.android.flighttime.mission.MissionCreatorActivity;
import com.android.flighttime.utils.Constants;
import com.roughike.swipeselector.SwipeItem;

import org.parceler.Parcels;

import java.util.Calendar;
import java.util.List;

import io.realm.RealmChangeListener;


public class MainPresenterImpl implements MainPresenter,  FindItemsInteractor.OnMissionFinishedListener, FindItemsInteractor.OnYearsFinishedListener {

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
    public void navigateToCreateMission() {
        Intent intent = new Intent(context, MissionCreatorActivity.class);
        intent.putExtra("type_of_activity", Constants.TYPE_OF_MISSION_ACTIVITY_CREATED);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void navigateToCreateFlight(AbstractExpandableDataProvider.MissionData mission) {
        Intent intent = new Intent(context, MissionCreatorActivity.class);
        intent.putExtra("type_of_activity", Constants.TYPE_OF_FLIGHT_ACTIVITY_CREATED);
        intent.putExtra("mission_id", mission.getMission().getId());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void navigateToChangeMission(MissionDB missionDB, int missionID) {
        Intent intent = new Intent(context, MissionCreatorActivity.class);
        intent.putExtra("type_of_activity", Constants.TYPE_OF_MISSION_ACTIVITY_CREATED);
        intent.putExtra("mission", Parcels.wrap(missionDB));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void onDeleteMission(final int groupPosition) {
        if (dbHelper != null) {
            dbHelper.addListener(new RealmChangeListener() {
                @Override
                public void onChange(Object element) {
                    Log.d(TAG, "deleted mission" + groupPosition);
                    dbHelper.deleteListener(this);
//                    mainView.notifyGroupItemRestored(groupPosition);
                }
            });
            dbHelper.deleteMission(groupPosition);

        }
    }

    @Override
    public void onDeleteFlight(final int groupPosition, final int childPosition) {
        if (mainView != null) {
            mainView.showProgress();
        }
        if (dbHelper != null) {
            dbHelper.addListener(new RealmChangeListener() {
                @Override
                public void onChange(Object element) {
                    Log.d(TAG, "delete flight in " + groupPosition + " " + childPosition);
                    dbHelper.deleteListener(this);
                    mainView.hideProgress();

                }
            });
            dbHelper.deleteFlightInMission(groupPosition, childPosition);

        }
    }

    @Override
    public void onDestroy() {
        mainView = null;
        dbHelper.closeRealm();

    }

    @Override
    public void onYearsFinished(SwipeItem[] yearsList) {
        if (mainView != null) {
            mainView.setYears(yearsList);
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
}
