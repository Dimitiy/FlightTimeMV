package com.android.flighttime.database;


import android.os.Parcel;

import org.parceler.Parcels;

/**
 * Created by oldman on 19.08.16.
 */
public class FlightDBListParcelConverter extends RealmListParcelConverter<FlightDB> {


    @Override
    public void itemToParcel(FlightDB input, Parcel parcel) {
        parcel.writeParcelable(Parcels.wrap(input), 0);

    }

    @Override
    public FlightDB itemFromParcel(Parcel parcel) {
        return Parcels.unwrap(parcel.readParcelable(FlightDB.class.getClassLoader()));

    }
}