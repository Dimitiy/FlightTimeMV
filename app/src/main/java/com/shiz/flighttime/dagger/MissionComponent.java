package com.shiz.flighttime.dagger;

import com.shiz.flighttime.mission.MissionCreatorActivity;

//import javax.inject.Singleton;
//
//import dagger.Component;

/**
 * Created by oldman on 25.05.16.
 */

//@Component(modules={MissionModule.class})
//@Singleton
public interface MissionComponent {

    //    public MissionCreatorPresenter getMissionCreatorPresenter();
    void inject(MissionCreatorActivity activity);

}
