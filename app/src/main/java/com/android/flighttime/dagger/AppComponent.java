package com.android.flighttime.dagger;

import android.app.Application;

import com.android.flighttime.MyApplication;
import com.android.flighttime.main.MainActivityPresenterImpl;


/**
 * Created by oldman on 22.08.16.
 */
//@Singleton
//@Component(modules = AppModule.class)
public interface AppComponent {
    MainActivityComponent presenter(MainActivityModule module);

    Application application();
}
