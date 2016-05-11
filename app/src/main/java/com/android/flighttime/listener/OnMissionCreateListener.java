package com.android.flighttime.listener;

import java.util.Calendar;

/**
 * Created by OldMan on 10.05.2016.
 */
public interface OnMissionCreateListener {
    void onMissionCreated(String address, Calendar calendar);
}
