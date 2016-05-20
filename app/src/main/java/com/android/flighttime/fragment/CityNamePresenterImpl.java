package com.android.flighttime.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;

/**
 * Created by oldman on 19.05.16.
 */
public class CityNamePresenterImpl implements CityNamePresenter, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private final CityNameView cityView;
    private final Context context;
    protected GoogleApiClient googleApiClient;


    public CityNamePresenterImpl(CityNameView cityView, Context context) {
        this.cityView = cityView;
        this.context = context;
    }


    @Override
    public void onResume() {
        if (cityView != null) {
            cityView.showProgress();
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
