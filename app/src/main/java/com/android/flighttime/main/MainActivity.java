package com.android.flighttime.main;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.android.flighttime.R;
import com.android.flighttime.adapters.ExpandSwipeViewAdapter;
import com.android.flighttime.data.AbstractExpandableDataProvider;
import com.android.flighttime.data.ExpandableDataProvider;
import com.android.flighttime.database.FlightDB;
import com.android.flighttime.database.MissionDB;
import com.android.flighttime.dialog.DelItemDialog;
import com.android.flighttime.listener.DialogClickListener;
import com.android.flighttime.mission.MissionCreatorActivity;
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.SwipeDismissItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.touchguard.RecyclerViewTouchActionGuardManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;
import com.roughike.swipeselector.OnSwipeItemSelectedListener;
import com.roughike.swipeselector.SwipeItem;
import com.roughike.swipeselector.SwipeSelector;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainView, View.OnClickListener, OnSwipeItemSelectedListener, RecyclerViewExpandableItemManager.OnGroupCollapseListener,
        RecyclerViewExpandableItemManager.OnGroupExpandListener, DialogClickListener {
    private CoordinatorLayout coordinatorLayout;
    private ProgressBar progressBar;
    private SwipeSelector swipeYearSelector;
    private FloatingActionButton fab;
    private MainPresenter presenter;
    private RecyclerView missionRecyclerView;
    private RecyclerViewExpandableItemManager recyclerViewExpandableItemManager;
    private RecyclerViewDragDropManager recyclerViewDragDropManager;
    private RecyclerViewSwipeManager recyclerViewSwipeManager;
    private RecyclerViewTouchActionGuardManager recyclerViewTouchActionGuardManager;
    private static final String SAVED_STATE_EXPANDABLE_ITEM_MANAGER = "RecyclerViewExpandableItemManager";
    private RecyclerView.LayoutManager layoutManager;
    private Bundle savedInstanceState;
    private Context context;
    //    private ExpandSwipeViewAdapter adapter;
    private RecyclerView.Adapter mWrappedAdapter;
    private ExpandableDataProvider dataProvider;
    private ExpandSwipeViewAdapter adapter;

    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);
        context = getApplicationContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressBar = (ProgressBar) findViewById(R.id.progress);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        swipeYearSelector = (SwipeSelector) findViewById(R.id.swipeYear);
        swipeYearSelector.setOnItemSelectedListener(this);

         presenter = new MainPresenterImpl(this, getApplicationContext());

    }

    private void initRecycleView(ExpandSwipeViewAdapter adapter) {
        missionRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(context);

        final Parcelable eimSavedState = (savedInstanceState != null) ? savedInstanceState.getParcelable(SAVED_STATE_EXPANDABLE_ITEM_MANAGER) : null;
        recyclerViewExpandableItemManager = new RecyclerViewExpandableItemManager(eimSavedState);
        recyclerViewExpandableItemManager.setOnGroupExpandListener(this);
        recyclerViewExpandableItemManager.setOnGroupCollapseListener(this);

        // touch guard manager  (this class is required to suppress scrolling while swipe-dismiss animation is running)
        recyclerViewTouchActionGuardManager = new RecyclerViewTouchActionGuardManager();
        recyclerViewTouchActionGuardManager.setInterceptVerticalScrollingWhileAnimationRunning(true);
        recyclerViewTouchActionGuardManager.setEnabled(true);

        // drag & drop manager
        recyclerViewDragDropManager = new RecyclerViewDragDropManager();
        recyclerViewDragDropManager.setDraggingItemShadowDrawable(
                (NinePatchDrawable) ContextCompat.getDrawable(context, R.drawable.material_shadow_z3));
        // swipe manager
        recyclerViewSwipeManager = new RecyclerViewSwipeManager();

        mWrappedAdapter = recyclerViewExpandableItemManager.createWrappedAdapter(adapter);         // wrap for expanding
        mWrappedAdapter = recyclerViewDragDropManager.createWrappedAdapter(mWrappedAdapter);     // wrap for dragging
        mWrappedAdapter = recyclerViewSwipeManager.createWrappedAdapter(mWrappedAdapter);      // wrap for swiping

        final GeneralItemAnimator animator = new SwipeDismissItemAnimator();
        animator.setSupportsChangeAnimations(true);

        missionRecyclerView.setLayoutManager(layoutManager);
        missionRecyclerView.setAdapter(mWrappedAdapter);  // requires *wrapped* adapter
        missionRecyclerView.setItemAnimator(animator);
        missionRecyclerView.setHasFixedSize(false);
        // additional decorations
        //noinspection StatementWithEmptyBody
        if (supportsViewElevation()) {
            // Lollipop or later has native drop shadow feature. ItemShadowDecorator is not required.
        } else {
            missionRecyclerView.addItemDecoration(new ItemShadowDecorator((NinePatchDrawable) ContextCompat.getDrawable(context, R.drawable.material_shadow_z1)));
        }
        missionRecyclerView.addItemDecoration(new SimpleListDividerDecorator(ContextCompat.getDrawable(context, R.drawable.list_divider_h), true));

        recyclerViewTouchActionGuardManager.attachRecyclerView(missionRecyclerView);
        recyclerViewSwipeManager.attachRecyclerView(missionRecyclerView);
        recyclerViewDragDropManager.attachRecyclerView(missionRecyclerView);
        recyclerViewExpandableItemManager.attachRecyclerView(missionRecyclerView);
    }

    private boolean supportsViewElevation() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        if (recyclerViewDragDropManager != null) {
            recyclerViewDragDropManager.release();
            recyclerViewDragDropManager = null;
        }

        if (recyclerViewSwipeManager != null) {
            recyclerViewSwipeManager.release();
            recyclerViewSwipeManager = null;
        }

        if (recyclerViewTouchActionGuardManager != null) {
            recyclerViewTouchActionGuardManager.release();
            recyclerViewTouchActionGuardManager = null;
        }

        if (recyclerViewExpandableItemManager != null) {
            recyclerViewExpandableItemManager.release();
            recyclerViewExpandableItemManager = null;
        }

        if (missionRecyclerView != null) {
            missionRecyclerView.setItemAnimator(null);
            missionRecyclerView.setAdapter(null);
            missionRecyclerView = null;
        }

        if (mWrappedAdapter != null) {
            WrapperAdapterUtils.releaseAll(mWrappedAdapter);
            mWrappedAdapter = null;
        }
        adapter = null;
        layoutManager = null;
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save current state to support screen rotation, etc...
        if (recyclerViewExpandableItemManager != null) {
            outState.putParcelable(
                    SAVED_STATE_EXPANDABLE_ITEM_MANAGER,
                    recyclerViewExpandableItemManager.getSavedState());
        }
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
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void setYears(final SwipeItem[] swipeItems) {
        Log.d(TAG, swipeItems.toString());
        runOnUiThread(new Runnable() {
            public void run() {
                swipeYearSelector.setItems(swipeItems);
                presenter.onMissionItems(swipeYearSelector.getSelectedItem().title);

            }
        });
    }

    @Override
    public void setMissionItems(final List<MissionDB> missionDBlList) {
//        runOnUiThread(new Runnable() {
//            public void run() {
        dataProvider = new ExpandableDataProvider(missionDBlList);
        if (adapter == null) {
            adapter = new ExpandSwipeViewAdapter(recyclerViewExpandableItemManager, dataProvider);
            initRecycleView(adapter);
            adapter.setEventListener(MainActivity.this);

        } else
            adapter.refresh(dataProvider);

    }
