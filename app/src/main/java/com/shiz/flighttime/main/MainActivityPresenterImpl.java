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

package com.shiz.flighttime.main;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.shiz.flighttime.R;
import com.shiz.flighttime.database.MissionDB;
import com.shiz.flighttime.data.YearEntity;
import com.shiz.flighttime.database.DBHelper;
import com.shiz.flighttime.mission.MissionCreatorActivity;
import com.shiz.flighttime.utils.Constants;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmChangeListener;
import za.co.riggaroo.materialhelptutorial.TutorialItem;


public class MainActivityPresenterImpl implements MainActivityPresenter, MainInteractor.OnMissionFinishedListener, MainInteractor.OnYearsFinishedListener {

    private MainActivityView mainView;
    private MainInteractor findItemsInteractor;
    private DBHelper dbHelper;
    private Context context;
    private String TAG = MainActivityPresenterImpl.class.getSimpleName();

    public MainActivityPresenterImpl(MainActivityView mainView, Context context) {
        this.mainView = mainView;
        this.context = context;
        dbHelper = new DBHelper(context);
        findItemsInteractor = new MainInteractorImpl(context);
    }

    @Override
    public void onResume() {
        findItemsInteractor.findYearsItems(dbHelper, this);
    }

    @Override
    public void onFindYearsItems() {
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
    public void navigateToCreateFlight(MissionDB mission) {
        Intent intent = new Intent(context, MissionCreatorActivity.class);
        intent.putExtra("type_of_activity", Constants.TYPE_OF_FLIGHT_ACTIVITY_CREATED);
        intent.putExtra("mission_id", mission.getId());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void navigateToChangeMission(MissionDB mission, int missionID) {
        Intent intent = new Intent(context, MissionCreatorActivity.class);
        intent.putExtra("type_of_activity", Constants.TYPE_OF_MISSION_ACTIVITY_CHANGED);
        intent.putExtra("mission", Parcels.wrap(mission));
        intent.putExtra("mission_id", mission.getId());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void navigateToChangeFlight(MissionDB mission, int groupPosition, int childPosition) {
        Intent intent = new Intent(context, MissionCreatorActivity.class);
        intent.putExtra("type_of_activity", Constants.TYPE_OF_FLIGHT_ACTIVITY_CHANGED);
        intent.putExtra("mission", Parcels.wrap(mission));
        intent.putExtra("mission_id", mission.getId());
        intent.putExtra("flight_id", childPosition);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void onDeleteMission(final int groupPosition) {
        if (mainView != null)
            mainView.showProgress();

        if (dbHelper != null) {
            dbHelper.addListener(new RealmChangeListener() {
                @Override
                public void onChange(Object element) {
                    Log.d(TAG, "deleted mission" + groupPosition);
                    dbHelper.deleteListener(this);
                    mainView.notifyOnGroupItemRemoved();
                    mainView.hideProgress();
                }
            });
            dbHelper.deleteMission(groupPosition);

        }
    }

    @Override
    public void onDeleteFlight(final int groupPosition, final int childPosition) {
        if (mainView != null)
            mainView.showProgress();

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
    public void onGetTutorialItems() {
        TutorialItem tutorialItem1 = new TutorialItem(R.string.work_with_mission, R.string.tutorial_work_with_mission,
                R.color.menu_help, R.mipmap.swipe_tutorial,  R.mipmap.ic_swipe);
//
//        TutorialItem tutorialItem2 = new TutorialItem(R.string.slide_2_volunteer_professionals, R.string.slide_2_volunteer_professionals_subtitle,
//                R.color.slide_2,  R.drawable.tut_page_2_front,  R.drawable.tut_page_2_background);
//
        TutorialItem tutorialItem3 = new TutorialItem(context.getString(R.string.action_settings), null,
                R.color.menu_invite_friends, R.mipmap.ic_edit);
//
//        TutorialItem tutorialItem4 = new TutorialItem(R.string.slide_4_different_languages, R.string.slide_4_different_languages_subtitle,
//                R.color.slide_4,  R.drawable.tut_page_4_foreground, R.drawable.tut_page_4_background);
//
        ArrayList<TutorialItem> tutorialItems = new ArrayList<>();
        tutorialItems.add(tutorialItem1);
//        tutorialItems.add(tutorialItem2);
        tutorialItems.add(tutorialItem3);
//        tutorialItems.add(tutorialItem4);

        mainView.loadTutorial(tutorialItems);
    }

    @Override
    public void onYearsFinished(ArrayList<YearEntity> yearsList) {
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
