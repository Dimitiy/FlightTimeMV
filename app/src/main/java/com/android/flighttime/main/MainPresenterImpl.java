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

import com.android.flighttime.database.DBHelper;
import com.android.flighttime.database.MissionDB;
import com.android.flighttime.dialog.MissionCreator;
import com.android.flighttime.listener.OnMissionCreateListener;
import com.roughike.swipeselector.SwipeItem;

import java.util.Calendar;
import java.util.List;

import io.realm.RealmChangeListener;


public class MainPresenterImpl implements MainPresenter, OnMissionCreateListener, FindItemsInteractor.OnMissionFinishedListener, FindItemsInteractor.OnYearsFinishedListener, RealmChangeListener {

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

    @Override public void onResume() {
        if (mainView != null) {
            mainView.showProgress();
        }
        findItemsInteractor.findYearsItems(dbHelper, this);
    }

    @Override
    public void onSwipeItemClicked(SwipeItem item) {
        if (mainView != null) {
            mainView.showMessage(String.format("Position %d clicked", item.description));
        }
    }

    @Override
    public void onGroupItemRemoved(int groupPosition) {

    }

    @Override
    public void onChildItemRemoved(int groupPosition, int childPosition) {

    }

    @Override
    public void onGroupItemPinned(int groupPosition) {

    }

    @Override
    public void onChildItemPinned(int groupPosition, int childPosition) {

    }

    @Override
    public void onItemViewClicked(View v, boolean pinned) {

    }

    @Override
    public void navigateToCreateMission(View v) {
        new MissionCreator((Activity)mainView, this);
    }

    @Override
    public void navigateToChangeMission() {

    }

    @Override
    public void onUnderSwipeAddFlightButtonClicked(int groupPosition, View v) {

    }

    @Override
    public void onUnderSwipeEditMissionButtonClicked(View v) {

    }

    @Override public void onDestroy() {
        mainView = null;
    }

    @Override
    public void onMissionFinished(List<MissionDB> missionsList) {
        if (mainView != null) {
            mainView.setItems(missionsList);
            mainView.hideProgress();
        }
    }

    @Override
    public void onYearsFinished(SwipeItem[] yearsList) {
        if (mainView != null) {
            mainView.setYears(yearsList);
            mainView.hideProgress();
        }
    }

    @Override
    public void onMissionCreated(String address, Calendar calendar) {
        Log.d("MissionCreated", address);

        dbHelper.addListener(this);
        dbHelper.insertMission(address, calendar);
    }

    @Override
    public void onChange(Object element) {
        Log.d(TAG, "element.getClass().toString()");
    }
}