//        });
//    }

    @Override
    public void showMessageSnackbar(int message, int action, final int groupPosition, final int childPosition) {
        final int missionId = dataProvider.getMissionItem(groupPosition).getMission().getId();
        final int flightId = dataProvider.getFlightItem(groupPosition, childPosition).getFlight().getId();

        Snackbar snackbar = Snackbar.make(
                coordinatorLayout,
                message,
                Snackbar.LENGTH_LONG).setCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if (event != Snackbar.Callback.DISMISS_EVENT_ACTION)
                    if (childPosition != -1)
                        presenter.onDeleteFlight(missionId, flightId);
                    else if (groupPosition != -1)
                        presenter.onDeleteMission(missionId);
            }

            @Override
            public void onShown(Snackbar snackbar) {
                if (childPosition != -1) {
                    dataProvider.removeFlightItem(groupPosition, childPosition);
                    adapter.notifyDataSetChanged();
                } else if (groupPosition != -1) {
                    dataProvider.removeMissionItem(groupPosition);
                    adapter.notifyDataSetChanged();
                }

            }
        });
        snackbar.setAction(action, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemUndoActionClicked();
            }
        });
        snackbar.setActionTextColor(ContextCompat.getColor(context, R.color.snackbar_action_color_done)).getView().setBackgroundColor(ContextCompat.getColor(context, R.color.colorlightGreen));
        snackbar.show();
    }

    private void onItemUndoActionClicked() {
        final long result = dataProvider.undoLastRemoval();
        if (result == RecyclerViewExpandableItemManager.NO_EXPANDABLE_POSITION) {
            return;
        }
        final int groupPosition = RecyclerViewExpandableItemManager.getPackedPositionGroup(result);
        final int childPosition = RecyclerViewExpandableItemManager.getPackedPositionChild(result);

        if (childPosition == RecyclerView.NO_POSITION) {
            // group item
            notifyGroupItemRestored(groupPosition);
        } else {
            // child item
            notifyChildItemRestored(groupPosition, childPosition);
        }
    }

    @Override
    public void showAlertDialog(String message) {

    }

    @Override
    public void showSnackBar(String message) {

    }


    @Override
    public void onGroupItemRemoved(int groupPosition) {
        final DialogFragment dialog = DelItemDialog.newInstance(groupPosition, RecyclerView.NO_POSITION);
        dialog.show(getSupportFragmentManager(), "delete_dialog");
    }

    @Override
    public void onClickToDeleteMission(int groupPosition) {
        showMessageSnackbar(R.string.snack_bar_text_group_item_removed, R.string.snack_bar_action_undo, groupPosition, -1);
    }

    @Override
    public void editMissionSuccess() {

    }

    @Override
    public void onNoClick(int groupPosition, int childPosition) {

    }

    @Override
    public void onChildItemRemoved(int groupPosition, int childPosition) {
        showMessageSnackbar(R.string.snack_bar_text_child_item_removed, R.string.snack_bar_action_undo, groupPosition, childPosition);
    }

    @Override
    public void onGroupItemPinned(int groupPosition) {

    }

    @Override
    public void onChildItemPinned(int groupPosition, int childPosition) {

    }

    @Override
    public void onItemViewClicked(View v, boolean pinned) {
        final int flatPosition = missionRecyclerView.getChildAdapterPosition(v);

        if (flatPosition == RecyclerView.NO_POSITION) {
            return;
        }
        final long expandablePosition = recyclerViewExpandableItemManager.getExpandablePosition(flatPosition);
        final int groupPosition = RecyclerViewExpandableItemManager.getPackedPositionGroup(expandablePosition);
        final int childPosition = RecyclerViewExpandableItemManager.getPackedPositionChild(expandablePosition);

        if (childPosition == RecyclerView.NO_POSITION) {
            onGroupItemClicked(groupPosition);
        } else {
            onChildItemClicked(groupPosition, childPosition);
        }
    }

    @Override
    public void onUnderSwipeAddFlightButtonClicked(int groupPosition, View v) {
        presenter.navigateToCreateFlight(dataProvider.getMissionItem(groupPosition), v);
    }

    @Override
    public void onFlightItemCreated(int id, FlightDB flight) {
        dataProvider.addFlightItem(id, flight);
        int childCount = dataProvider.getFlightCount(id);
        recyclerViewExpandableItemManager.notifyChildItemInserted(id, childCount - 1);

    }

    @Override
    public void onUnderSwipeEditMissionButtonClicked(View v) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
