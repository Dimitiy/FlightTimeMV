package com.android.flighttime.mission;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.flighttime.R;
import com.android.flighttime.fragment.CityNameFragment;
import com.android.flighttime.fragment.DateFragment;
import com.android.flighttime.listener.OnBackPressedListener;
import com.android.flighttime.main.MainActivity;

import java.util.ArrayList;

import me.drozdzynski.library.steppers.OnCancelAction;
import me.drozdzynski.library.steppers.OnFinishAction;
import me.drozdzynski.library.steppers.SteppersItem;
import me.drozdzynski.library.steppers.SteppersView;

public class MissionCreatorActivity extends AppCompatActivity implements MissionCreatorView{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_mission);
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
                MissionCreatorActivity.this.startActivity(new Intent(MissionCreatorActivity.this, MainActivity.class));
                MissionCreatorActivity.this.finish();
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
        steppersView.setItems(getItems());
        steppersView.build();
    }

    private ArrayList<SteppersItem> getItems(){
        ArrayList<SteppersItem> steps = new ArrayList<>();

        int i = 1;
        while (i <= 4) {

            final SteppersItem item = new SteppersItem();
            item.setLabel(getResources().getString(R.string.step) + i);
            item.setPositiveButtonEnable(i % 2 != 0);

            if(i % 2 == 0) {
                CityNameFragment cityNameFragment = new CityNameFragment();
                item.setPositiveButtonEnable(true);
                item.setSubLabel(getResources().getString(R.string.enter_travel_city));
                item.setFragment(cityNameFragment);
            } else {
                DateFragment blankSecondFragment = new DateFragment();
                item.setSubLabel("Fragment: " + blankSecondFragment.getClass().getSimpleName());
                item.setFragment(blankSecondFragment);
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
            super.onBackPressed();

//        }
    }
    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }
}
