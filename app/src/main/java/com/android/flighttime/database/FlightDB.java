package com.android.flighttime.database;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by OldMan on 13.02.2016.
 */
public class FlightDB extends RealmObject {
    @PrimaryKey
    private int id;
    @Required // Name is not nullable
    private Date date;
    private long duration;

//    public FlightDB(long duration, Date date, int id) {
//        this.duration = duration;
//        this.date = date;
//        this.id = id;
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
}
