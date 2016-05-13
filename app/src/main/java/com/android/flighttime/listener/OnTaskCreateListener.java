package com.android.flighttime.listener;

import com.android.flighttime.data.AbstractExpandableDataProvider;

import java.util.Calendar;

/**
 * Created by OldMan on 10.05.2016.
 */
public interface OnTaskCreateListener {
    void onMissionCreated(String address, Calendar calendar);
    void onFlightCreated(AbstractExpandableDataProvider.MissionData mission, Calendar calendar);
}
