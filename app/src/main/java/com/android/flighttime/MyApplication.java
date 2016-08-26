package com.android.flighttime;

import android.app.Application;
import android.content.Context;

import com.android.flighttime.dagger.AppComponent;
import com.android.flighttime.dagger.AppModule;

//import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by OldMan on 14.02.2016.
 */

public class MyApplication extends Application  {

    private static AppComponent appComponent;
    private static MyApplication application;
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        RealmConfiguration config = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(config);
//        ButterKnife.setDebug(BuildConfig.DEBUG);

    }

    public static MyApplication get(Context context) {
        return (MyApplication) context.getApplicationContext();
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }
}