package com.android.flighttime.mission;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.flighttime.database.DBHelper;
import com.android.flighttime.main.FindItemsInteractorImpl;
import com.android.flighttime.main.MainView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;

/**
 * Created by oldman on 19.05.16.
 */
public class MissionCreatorPresenterImpl implements MissionCreatorPresenter, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private final MissionCreatorView missionView;
    private final Context context;
    protected GoogleApiClient googleApiClient;


    public MissionCreatorPresenterImpl(MissionCreatorView missionView, Context context) {
        this.missionView = missionView;
        this.context = context;
    }


    @Override
    public void onResume() {
        if (missionView != null) {
            missionView.showProgress();
        }
        if (!googleApiClient.isConnected() && !googleApiClient.isConnecting()) {
            Log.d("Google API", "Connecting");
            googleApiClient.connect();
        }
    }

    @Override
    public void onPause() {
        if (googleApiClient.isConnected()) {
            Log.v("Google API", "Dis-Connecting");
            googleApiClient.disconnect();
        }
    }
    @Override
    public void onBuildGoogleApiClient(String year) {
        googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .build();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
