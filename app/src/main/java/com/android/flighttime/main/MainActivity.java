package com.android.flighttime.main;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.NinePatchDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.android.flighttime.R;
import com.android.flighttime.adapters.ExpandSwipeViewAdapter;
import com.android.flighttime.adapters.YearsCoverFlowAdapter;
import com.android.flighttime.data.AbstractExpandableDataProvider;
import com.android.flighttime.data.ExpandableDataProvider;
import com.android.flighttime.data.YearEntity;
import com.android.flighttime.database.FlightDB;
import com.android.flighttime.database.MissionDB;
import com.android.flighttime.dialog.DeleteItemDialog;
import com.android.flighttime.listener.DeleteDialogClickListener;
import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.SwipeDismissItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.touchguard.RecyclerViewTouchActionGuardManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;


import com.melnykov.fab.FloatingActionButton;
import com.tiancaicc.springfloatingactionmenu.MenuItemView;
import com.tiancaicc.springfloatingactionmenu.OnMenuActionListener;
import com.tiancaicc.springfloatingactionmenu.SpringFloatingActionMenu;

import java.util.ArrayList;
import java.util.List;

//import icepick.Icepick;
//import icepick.State;

public class MainActivity extends AppCompatActivity implements MainActivityView, View.OnClickListener, CarouselLayoutManager.OnCenterItemSelectionListener, RecyclerViewExpandableItemManager.OnGroupCollapseListener,
        RecyclerViewExpandableItemManager.OnGroupExpandListener, DeleteDialogClickListener {
    private CoordinatorLayout coordinatorLayout;
    private FloatingActionButton fab;
    //    @State
    private ProgressBar progressBar;

    private MainActivityPresenter presenter;

    private RecyclerView missionRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerViewExpandableItemManager recyclerViewExpandableItemManager;
    private RecyclerViewDragDropManager recyclerViewDragDropManager;
    private RecyclerViewSwipeManager recyclerViewSwipeManager;
    private RecyclerViewTouchActionGuardManager recyclerViewTouchActionGuardManager;
    //    private ExpandSwipeViewAdapter adapter;
    private RecyclerView.Adapter mWrappedAdapter;
    private ExpandableDataProvider dataProvider;
    private ExpandSwipeViewAdapter adapter;
    private YearsCoverFlowAdapter yearsAdapter = null;

    private static final String SAVED_STATE_EXPANDABLE_ITEM_MANAGER = "RecyclerViewExpandableItemManager";
    private Bundle savedInstanceState;
    private Context context;

    private RecyclerView yearsRecyclerView;
    private CarouselLayoutManager carouselLayoutManager;
    private String TAG = MainActivity.class.getSimpleName();
    private SpringFloatingActionMenu springFloatingActionMenu;
    private int frameDuration = 20;
    private AnimationDrawable frameAnim;
    private AnimationDrawable frameReverseAnim;
    private Resources resources;
    private static int[] frameAnimRes = new int[]{
            R.drawable.ic_add,
            R.drawable.ic_close,
    };

    public MainActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_main);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);
        context = getApplicationContext();
        resources = getResources();

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitleEnabled(false);

        createFabFrameAnim();
        createFabReverseFrameAnim();

        final FloatingActionButton fab = new FloatingActionButton(this);
        fab.setType(FloatingActionButton.TYPE_NORMAL);
        fab.setImageDrawable(frameAnim);
