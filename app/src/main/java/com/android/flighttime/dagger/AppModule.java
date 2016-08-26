package com.android.flighttime.dagger;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.android.flighttime.MyApplication;
import com.android.flighttime.main.MainActivityPresenterImpl;


/**
 * Created by oldman on 22.08.16.
 */
//@Module
public class AppModule {

    private MyApplication application;

    public AppModule(MyApplication application) {
        this.application = application;
    }

//    @Provides
//    @Singleton
    public Application provideApplication() {
        return application;
    }

//    @Provides
//    @Singleton
    public Resources provideResources() {
        return application.getResources();
    }
}
