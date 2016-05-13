package com.android.flighttime.listener;

import android.view.View;

/**
 * Created by oldman on 20.04.16.
 */
public interface EventListener {
    void onGroupItemRemoved(int groupPosition);

    void onChildItemRemoved(int groupPosition, int childPosition);

    void onGroupItemPinned(int groupPosition);

    void onChildItemPinned(int groupPosition, int childPosition);

    void onItemViewClicked(View v, boolean pinned);

    void onUnderSwipeAddFlightButtonClicked(int groupPosition, View v);

    void onUnderSwipeEditMissionButtonClicked(View v);
}
