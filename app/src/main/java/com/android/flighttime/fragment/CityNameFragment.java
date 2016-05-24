package com.android.flighttime.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.flighttime.R;
import com.android.flighttime.adapters.PlacesAutoCompleteAdapter;
import com.android.flighttime.listener.CityChangeListener;
import com.android.flighttime.listener.OnBackPressedListener;
import com.android.flighttime.listener.RecyclerItemFromAutoCompleteClickListener;


public class CityNameFragment extends Fragment implements CityNameView, View.OnClickListener, OnBackPressedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private PlacesAutoCompleteAdapter autoCompleteAdapter;
    private volatile boolean isGoogleApiClient = false;
    private EditText autocompleteView;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    private ImageView delete;
    private String address;

    // TODO: Rename and change types of parameters
    private String oldNameCity;
    private CityNamePresenter presenter;
    private CityChangeListener cityChangeListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment CityNameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CityNameFragment newInstance(String param1) {
        CityNameFragment fragment = new CityNameFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public void addCityNameChangedListener(CityChangeListener listener) {
        this.cityChangeListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            oldNameCity = getArguments().getString(ARG_PARAM1);
        }
        presenter = new CityNamePresenterImpl(this, getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_city_name, container, false);
        autocompleteView = (EditText) view.findViewById(R.id.autocomplete_places);
        autocompleteView.setFocusable(true);
        if (oldNameCity != null)
            autocompleteView.setText(oldNameCity);
        delete = (ImageView) view.findViewById(R.id.cross);

        recyclerView = (RecyclerView) view.findViewById(R.id.city_recycler_view);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(autoCompleteAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        delete.setOnClickListener(this);

        autocompleteView.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (!s.toString().equals("")) {
                    if (getGoogleApiClient()) {
                        recyclerView.setVisibility(View.VISIBLE);
                        autoCompleteAdapter.getFilter().filter(s.toString());
                    }

                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {
                if(cityChangeListener != null)
                    cityChangeListener.onNameCityChange(s.toString());
            }
        });
        recyclerView.addOnItemTouchListener(
                new RecyclerItemFromAutoCompleteClickListener(getActivity(), new RecyclerItemFromAutoCompleteClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        final PlacesAutoCompleteAdapter.PlaceAutocomplete item = autoCompleteAdapter.getItem(position);
                        final String placeId = String.valueOf(item.placeId);
                        address = String.valueOf(item.description);
                        autocompleteView.setText(address);
                    }
                })
        );
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == delete) {
            autocompleteView.setText("");
            recyclerView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        if (recyclerView.getVisibility() == View.VISIBLE)
            recyclerView.setVisibility(View.INVISIBLE);
        else
            super.getActivity().onBackPressed();
    }

    @Override
    public void onStart() {

        super.onStart();

    }

    @Override
    public void onStop() {
        presenter.onStop();
        super.onStop();
    }
    @Override
    public void onDestroyView() {
        cityChangeListener = null;
        super.onStop();
    }
    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showRecycleView() {
        recyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setGoogleApiClient(boolean isGoogleApiClient) {
        this.isGoogleApiClient = isGoogleApiClient;
    }

    private boolean getGoogleApiClient() {
        return isGoogleApiClient;
    }
}
