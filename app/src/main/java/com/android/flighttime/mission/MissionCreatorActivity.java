package com.android.flighttime.mission;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.android.flighttime.R;
import com.android.flighttime.fragment.CityNameFragment;
import com.android.flighttime.fragment.DateFragment;
import com.android.flighttime.listener.CityChangeListener;
import com.android.flighttime.listener.DatePickerListener;
import com.android.flighttime.listener.TimePickerListener;
import com.android.flighttime.main.MainActivity;
import com.android.flighttime.utils.Constants;

import java.util.ArrayList;
import java.util.Calendar;

import me.drozdzynski.library.steppers.OnCancelAction;
import me.drozdzynski.library.steppers.OnFinishAction;
import me.drozdzynski.library.steppers.SteppersItem;
import me.drozdzynski.library.steppers.SteppersView;

public class MissionCreatorActivity extends AppCompatActivity implements MissionCreatorView {
    private String nameCity = "";
    private Calendar calendarDate, calendarTime;
    private ProgressBar progressBar;
    private MissionCreatorPresenterImpl presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_mission);
        Intent intent = getIntent();
        nameCity = intent.getStringExtra("city");
        final int typeOfActivity = intent.getIntExtra("type_of_activity", 0);
        final int missionID = intent.getIntExtra("type_of_activity", 0);
        calendarDate = Calendar.getInstance();
        calendarTime = Calendar.getInstance();
        progressBar = (ProgressBar) findViewById(R.id.progress);

        presenter = new MissionCreatorPresenterImpl(this, getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SteppersView.Config steppersViewConfig = new SteppersView.Config();

        steppersViewConfig.setOnFinishAction(new OnFinishAction() {
            @Override
            public void onFinish() {
                if (typeOfActivity == Constants.TYPE_OF_MISSION_ACTIVITY_CREATED)
                    presenter.createMission(nameCity, calendarDate, calendarTime);
                else {
                    presenter.createFlight(missionID, calendarDate, calendarTime);
                }
            }
        });

        steppersViewConfig.setOnCancelAction(new OnCancelAction() {
            @Override
            public void onCancel() {
                MissionCreatorActivity.this.startActivity(new Intent(MissionCreatorActivity.this, MainActivity.class));
                MissionCreatorActivity.this.finish();
            }
        });

        steppersViewConfig.setFragmentManager(getSupportFragmentManager());


        SteppersView steppersView = (SteppersView) findViewById(R.id.steppersView);
        steppersView.setConfig(steppersViewConfig);
        steppersView.setItems(getItems(typeOfActivity));
        steppersView.build();


    }

    private ArrayList<SteppersItem> getItems(int i) {
        ArrayList<SteppersItem> steps = new ArrayList<>();
        while (i <= 2) {
            final SteppersItem item = new SteppersItem();
            switch (i) {
                case 0:
                    CityNameFragment cityNameFragment = CityNameFragment.newInstance(nameCity);
                    item.setLabel(getResources().getString(R.string.enter_travel_city));
                    item.setSubLabel(getResources().getString(R.string.home_airfield));
                    item.setPositiveButtonEnable(false);
                    cityNameFragment.addCityNameChangedListener(new CityChangeListener() {
                        @Override
                        public void onNameCityChange(String city) {
                            if (city.length() >= 3) {
                                item.setPositiveButtonEnable(true);
                                nameCity = city;
                            } else
                                item.setPositiveButtonEnable(false);
                        }
                    });
                    item.setFragment(cityNameFragment);
                    break;
                case 1:
                    DateFragment dateFragment = DateFragment.newInstance(Constants.DATE_FORMAT);
                    dateFragment.addDatePickerListener(new DatePickerListener() {
                        @Override
                        public void onSelectDate(Calendar calendarMission) {
                            calendarDate.set(calendarMission.get(Calendar.YEAR),
                                    calendarMission.get(Calendar.MONTH),
                                    calendarMission.get(Calendar.DAY_OF_MONTH));
                        }
                    });
                    item.setLabel(getResources().getString(R.string.choose_date));
                    item.setFragment(dateFragment);
                    break;

                case 2:
                    DateFragment timeFragment = DateFragment.newInstance(Constants.TIME_FORMAT);
                    timeFragment.addTimePickerListener(new TimePickerListener() {
                        @Override
                        public void onSelectTimeCount(Calendar calendarMission) {
                            calendarTime.set(Constants.BEGIN_COUNT_TIME_FLIGHT, Constants.BEGIN_COUNT_TIME_FLIGHT,
                                    Constants.BEGIN_COUNT_TIME_FLIGHT, calendarMission.get(Calendar.HOUR),
                                    calendarMission.get(Calendar.MINUTE));
//                            calendarTime.set(Calendar.HOUR, );
                        }
                    });
                    item.setLabel(getResources().getString(R.string.choose_count_time));
                    item.setSubLabel(getResources().getString(R.string.have_flown));
                    item.setFragment(timeFragment);
                    break;
            }
            steps.add(item);
            i++;
        }
        return steps;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
//        FragmentManager fm = getSupportFragmentManager();
//        OnBackPressedListener backPressedListener = null;
//        for (Fragment fragment: fm.getFragments()) {
//            if (fragment instanceof  OnBackPressedListener) {
//                backPressedListener = (OnBackPressedListener) fragment;
//                break;
//            }
//        }
//        if (backPressedListener != null) {
//            backPressedListener.onBackPressed();
//        } else {
        navigateToMainView();
        super.onBackPressed();

//        }
    }

    @Override
    public void showProgress() {
        Log.d("Mission", "progress");
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void navigateToMainView() {
        MissionCreatorActivity.this.startActivity(new Intent(MissionCreatorActivity.this, MainActivity.class));
        MissionCreatorActivity.this.finish();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

}
