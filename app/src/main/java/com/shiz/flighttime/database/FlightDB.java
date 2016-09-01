package com.shiz.flighttime.database;

import org.parceler.Parcel;

import java.util.Date;

import io.realm.FlightDBRealmProxy;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.annotations.Required;

/**
 * Created by OldMan on 13.02.2016.
 */
@RealmClass
@Parcel(implementations = {FlightDBRealmProxy.class},
        value = Parcel.Serialization.BEAN,
        analyze = {FlightDB.class})
public class FlightDB extends RealmObject {
    @PrimaryKey
    private int id;
    @Required // Name is not nullable
    private Date date;
    private long duration;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
