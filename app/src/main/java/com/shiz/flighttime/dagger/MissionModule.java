package com.shiz.flighttime.dagger;

import android.content.Context;

import com.shiz.flighttime.mission.MissionCreatorPresenterImpl;
import com.shiz.flighttime.mission.MissionCreatorView;

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
