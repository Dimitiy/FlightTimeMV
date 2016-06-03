package com.android.flighttime.fragment;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.EditText;

import com.android.flighttime.R;
import com.android.flighttime.listener.GoogleEventListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
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

    public CityNamePresenterImpl(String oldCity, CityNameFragment cityView, FragmentActivity fragmentManager) {
        this.cityView = cityView;

        autocompleteFragment = (PlaceAutocompleteFragment)
                fragmentManager.getFragmentManager().findFragmentById(R.id.place_fragment);
        if (autocompleteFragment != null) {
            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES).build();
            autocompleteFragment.setOnPlaceSelectedListener(this);
            autocompleteFragment.setHint("Search a Location");
            autocompleteFragment.setFilter(typeFilter);
            if (oldCity != null)
                autocompleteFragment.setText(oldCity);
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
        autocompleteFragment.setText(place.getAddress());
        cityView.onPlaceSelected(place.getAddress().toString());

    }

    @Override
    public void onError(Status status) {
        cityView.onErrorPlaceSelection(status);
    }
}
