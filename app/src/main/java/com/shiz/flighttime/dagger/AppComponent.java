package com.shiz.flighttime.dagger;

import android.app.Application;


/**
 * Created by oldman on 22.08.16.
 */
//@Singleton
//@Component(modules = AppModule.class)
public interface AppComponent {
    MainActivityComponent presenter(MainActivityModule module);

    Application application();
}
