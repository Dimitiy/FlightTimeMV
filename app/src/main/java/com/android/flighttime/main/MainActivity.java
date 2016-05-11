package com.android.flighttime.main;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TimePicker;

import com.android.flighttime.R;
import com.android.flighttime.database.MissionDB;
import com.android.flighttime.dialog.DatePickerDialog;
import com.android.flighttime.dialog.MissionCreator;
import com.android.flighttime.dialog.TimePickerDialog;
import com.android.flighttime.listener.DatePickerListener;
import com.android.flighttime.listener.TimePickerListener;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.roughike.swipeselector.OnSwipeItemSelectedListener;
import com.roughike.swipeselector.SwipeItem;
import com.roughike.swipeselector.SwipeSelector;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainView, View.OnClickListener, OnSwipeItemSelectedListener, RecyclerViewExpandableItemManager.OnGroupCollapseListener,
        RecyclerViewExpandableItemManager.OnGroupExpandListener {
    private ProgressBar progressBar;
    private RecyclerView missionRecyclerView;
    private SwipeSelector swipeYearSelector;
    private FloatingActionButton fab;
    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        missionRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swipeYearSelector = (SwipeSelector) findViewById(R.id.swipeYear);
        swipeYearSelector.setOnItemSelectedListener(this);
        presenter = new MainPresenterImpl(this, getApplicationContext());

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
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void setYears(SwipeItem[] swipeItems) {

    }

    @Override
    public void setItems(List<MissionDB> missionDBlList) {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void showAlertDialog(String message) {

    }

    @Override
    public void showSnackBar(String message) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                presenter.navigateToCreateMission(v);
            default:
                break;
        }
    }

    @Override
    public void onItemSelected(SwipeItem item) {
//        runOnUiThread(new Runnable() {
//            public void run() {
//                dataProvider = new ExpandableDataProvider(getMissionListForTheYear());
//                adapter.refresh(dataProvider);
//            }
//        });

    }

    @Override
    public void onGroupCollapse(int groupPosition, boolean fromUser) {

    }

    @Override
    public void onGroupExpand(int groupPosition, boolean fromUser) {

    }
}
