package com.android.flighttime.mission;

import android.content.Context;

/**
 * Created by oldman on 19.05.16.
 */
public class MissionCreatorPresenterImpl implements MissionCreatorPresenter {
    private final MissionCreatorView missionView;
    private final Context context;


    public MissionCreatorPresenterImpl(MissionCreatorView missionView, Context context) {
        this.missionView = missionView;
        this.context = context;
    }


    @Override
    public void onResume() {
        if (missionView != null) {
            missionView.showProgress();
        }
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onDestroy() {

    }
}
