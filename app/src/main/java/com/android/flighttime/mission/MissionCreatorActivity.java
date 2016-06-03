package com.android.flighttime.mission;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.android.flighttime.R;
import com.android.flighttime.database.MissionDB;
import com.android.flighttime.fragment.CityNameFragment;
import com.android.flighttime.fragment.DateFragment;
import com.android.flighttime.listener.CityChangeListener;
import com.android.flighttime.listener.DatePickerListener;
import com.android.flighttime.listener.TimePickerListener;
import com.android.flighttime.main.MainActivity;
import com.android.flighttime.utils.Constants;
import com.android.flighttime.utils.Formatter;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import me.drozdzynski.library.steppers.OnCancelAction;
import me.drozdzynski.library.steppers.OnFinishAction;
import me.drozdzynski.library.steppers.SteppersItem;
import me.drozdzynski.library.steppers.SteppersView;

public class MissionCreatorActivity extends AppCompatActivity implements MissionCreatorView {
    private String nameCity = "";
    private Calendar calendarDate;
    private Date oldDate;
    private long duration = 0;
    private final int ZERO_STEPS = 0;
    private final int FIRST_STEPS = 1;
    private final int SECOND_STEPS = 2;
    private ProgressBar progressBar;
    private MissionCreatorPresenterImpl presenter;
    private boolean isUpdateMission = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_mission);
        Intent intent = getIntent();
        nameCity = intent.getStringExtra("city");
        final int typeOfActivity = intent.getIntExtra("type_of_activity", 0);
        final int missionID = intent.getIntExtra("mission_id", 0);
        final MissionDB mission = Parcels.unwrap(getIntent().getParcelableExtra("mission"));
        if (mission != null) {
            isUpdateMission = true;
            nameCity = mission.getCity();
            oldDate = mission.getDate();
        }
        calendarDate = Calendar.getInstance();
        progressBar = (ProgressBar) findViewById(R.id.progress);
        presenter = new MissionCreatorPresenterImpl(this, getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SteppersView.Config steppersViewConfig = new SteppersView.Config();

        steppersViewConfig.setOnFinishAction(new OnFinishAction() {
            @Override
            public void onFinish() {
                if (typeOfActivity == Constants.TYPE_OF_MISSION_ACTIVITY_CREATED)
                    if (!isUpdateMission)
                        presenter.createMission(nameCity, calendarDate);
                    else
                        presenter.updateMission(mission.getId(), nameCity, calendarDate);

                else {
                    presenter.createFlight(missionID, calendarDate, duration);
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

        ArrayList<SteppersItem> steps = new ArrayList<>();
        if (typeOfActivity == Constants.TYPE_OF_MISSION_ACTIVITY_CREATED) {
            steps.add(ZERO_STEPS, getCityFragment());
            steps.add(FIRST_STEPS, getDateFragment());
        } else {
            steps.add(ZERO_STEPS, getDateFragment());
            steps.add(FIRST_STEPS, getDurationFragment());
        }
        SteppersView steppersView = (SteppersView) findViewById(R.id.steppersView);
        steppersView.setConfig(steppersViewConfig);
        steppersView.setItems(steps);
        steppersView.build();


    }


    private SteppersItem getCityFragment() {
        final SteppersItem item = new SteppersItem();
        CityNameFragment cityNameFragment;
        if (isUpdateMission)
            cityNameFragment = CityNameFragment.newInstance(nameCity);
        else
            cityNameFragment = CityNameFragment.newInstance();

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
        return item;
    }

    private SteppersItem getDateFragment() {
        final SteppersItem item = new SteppersItem();

        DateFragment dateFragment;
        if (!isUpdateMission)
            dateFragment = DateFragment.newInstance(Constants.DATE_FORMAT);
        else
            dateFragment = DateFragment.newInstance(Constants.DATE_FORMAT, Formatter.getDateFormat(oldDate));
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
        return item;
    }

    private SteppersItem getDurationFragment() {
        final SteppersItem item = new SteppersItem();

        DateFragment timeFragment;
        timeFragment = DateFragment.newInstance(Constants.TIME_FORMAT);
        timeFragment.addTimePickerListener(new TimePickerListener() {
            @Override
            public void onSelectTimeCount(long calendarTime) {
                MissionCreatorActivity.this.duration = calendarTime;
            }
        });
        item.setLabel(getResources().getString(R.string.choose_count_time));
        item.setSubLabel(getResources().getString(R.string.have_flown));
        item.setFragment(timeFragment);
        return item;
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
//        navigateToMainView();
        super.onBackPressed();

//        }
    }

    @Override
    public void showProgress() {
         progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
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
