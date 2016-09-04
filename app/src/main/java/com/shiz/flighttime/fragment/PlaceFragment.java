package com.shiz.flighttime.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shiz.flighttime.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.shiz.flighttime.listener.CityChangeListener;
import com.shiz.flighttime.listener.GoogleEventListener;
import com.shiz.flighttime.listener.OnBackPressedListener;
import com.shiz.flighttime.utils.GoogleClient;

/**
 * Created by OldMan on 01.09.2016.
 */
public class PlaceFragment extends BaseNameFragment implements PlaceSelectionListener, GoogleEventListener, CityNameView, OnBackPressedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String TAG = PlaceFragment.class.getSimpleName();
    private String oldNameCity = null;
    private CityChangeListener cityChangeListener;
    private PlaceAutocompleteFragment autocompleteFragment;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment CityNameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlaceFragment newInstance(String param1) {
        PlaceFragment fragment = new PlaceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            oldNameCity = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place, container, false);

        if (GoogleClient.checkGooglePlayServicesAvailable(getContext())) {
            autocompleteFragment = (PlaceAutocompleteFragment)
                    getActivity().getFragmentManager().findFragmentById(R.id.place);
            if (autocompleteFragment != null) {
                AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                        .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES).build();
                autocompleteFragment.setOnPlaceSelectedListener(this);
                autocompleteFragment.setHint("Search a Location");
                autocompleteFragment.setFilter(typeFilter);
                if (oldNameCity != null)
                    autocompleteFragment.setText(oldNameCity);
            }
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView");
        cityChangeListener = null;
        super.onDestroyView();
    }

    @Override
    public void showProgress() {
    }

    @Override
    public void hideProgress() {
    }

    @Override
    public void onPlaceSelected(String place) {
        if (cityChangeListener != null)
            cityChangeListener.onNameCityChange(place);
    }

    @Override
    public void onErrorPlaceSelection(Status status) {
        Log.e(TAG, "onError: Status = " + status.toString());
        Toast.makeText(getContext(), "Place selection failed: " + status.getStatusMessage(),
                Toast.LENGTH_SHORT).show();

    }

    public void addCityNameChangedListener(CityChangeListener cityChangeListener) {
        this.cityChangeListener = cityChangeListener;
    }

    @Override
    public void onBackPressed() {

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
        onPlaceSelected(place.getAddress().toString());
    }

    @Override
    public void onError(Status status) {
        onErrorPlaceSelection(status);
    }
}

