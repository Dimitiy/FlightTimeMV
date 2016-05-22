package com.android.flighttime.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceBuffer;
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
        onBuildGoogleApiClient();
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
    public void onStop() {
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onBuildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .build();
    }

    @Override
    public void onDestroy() {
        if (cityView != null)
            cityView.setGoogleApiClient(false);
    }

    public void onPlaceResult(String placeId) {
        final PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                .getPlaceById(googleApiClient, placeId);
        placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
            @Override
            public void onResult(PlaceBuffer places) {
                if (places.getCount() == 1) {
                    cityView.showRecycleView();
                } else {
                    Toast.makeText(context, "OOPs!!! Something went wrong..", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        cityView.setGoogleApiClient(true);
    }

    @Override
    public void onConnectionSuspended(int i) {
        cityView.setGoogleApiClient(false);
    }

    @Override
    public void onStart() {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        cityView.setGoogleApiClient(false);
    }
}
