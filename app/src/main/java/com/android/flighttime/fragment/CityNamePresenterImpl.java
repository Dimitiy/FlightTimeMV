package com.android.flighttime.fragment;

import android.support.v4.app.FragmentActivity;
import android.widget.EditText;

import com.android.flighttime.R;
import com.android.flighttime.listener.GoogleEventListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

/**
 * Created by oldman on 19.05.16.
 */
public class CityNamePresenterImpl implements PlaceSelectionListener, GoogleEventListener, CityNamePresenter {
    private final CityNameView cityView;
    //    private final Context context;
    PlaceAutocompleteFragment autocompleteFragment;
//    public CityNamePresenterImpl(CityNameView cityView, Context context) {
//        this.context = context;
//
//    }

    public CityNamePresenterImpl(CityNameFragment cityView, FragmentActivity fragmentManager) {
        this.cityView = cityView;

        autocompleteFragment = (PlaceAutocompleteFragment)
                fragmentManager.getFragmentManager().findFragmentById(R.id.place_fragment);
        if (autocompleteFragment != null) {
            autocompleteFragment.setOnPlaceSelectedListener(this);
            autocompleteFragment.setHint("Search a Location");
        }
//        autocompleteFragment.setBoundsBias(BOUNDS_MOUNTAIN_VIEW);
    }


    @Override
    public void onResume() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onDestroy() {
        autocompleteFragment.onDestroy();
    }

    @Override
    public void onConnected() {

    }

    @Override
    public void onDisconnected() {

    }


    @Override
    public void onPlaceSelected(Place place) {
        cityView.onPlaceSelected(autocompleteFragment.getString(R.string.formatted_place_data, place
                .getName(), place.getAddress(), place.getPhoneNumber(), place
                .getWebsiteUri(), place.getRating(), place.getId()));

//        if (!TextUtils.isEmpty(place.getAttributions())){
//            attributionsTextView.setText(Html.fromHtml(place.getAttributions().toString() + " " + place.getAddress()));
//            cityChangeListener.onNameCityChange(Html.fromHtml(place.getAttributions().toString()).toString());
    }

    @Override
    public void onError(Status status) {
        cityView.onErrorPlaceSelection(status);
    }
}
