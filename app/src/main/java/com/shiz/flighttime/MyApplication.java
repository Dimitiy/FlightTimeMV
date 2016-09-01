package com.shiz.flighttime;

import android.app.Application;
import android.content.Context;

import com.shiz.flighttime.utils.Constants;

import io.realm.Realm;
import io.realm.RealmConfiguration;

//import butterknife.ButterKnife;

/**
 * Created by OldMan on 14.02.2016.
 */

public class MyApplication extends Application {


    private static MyApplication application;

    public static MyApplication get(Context context) {
        return (MyApplication) context.getApplicationContext();
    }

    public static Realm getRealm() {
        return Realm.getDefaultInstance();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        RealmConfiguration config = new RealmConfiguration.Builder(this).name(getResources().getString(R.string.app_name)).deleteRealmIfMigrationNeeded().
        schemaVersion(Constants.VersionRealm).
                build();
        Realm.setDefaultConfiguration(config);
    }
}