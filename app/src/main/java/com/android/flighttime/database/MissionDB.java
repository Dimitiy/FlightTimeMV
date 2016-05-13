package com.android.flighttime.database;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by OldMan on 13.02.2016.
 */
public class MissionDB extends RealmObject {
    @Required // Name is not nullable
    private String city;
    @PrimaryKey
    private int id;
    @Required // Name is not nullable
    private Date date;
    private long duration;
    private RealmList<FlightDB> flightDBRealmList;

//    public MissionDB(int id, String city,  Date date, long duration, RealmList<FlightDB> flightDBRealmList) {
//        this.city = city;
//        this.id = id;
//        this.date = date;
//        this.duration = duration;
//        this.flightDBRealmList = flightDBRealmList;
//    }

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

    public RealmList<FlightDB> getFlightDBRealmList() {
        return flightDBRealmList;
    }

    public void setFlightDBRealmList(RealmList<FlightDB> flightDBRealmList) {
        this.flightDBRealmList = flightDBRealmList;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    /**
     * Your constructor and any other accessor
     *  methods should go here.
     */

}
