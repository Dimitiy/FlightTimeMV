package com.android.flighttime.utils;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.flighttime.listener.GoogleEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;

/**
 * Created by OldMan on 29.05.2016.
 */
public class GoogleClient implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = GoogleClient.class.getSimpleName();
    private Context context;
    private GoogleEventListener googleEventListener;
    /**
     * Request code for auto Google Play Services error resolution.
     */
    protected static final int REQUEST_CODE_RESOLUTION = 1;

    /**
     * Next available request code.
     */
    protected static final int NEXT_AVAILABLE_REQUEST_CODE = 2;

    /**
     * Google API client.
     */
    @Nullable
    private GoogleApiClient googleApiClient;


    public GoogleClient(int typeClient, Context context, GoogleEventListener googleEventListener) {
        this.context = context;
        this.googleEventListener = googleEventListener;
        if (googleApiClient == null) {
            if (typeClient == Constants.GOOGLE_LOCATION_CLIENT)
                googleApiClient = new GoogleApiClient.Builder(context)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .addApi(Places.GEO_DATA_API)
                        .build();
            else if (typeClient == Constants.GOOGLE_DRIVE_CLIENT)
                googleApiClient = new GoogleApiClient.Builder(this.context)
                        .addApi(Drive.API)
                        .addScope(Drive.SCOPE_FILE)
                        .addScope(Drive.SCOPE_APPFOLDER) // required for App Folder sample
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .build();
        }
        googleApiClient.connect();
    }

    /**
     * Handles resolution callbacks.
     */
    public void onConnect() {
        if (!googleApiClient.isConnected() && !googleApiClient.isConnecting()) {
            googleApiClient.connect();
        }
    }

    /**
     * Called when activity gets invisible. Connection to Drive service needs to
     * be disconnected as soon as an activity is invisible.
     */
    public void onDisconnect() {
        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }
    }

    /**
     * Called when {@code mGoogleApiClient} is connected.
     */
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "GoogleApiClient connected");
        googleEventListener.onConnected();
    }

    /**
     * Called when {@code mGoogleApiClient} is disconnected.
     */
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "GoogleApiClient connection suspended " + cause);
//        googleEventListener.onConnectionSuspended(cause);
    }

    /**
     * Called when {@code mGoogleApiClient} is trying to connect but failed.
     * Handle {@code result.getResolution()} if there is a resolution is
     * available.
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        Log.i(TAG, "GoogleApiClient connection failed: " + result.toString());
        if (!result.hasResolution()) {
            // show the localized error dialog.
            GoogleApiAvailability.getInstance().getErrorDialog((Activity) context, result.getErrorCode(), 0).show();
            return;
        }
        try {
            result.startResolutionForResult((Activity) context, REQUEST_CODE_RESOLUTION);
        } catch (IntentSender.SendIntentException e) {
            Log.e(TAG, "Exception while starting resolution activity", e);
        }
        googleEventListener.onDisconnected();
    }

    /**
     * Shows a toast message.
     */
    public void showMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * Getter for the {@code GoogleApiClient}.
     */
    public GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }

    static public boolean checkGooglePlayServicesAvailable(Context mContext) {
        final int status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(mContext);
        if (status == ConnectionResult.SUCCESS) {
            return true;
        }
        Log.d(TAG, "Google Play Services not available: " + GooglePlayServicesUtil.getErrorString(status));
        return false;
    }

}