//        fab.setImageResource(android.R.drawable.ic_dialog_email);
        fab.setColorPressedResId(R.color.colorPrimary);
        fab.setColorNormalResId(R.color.fab_normal_state);
        fab.setColorRippleResId(R.color.text_color);
        fab.setShadow(true);

        progressBar = (ProgressBar) findViewById(R.id.progress);
        springFloatingActionMenu = new SpringFloatingActionMenu.Builder(this)
                .fab(fab)
                .addMenuItem(R.color.menu_add_mission, R.drawable.ic_add_mission, resources.getString(R.string.add_mission), R.color.text_color, this)
                .addMenuItem(R.color.menu_invite_friends, R.drawable.ic_person_add, resources.getString(R.string.share), R.color.text_color, this)
                .addMenuItem(R.color.menu_settings, R.drawable.ic_settings, resources.getString(R.string.settings), R.color.text_color, this)
                .addMenuItem(R.color.menu_transparent, -1, "", R.color.text_color, this)
                .addMenuItem(R.color.menu_help, R.mipmap.ic_help, resources.getString(R.string.help), R.color.text_color, this)
                .animationType(SpringFloatingActionMenu.ANIMATION_TYPE_TUMBLR)
                .revealColor(R.color.menu_background)
                .gravity(Gravity.RIGHT | Gravity.BOTTOM)
                .onMenuActionListner(new OnMenuActionListener() {
                    @Override
                    public void onMenuOpen() {
                        fab.setImageDrawable(frameAnim);
                        frameReverseAnim.stop();
                        frameAnim.start();
                    }

                    @Override
                    public void onMenuClose() {
                        fab.setImageDrawable(frameReverseAnim);
                        frameAnim.stop();
                        frameReverseAnim.start();
                    }
                })
                .build();
        initYearsRecycleView();
        presenter = new MainActivityPresenterImpl(this, getApplicationContext());

    }

    private void createFabFrameAnim() {
        frameAnim = new AnimationDrawable();
        frameAnim.setOneShot(true);
        for (int res : frameAnimRes) {
            frameAnim.addFrame(resources.getDrawable(res), frameDuration);
        }
    }

    private void createFabReverseFrameAnim() {
        frameReverseAnim = new AnimationDrawable();
        frameReverseAnim.setOneShot(true);
        for (int i = frameAnimRes.length - 1; i >= 0; i--) {
            frameReverseAnim.addFrame(resources.getDrawable(frameAnimRes[i]), frameDuration);
        }
    }

    private void initYearsRecycleView() {
        // vertical and cycle layout
        carouselLayoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, true);
        carouselLayoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());

        yearsRecyclerView = (RecyclerView) findViewById(R.id.years_recycler_view);
        yearsRecyclerView.setLayoutManager(carouselLayoutManager);
        yearsRecyclerView.setHasFixedSize(true);

    }


    private void initRecycleView(ExpandableDataProvider dataProvider) {
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
        adapter = new ExpandSwipeViewAdapter(recyclerViewExpandableItemManager, dataProvider, getApplicationContext());
        adapter.setEventListener(this);

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
    public void onBackPressed() {
        super.onBackPressed();
        if (springFloatingActionMenu.isMenuOpen()) {
            springFloatingActionMenu.hideMenu();
        } else {
            super.onBackPressed();
        }
//        finish();
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
//        Icepick.saveInstanceState(this, outState);

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

        runOnUiThread(new Runnable() {
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideProgress() {
        runOnUiThread(new Runnable() {
            public void run() {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void setYears(final ArrayList<YearEntity> yearsList) {
        Log.d(TAG, yearsList.toString());


        yearsAdapter = new YearsCoverFlowAdapter(this, yearsList);
        yearsRecyclerView.setAdapter(yearsAdapter);
        yearsRecyclerView.addOnScrollListener(new CenterScrollListener());
        carouselLayoutManager.addOnItemSelectionListener(this);

    }

    @Override
    public void onCenterItemChanged(int adapterPosition) {
        Log.d("Main", "adapterPosition  " + adapterPosition);
        presenter.onMissionItems(yearsAdapter.getItem(adapterPosition).getYears());
    }

    @Override
    public void setMissionItems(final List<MissionDB> missionDBlList) {
        runOnUiThread(new Runnable() {
            public void run() {
                dataProvider = new ExpandableDataProvider(missionDBlList);
                if (adapter == null) {
                    Log.d(TAG, "setMissionItems adapter = null");
                    initRecycleView(dataProvider);
                } else {
                    Log.d(TAG, "setMissionItems adapter refresh");
                    adapter.refresh(dataProvider);
                }
            }
        });
    }

    @Override
    public void showMessageSnackbar(int message, int action, final int groupPosition, final int childPosition) {
        final int missionId = dataProvider.getMissionItem(groupPosition).getMission().getId();
        int flightId = -1;
        if (childPosition != -1)
            flightId = dataProvider.getFlightItem(groupPosition, childPosition).getFlight().getId();

        final int finalFlightId = flightId;
        Snackbar snackbar = Snackbar.make(
                coordinatorLayout,
                message,
                Snackbar.LENGTH_LONG).setCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if (event != Snackbar.Callback.DISMISS_EVENT_ACTION)
                    if (childPosition != -1) {
                        presenter.onDeleteFlight(missionId, finalFlightId);
                    } else {
                        presenter.onDeleteMission(missionId);
                    }
            }

            @Override
            public void onShown(Snackbar snackbar) {
                if (childPosition != -1) {
                    dataProvider.removeFlightItem(groupPosition, childPosition);
                    adapter.notifyDataSetChanged();
                    AbstractExpandableDataProvider.MissionData data = dataProvider.getMissionItem(groupPosition);

                    if (data.isPinned()) {
                        // unpin if tapped the pinned item
                        data.setPinned(true);
                    }
                } else {
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
    public void showAlertDialog(DeleteDialogClickListener listener) {

    }

    @Override
    public void showSnackBar(String message) {

    }


    @Override
    public void onGroupItemRemoved(int groupPosition) {
        Log.d(TAG, "onGroupItemRemoved(groupPosition = " + groupPosition);

        final DialogFragment dialog = DeleteItemDialog.newInstance(groupPosition, RecyclerView.NO_POSITION);
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
    public void onClickToDeleteFlight(int groupPosition, int childPosition) {

    }

    @Override
    public void onChildItemRemoved(int groupPosition, int childPosition) {
        showMessageSnackbar(R.string.snack_bar_text_child_item_removed, R.string.snack_bar_action_undo, groupPosition, childPosition);
    }

    @Override
    public void onGroupItemPinned(int groupPosition) {
        Log.d(TAG, "onGroupItemPinned " + groupPosition);
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
    public void onUnderSwipeAddFlightButtonClicked(int groupPosition) {
        presenter.navigateToCreateFlight(dataProvider.getMissionItem(groupPosition).getMission());
    }

    @Override
    public void onFlightItemCreated(int id, FlightDB flight) {
        dataProvider.addFlightItem(id, flight);
        int childCount = dataProvider.getFlightCount(id);
        recyclerViewExpandableItemManager.notifyChildItemInserted(id, childCount - 1);

    }

    @Override
    public void onUnderSwipeEditMissionButtonClicked(int groupPosition) {
        presenter.navigateToChangeMission(dataProvider.getMissionItem(groupPosition).getMission(), groupPosition);
    }

    @Override
    public void onEditFlightSwiped(int groupPosition, int childPosition) {
        presenter.navigateToChangeFlight(dataProvider.getMissionItem(groupPosition).getMission(), groupPosition, childPosition);

    }

    @Override
    public void onClick(View v) {

        MenuItemView menuItemView = (MenuItemView) v;
        if (menuItemView != null) {
            String menu = menuItemView.getLabelTextView().getText().toString();
            if (menu.equals(resources.getString(R.string.add_mission)))
                presenter.navigateToCreateMission();
            else if (menu.equals(resources.getString(R.string.share))) {
                final String appPackageName = "org.telegram.messenger";
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }

        }

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

    @Override
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
        Log.d(TAG, "notifyGroupItemChanged" + groupPosition);
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

