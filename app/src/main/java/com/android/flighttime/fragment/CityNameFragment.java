package com.android.flighttime.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.flighttime.R;
import com.android.flighttime.adapters.PlacesAutoCompleteAdapter;
import com.android.flighttime.listener.OnBackPressedListener;
import com.android.flighttime.listener.RecyclerItemFromAutoCompleteClickListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;


public class CityNameFragment extends Fragment implements  View.OnClickListener, OnBackPressedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private PlacesAutoCompleteAdapter autoCompleteAdapter;

    private View.OnClickListener onClickListener;
    private EditText autocompleteView;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    private ImageView delete;
    private String address;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CityNameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CityNameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CityNameFragment newInstance(String param1, String param2) {
        CityNameFragment fragment = new CityNameFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_city_name, container, false);
        autocompleteView = (EditText) view.findViewById(R.id.autocomplete_places);
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
                if (!s.toString().equals("") && googleApiClient.isConnected()) {
//                    if (mRecyclerView.getVisibility() == View.INVISIBLE) {
                    recyclerView.setVisibility(View.VISIBLE);
                    autoCompleteAdapter.getFilter().filter(s.toString());

//                    }
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {

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

                        final PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                                .getPlaceById(googleApiClient, placeId);
                        placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                            @Override
                            public void onResult(PlaceBuffer places) {
                                if (places.getCount() == 1) {
                                    //Do the things here on Click.....
//                                    Toast.makeText(getApplicationContext(), String.valueOf(places.get(0).getLatLng()), Toast.LENGTH_SHORT).show();
                                    recyclerView.setVisibility(View.INVISIBLE);
                                } else {
                                    Toast.makeText(getActivity(), "OOPs!!! Something went wrong..", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                })
        );
        return view;
    }


    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
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
}