//                presenter.navigateToCreateMission(v);
                startActivity(new Intent(this, MissionCreatorActivity.class));
            default:
                break;
        }
    }

    @Override
    public void onItemSelected(SwipeItem item) {
        presenter.onMissionItems(item.title);
    }

    @Override
    public void onGroupCollapse(int groupPosition, boolean fromUser) {

    }

    @Override
    public void onGroupExpand(int groupPosition, boolean fromUser) {
        if (fromUser) {
            adjustScrollPositionOnGroupExpanded(groupPosition);
        }
    }

    private void adjustScrollPositionOnGroupExpanded(int groupPosition) {
        int childItemHeight = getResources().getDimensionPixelSize(R.dimen.list_item_height);
        int topMargin = (int) (getResources().getDisplayMetrics().density * 16); // top-spacing: 16dp
        int bottomMargin = topMargin; // bottom-spacing: 16dp
        recyclerViewExpandableItemManager.scrollToGroup(groupPosition, childItemHeight, topMargin, bottomMargin);
    }

    public void notifyGroupItemRestored(int groupPosition) {
        adapter.notifyDataSetChanged();
        final long expandablePosition = RecyclerViewExpandableItemManager.getPackedPositionForGroup(groupPosition);
        final int flatPosition = recyclerViewExpandableItemManager.getFlatPosition(expandablePosition);
        missionRecyclerView.scrollToPosition(flatPosition);
    }

    public void notifyChildItemRestored(int groupPosition, int childPosition) {
        adapter.notifyDataSetChanged();
        final long expandablePosition = RecyclerViewExpandableItemManager.getPackedPositionForChild(groupPosition, childPosition);
        final int flatPosition = recyclerViewExpandableItemManager.getFlatPosition(expandablePosition);
        missionRecyclerView.scrollToPosition(flatPosition);
    }

    public void notifyGroupItemChanged(int groupPosition) {
        final long expandablePosition = RecyclerViewExpandableItemManager.getPackedPositionForGroup(groupPosition);
        final int flatPosition = recyclerViewExpandableItemManager.getFlatPosition(expandablePosition);
        adapter.notifyItemChanged(flatPosition);
    }

    public void onNotifyExpandableItemPinnedDialogDismissed(int groupPosition, int childPosition, boolean ok) {
        if (childPosition == RecyclerView.NO_POSITION) {
            dataProvider.getMissionItem(groupPosition).setPinned(ok);
            notifyGroupItemChanged(groupPosition);
        } else {
            dataProvider.getFlightItem(groupPosition, childPosition).setPinned(ok);
            notifyChildItemChanged(groupPosition, childPosition);
        }
    }

    public void notifyChildItemChanged(int groupPosition, int childPosition) {
        final long expandablePosition = RecyclerViewExpandableItemManager.getPackedPositionForChild(groupPosition, childPosition);
        final int flatPosition = recyclerViewExpandableItemManager.getFlatPosition(expandablePosition);
        adapter.notifyItemChanged(flatPosition);
    }

    public void onGroupItemClicked(int groupPosition) {
        AbstractExpandableDataProvider.MissionData data = dataProvider.getMissionItem(groupPosition);
        if (data.isPinned()) {
            // unpin if tapped the pinned item
            data.setPinned(false);
            notifyGroupItemChanged(groupPosition);
        }
    }

    public void onChildItemClicked(int groupPosition, int childPosition) {
        AbstractExpandableDataProvider.FlightData data = dataProvider.getFlightItem(groupPosition, childPosition);
        if (data.isPinned()) {
            // unpin if tapped the pinned item
            data.setPinned(false);
            notifyChildItemChanged(groupPosition, childPosition);
        }
    }
}
