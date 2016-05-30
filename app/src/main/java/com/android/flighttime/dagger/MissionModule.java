package com.android.flighttime.dagger;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.flighttime.mission.MissionCreatorPresenterImpl;
import com.android.flighttime.mission.MissionCreatorView;

//import javax.inject.Singleton;
//
//import dagger.Module;
//import dagger.Provides;

/**
 * Created by oldman on 25.05.16.
 */
//@Module
public class MissionModule {
    public MissionModule() {
    }
    // Dagger will only look for methods annotated with @Provides
//    @Provides
//    @Singleton
    MissionCreatorPresenterImpl providePresenter(MissionCreatorView missionView, Context context) {
        return new MissionCreatorPresenterImpl(missionView, context);
    }

}
