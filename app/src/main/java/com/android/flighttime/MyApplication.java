package com.android.flighttime;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;
import android.Manifest;

import com.android.flighttime.utils.Constants;

//import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by OldMan on 14.02.2016.
 */

public class MyApplication extends Application {


    private static MyApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        RealmConfiguration config = new RealmConfiguration.Builder(this).name(getResources().getString(R.string.app_name)).schemaVersion(Constants.VersionRealm).
                build();
        Realm.setDefaultConfiguration(config);
//       if(!checkRealm())
//           Toast.makeText(this,"Oops you just denied the write the storage permission",Toast.LENGTH_LONG).show();


//        ButterKnife.setDebug(BuildConfig.DEBUG);

    }



    public boolean checkRealm() {
        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {

            return true;
        } else
            return false;
    }

    public static MyApplication get(Context context) {
        return (MyApplication) context.getApplicationContext();
    }
    public static Realm getRealm(){
        return Realm.getDefaultInstance();
    }
}